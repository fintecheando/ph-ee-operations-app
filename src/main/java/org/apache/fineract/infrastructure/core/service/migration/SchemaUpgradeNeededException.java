package org.apache.fineract.infrastructure.core.service.migration;

public class SchemaUpgradeNeededException extends RuntimeException {

    public SchemaUpgradeNeededException(String message) {
        super(message);
    }
}

