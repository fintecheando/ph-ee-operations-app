package org.apache.fineract.paymenthub.domain;

import org.apache.fineract.organisation.parent.AbstractPersistableCustom;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "ph_errorcode")
public class ErrorCode extends AbstractPersistableCustom {

    @Column(name = "TRANSACTION_TYPE")
    String transactionType;

    @Column(name = "ERROR_MESSAGE")
    String errorMessage;

    @Column(name = "ERROR_CODE")
    String errorCode;

    @Column(name = "RECOVERABLE")
    boolean recoverable;

}
