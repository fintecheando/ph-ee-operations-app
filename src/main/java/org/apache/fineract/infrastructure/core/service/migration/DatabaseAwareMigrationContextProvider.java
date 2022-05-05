package org.apache.fineract.infrastructure.core.service.migration;

import java.util.Map;
import org.apache.fineract.infrastructure.core.service.database.DatabaseType;
import org.apache.fineract.infrastructure.core.service.database.DatabaseTypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseAwareMigrationContextProvider {

    private final Map<DatabaseType, String> contextMapping = Map.of(DatabaseType.MYSQL, "mysql", DatabaseType.POSTGRESQL, "postgresql");

    private final DatabaseTypeResolver databaseTypeResolver;

    @Autowired
    public DatabaseAwareMigrationContextProvider(DatabaseTypeResolver databaseTypeResolver) {
        this.databaseTypeResolver = databaseTypeResolver;
    }

    public String provide() {
        DatabaseType databaseType = databaseTypeResolver.databaseType();
        String context = contextMapping.get(databaseType);
        if (context == null) {
            throw new IllegalStateException("Database is not supported");
        }
        return context;
    }
}
