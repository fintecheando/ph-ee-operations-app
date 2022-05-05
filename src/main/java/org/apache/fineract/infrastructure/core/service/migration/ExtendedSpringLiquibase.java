package org.apache.fineract.infrastructure.core.service.migration;

import java.sql.SQLException;
import liquibase.Liquibase;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;

public class ExtendedSpringLiquibase extends SpringLiquibase {

    public void changeLogSync() throws LiquibaseException {
        try (Liquibase liquibase = createLiquibase(getDataSource().getConnection())) {
            liquibase.changeLogSync(getContexts());
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
