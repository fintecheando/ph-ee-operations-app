package org.apache.fineract.paymenthub.domain;



import org.apache.fineract.organisation.parent.AbstractPersistableCustom;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import static org.apache.fineract.paymenthub.domain.TransactionRequestState.IN_PROGRESS;

import java.math.BigDecimal;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "ph_transaction_requests")
public class TransactionRequest extends AbstractPersistableCustom {

    @Column(name = "WORKFLOW_INSTANCE_KEY")
    private String workflowInstanceKey;

    @Column(name = "TRANSACTION_ID")
    private String transactionId;

    @Column(name = "STARTED_AT")
    private Date startedAt;

    @Column(name = "COMPLETED_AT")
    private Date completedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    private TransactionRequestState state;

    @Column(name = "PAYEE_DFSP_ID")
    private String payeeDfspId;
    @Column(name = "PAYEE_PARTY_ID")
    private String payeePartyId;
    @Column(name = "PAYEE_PARTY_ID_TYPE")
    private String payeePartyIdType;
    @Column(name = "PAYEE_FEE")
    private BigDecimal payeeFee;
    @Column(name = "PAYEE_QUOTE_CODE")
    private String payeeQuoteCode;

    @Column(name = "PAYER_DFSP_ID")
    private String payerDfspId;
    @Column(name = "PAYER_PARTY_ID")
    private String payerPartyId;
    @Column(name = "PAYER_PARTY_ID_TYPE")
    private String payerPartyIdType;
    @Column(name = "PAYER_FEE")
    private BigDecimal payerFee;
    @Column(name = "PAYER_QUOTE_CODE")
    private String payerQuoteCode;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "DIRECTION")
    private String direction;

    @Column(name = "AUTH_TYPE")
    private String authType;

    @Column(name = "INITIATOR_TYPE")
    private String initiatorType;

    @Column(name = "SCENARIO")
    private String scenario;

    public TransactionRequest() {
    }

    public TransactionRequest(String workflowInstanceKey) {
        this.workflowInstanceKey = workflowInstanceKey;
        this.state = IN_PROGRESS;
    }
}
