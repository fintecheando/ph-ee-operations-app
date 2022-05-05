package org.apache.fineract.infrastructure.core.service.migration;

import static org.apache.fineract.core.domain.FineractPlatformTenantConnection.toJdbcUrl;
import static org.apache.fineract.core.domain.FineractPlatformTenantConnection.toProtocol;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.fineract.core.domain.FineractPlatformTenant;
import org.apache.fineract.core.domain.FineractPlatformTenantConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TenantDataSourceFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TenantDataSourceFactory.class);

    private final HikariDataSource tenantDataSource;

    @Autowired
    public TenantDataSourceFactory(@Qualifier("hikariTenantDataSource") HikariDataSource tenantDataSource) {
        this.tenantDataSource = tenantDataSource;
    }

    public DataSource create(FineractPlatformTenant tenant) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(tenantDataSource.getDriverClassName());
        dataSource.setDataSourceProperties(tenantDataSource.getDataSourceProperties());
        dataSource.setMinimumIdle(tenantDataSource.getMinimumIdle());
        dataSource.setMaximumPoolSize(tenantDataSource.getMaximumPoolSize());
        dataSource.setIdleTimeout(tenantDataSource.getIdleTimeout());
        dataSource.setConnectionTestQuery(tenantDataSource.getConnectionTestQuery());

        FineractPlatformTenantConnection tenantConnection = tenant.getConnection();
        dataSource.setUsername(tenantConnection.getSchemaUsername());
        dataSource.setPassword(tenantConnection.getSchemaPassword());
        String protocol = toProtocol(tenantDataSource);
        String tenantJdbcUrl = toJdbcUrl(protocol, tenantConnection.getSchemaServer(), tenantConnection.getSchemaServerPort(),
                tenantConnection.getSchemaName(), tenantConnection.getSchemaConnectionParameters());
        LOG.info("JDBC URL for tenant {} is {}", tenant.getTenantIdentifier(), tenantJdbcUrl);
        dataSource.setJdbcUrl(tenantJdbcUrl);
        return dataSource;
    }
}
