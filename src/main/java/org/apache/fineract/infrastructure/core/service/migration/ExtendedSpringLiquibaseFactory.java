package org.apache.fineract.infrastructure.core.service.migration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class ExtendedSpringLiquibaseFactory {

    private final LiquibaseProperties liquibaseProperties;
    private final ResourceLoader resourceLoader;
    private final Environment environment;
    private final DatabaseAwareMigrationContextProvider databaseAwareMigrationContextProvider;

    @Autowired
    public ExtendedSpringLiquibaseFactory(LiquibaseProperties liquibaseProperties, ResourceLoader resourceLoader, Environment environment,
            DatabaseAwareMigrationContextProvider databaseAwareMigrationContextProvider) {
        this.liquibaseProperties = liquibaseProperties;
        this.resourceLoader = resourceLoader;
        this.environment = environment;
        this.databaseAwareMigrationContextProvider = databaseAwareMigrationContextProvider;
    }

    public ExtendedSpringLiquibase create(DataSource dataSource, String... contexts) {
        String databaseContext = databaseAwareMigrationContextProvider.provide();
        return new ExtendedSpringLiquibaseBuilder(liquibaseProperties).withDataSource(dataSource).withResourceLoader(resourceLoader)
                .withContexts(contexts).withContexts(environment.getActiveProfiles()).withContext(databaseContext).build();
    }
}
