package org.apache.fineract.paymenthub.domain;

public enum TransactionRequestState {

    IN_PROGRESS,
    RECEIVED,
    ACCEPTED,
    REJECTED,
    FAILED;
}