package org.apache.fineract.paymenthub.api;


import org.apache.fineract.paymenthub.domain.BusinessKey;
import org.apache.fineract.paymenthub.domain.BusinessKeyRepository;
import org.apache.fineract.paymenthub.domain.Task;
import org.apache.fineract.paymenthub.domain.TaskRepository;
import org.apache.fineract.paymenthub.domain.TransactionRequest;
import org.apache.fineract.paymenthub.domain.TransactionRequestDetail;
import org.apache.fineract.paymenthub.domain.TransactionRequestRepository;
import org.apache.fineract.paymenthub.domain.Transfer;
import org.apache.fineract.paymenthub.domain.TransferDetail;
import org.apache.fineract.paymenthub.domain.TransferRepository;
import org.apache.fineract.paymenthub.domain.TransferStatus;
import org.apache.fineract.paymenthub.domain.Variable;
import org.apache.fineract.paymenthub.domain.VariableRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value=OperationsConstants.API_VERSION_PATH, produces=MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class OperationsApi {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BusinessKeyRepository businessKeyRepository;
    private final TaskRepository taskRepository;
    private final VariableRepository variableRepository;
    private final TransferRepository transferRepository;
    private final TransactionRequestRepository transactionRequestRepository;

    // @Autowired
    private RestTemplate restTemplate;

    @Value("${channel-connector.url}")
    private String channelConnectorUrl;

    @Value("${channel-connector.transfer-path}")
    private String channelConnectorTransferPath;

    @PostMapping(OperationsConstants.API_TRANSFERS_PATH + "/{transactionId}/refund")
    public String refundTransfer(@RequestHeader("Platform-TenantId") String tenantId,
                                 @PathVariable("transactionId") String transactionId,
                                 @RequestBody String requestBody,
                                 HttpServletResponse response) {
        Transfer existingIncomingTransfer = transferRepository.findFirstByTransactionIdAndDirection(transactionId, "INCOMING");
        if (existingIncomingTransfer == null || !TransferStatus.COMPLETED.equals(existingIncomingTransfer.getStatus())) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            JSONObject failResponse = new JSONObject();
            failResponse.put("response", "Requested incoming transfer does not exist or not yet completed!");
            return failResponse.toString();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Platform-TenantId", tenantId);
        httpHeaders.add("Content-Type", "application/json");
        // httpHeaders.add("Authorization", "Bearer token"); TODO auth needed?

        JSONObject channelRequest = new JSONObject();
        JSONObject payer = new JSONObject();
        JSONObject payerPartyIdInfo = new JSONObject();
        payerPartyIdInfo.put("partyIdType", existingIncomingTransfer.getPayeePartyIdType());
        payerPartyIdInfo.put("partyIdentifier", existingIncomingTransfer.getPayeePartyId());
        payer.put("partyIdInfo", payerPartyIdInfo);
        channelRequest.put("payer", payer);
        JSONObject payee = new JSONObject();
        JSONObject payeePartyIdInfo = new JSONObject();
        payeePartyIdInfo.put("partyIdType", existingIncomingTransfer.getPayerPartyIdType());
        payeePartyIdInfo.put("partyIdentifier", existingIncomingTransfer.getPayerPartyId());
        payee.put("partyIdInfo", payeePartyIdInfo);
        channelRequest.put("payee", payee);
        JSONObject amount = new JSONObject();
        amount.put("amount", existingIncomingTransfer.getAmount());
        amount.put("currency", existingIncomingTransfer.getCurrency());
        channelRequest.put("amount", amount);
        try {
            JSONObject body = new JSONObject(requestBody);
            String comment = body.optString("comment", null);
            if (comment != null) {
                JSONObject extensionList = new JSONObject();
                JSONArray extensions = new JSONArray();
                addExtension(extensions, "comment", comment);
                extensionList.put("extension", extensions);
                channelRequest.put("extensionList", extensionList);
            }
        } catch (Exception e) {
            logger.error("Could not parse refund request body {}, can not set comment on refund!", requestBody);
        }

        ResponseEntity<String> channelResponse = restTemplate.exchange(channelConnectorUrl + channelConnectorTransferPath,
                HttpMethod.POST,
                new HttpEntity<String>(channelRequest.toString(), httpHeaders),
                String.class);
        response.setStatus(channelResponse.getStatusCodeValue());
        return channelResponse.getBody();
    }

    private void addExtension(JSONArray extensionList, String key, String value) {
        JSONObject extension = new JSONObject();
        extension.put("key", key);
        extension.put("value", value);
        extensionList.put(extension);
    }

    @GetMapping(OperationsConstants.API_TRANSFERS_PATH + "/{workflowInstanceKey}")
    public TransferDetail transferDetails(@PathVariable Long workflowInstanceKey) {
        Transfer transfer = transferRepository.findFirstByWorkflowInstanceKey(workflowInstanceKey);
        List<Task> tasks = taskRepository.findByWorkflowInstanceKeyOrderByTimestamp(workflowInstanceKey);
        List<Variable> variables = variableRepository.findByWorkflowInstanceKeyOrderByTimestamp(workflowInstanceKey);
        return new TransferDetail(transfer, tasks, variables);
    }

    @GetMapping(OperationsConstants.API_TRANSACTION_PATH + "/{workflowInstanceKey}")
    public TransactionRequestDetail transactionRequestDetails(@PathVariable Long workflowInstanceKey) {
        TransactionRequest transactionRequest = transactionRequestRepository.findFirstByWorkflowInstanceKey(workflowInstanceKey);
        List<Task> tasks = taskRepository.findByWorkflowInstanceKeyOrderByTimestamp(workflowInstanceKey);
        List<Variable> variables = variableRepository.findByWorkflowInstanceKeyOrderByTimestamp(workflowInstanceKey);
        return new TransactionRequestDetail(transactionRequest, tasks, variables);
    }

    @GetMapping(OperationsConstants.API_VARIABLES_PATH)
    public List<List<Variable>> variables(
            @RequestParam(value = "businessKey") String businessKey,
            @RequestParam(value = "businessKeyType") String businessKeyType
    ) {
        return loadTransfers(businessKey, businessKeyType).stream()
                .map(transfer -> variableRepository.findByWorkflowInstanceKeyOrderByTimestamp(transfer.getWorkflowInstanceKey()))
                .collect(Collectors.toList());
    }

    @GetMapping(OperationsConstants.API_TASKS_PATH)
    public List<List<Task>> tasks(
            @RequestParam(value = "businessKey") String businessKey,
            @RequestParam(value = "businessKeyType") String businessKeyType
    ) {
        return loadTransfers(businessKey, businessKeyType).stream()
                .map(transfer -> taskRepository.findByWorkflowInstanceKeyOrderByTimestamp(transfer.getWorkflowInstanceKey()))
                .collect(Collectors.toList());
    }

    private List<BusinessKey> loadTransfers(@RequestParam("businessKey") String
                                                    businessKey, @RequestParam("businessKeyType") String businessKeyType) {
        List<BusinessKey> businessKeys = businessKeyRepository.findByBusinessKeyAndBusinessKeyType(businessKey, businessKeyType);
        logger.debug("loaded {} transfer(s) for business key {} of type {}", businessKeys.size(), businessKey, businessKeyType);
        return businessKeys;
    }
}
