package org.apache.fineract.paymenthub.api;

import org.apache.fineract.paymenthub.domain.ErrorCode;
import org.apache.fineract.paymenthub.domain.ErrorCodeRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping(value=OperationsConstants.API_VERSION_PATH, produces=MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ErrorCodesApi {

    private final ErrorCodeRepository errorCodesRepository;

    @PostMapping(value = OperationsConstants.API_ERRORCODE_PATH, consumes = MediaType.APPLICATION_JSON)
    public ErrorCode create(@RequestBody ErrorCode errorCode) {
        return errorCodesRepository.save(errorCode);
    }

    @GetMapping(value = OperationsConstants.API_ERRORCODE_PATH)
    public List<ErrorCode> errorCodes() {
        List<ErrorCode> errorCodes = new ArrayList<>();
        errorCodesRepository.findAll().forEach(errorCodes::add);
        return errorCodes;
    }

    @GetMapping(value = OperationsConstants.API_ERRORCODE_PATH + "/{id}", consumes = MediaType.APPLICATION_JSON)
    public ErrorCode errorCode(@PathVariable Long id) {
        Optional<ErrorCode> optEntity = errorCodesRepository.findById(id);
        if (optEntity.isPresent()) {
            return optEntity.get();
        }
        return null;
    }

    @GetMapping(OperationsConstants.API_ERRORCODE_PATH + "/filter")
    public List<ErrorCode> errorCodeByFilter(@RequestParam("by") String filterType, @RequestParam("value") Object value) {
        switch (filterType){
            case "errorCode":
                return errorCodesRepository.getErrorCodesByErrorCode(value.toString());
            case "recoverable":
                boolean option = value.toString().equals("true");
                return errorCodesRepository.getErrorCodesByRecoverable(option);
            case "transactionType":
                return errorCodesRepository.getErrorCodesByTransactionType(value.toString());
            default:
                return null;
        }
    }

    @PutMapping(value =OperationsConstants.API_ERRORCODE_PATH + "/{id}", consumes = MediaType.APPLICATION_JSON)
    public void update(@PathVariable Long id, @RequestBody ErrorCode errorCode, HttpServletResponse response) {
        Optional<ErrorCode> optEntity = errorCodesRepository.findById(id);
        if (optEntity.isPresent()) {
            errorCode.setId(id);
            errorCodesRepository.save(errorCode);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @DeleteMapping(OperationsConstants.API_ERRORCODE_PATH + "/{id}")
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        Optional<ErrorCode> optEntity = errorCodesRepository.findById(id);
        if (optEntity.isPresent()) {
            errorCodesRepository.delete(optEntity.get());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
