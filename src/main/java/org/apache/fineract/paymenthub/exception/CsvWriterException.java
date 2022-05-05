package org.apache.fineract.paymenthub.exception;

import org.apache.fineract.paymenthub.data.ErrorCode;

public class CsvWriterException extends WriteToCsvException{

    public CsvWriterException(ErrorCode errorCode, String errorDescription) {
        super(errorCode, errorDescription);
    }

}
