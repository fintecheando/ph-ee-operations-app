package org.apache.fineract.core.domain;

import java.sql.Connection;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;

/**
 * Holds Tenant's DB server connection connection details.
 */
public class FineractPlatformTenantConnection {

    private final Long connectionId;
    private final String schemaServer;
    private final String schemaServerPort;
    private final String schemaConnectionParameters;
    private final String schemaUsername;
    private final String schemaPassword;
    private final String schemaName;
    private final boolean autoUpdateEnabled;
    private final int initialSize;
    private final long validationInterval;
    private final boolean removeAbandoned;
    private final int removeAbandonedTimeout;
    private final boolean logAbandoned;
    private final int abandonWhenPercentageFull;
    private final int maxActive;
    private final int minIdle;
    private final int maxIdle;
    private final int suspectTimeout;
    private final int timeBetweenEvictionRunsMillis;
    private final int minEvictableIdleTimeMillis;
    private final int maxRetriesOnDeadlock;
    private final int maxIntervalBetweenRetries;
    private final boolean testOnBorrow;

    public FineractPlatformTenantConnection(final Long connectionId, final String schemaName, String schemaServer,
            final String schemaServerPort, final String schemaConnectionParameters, final String schemaUsername,
            final String schemaPassword, final boolean autoUpdateEnabled, final int initialSize, final long validationInterval,
            final boolean removeAbandoned, final int removeAbandonedTimeout, final boolean logAbandoned,
            final int abandonWhenPercentageFull, final int maxActive, final int minIdle, final int maxIdle, final int suspectTimeout,
            final int timeBetweenEvictionRunsMillis, final int minEvictableIdleTimeMillis, final int maxRetriesOnDeadlock,
            final int maxIntervalBetweenRetries, final boolean tesOnBorrow) {

        this.connectionId = connectionId;
        this.schemaName = schemaName;
        this.schemaServer = schemaServer;
        this.schemaServerPort = schemaServerPort;
        this.schemaConnectionParameters = schemaConnectionParameters;
        this.schemaUsername = schemaUsername;
        this.schemaPassword = schemaPassword;
        this.autoUpdateEnabled = autoUpdateEnabled;
        this.initialSize = initialSize;
        this.validationInterval = validationInterval;
        this.removeAbandoned = removeAbandoned;
        this.removeAbandonedTimeout = removeAbandonedTimeout;
        this.logAbandoned = logAbandoned;
        this.abandonWhenPercentageFull = abandonWhenPercentageFull;
        this.maxActive = maxActive;
        this.minIdle = minIdle;
        this.maxIdle = maxIdle;
        this.suspectTimeout = suspectTimeout;
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
        this.maxRetriesOnDeadlock = maxRetriesOnDeadlock;
        this.maxIntervalBetweenRetries = maxIntervalBetweenRetries;
        this.testOnBorrow = tesOnBorrow;
    }

    public String getSchemaServer() {
        return this.schemaServer;
    }

    public String getSchemaServerPort() {
        return this.schemaServerPort;
    }

    public String getSchemaConnectionParameters() {
        return this.schemaConnectionParameters;
    }

    public String getSchemaUsername() {
        return this.schemaUsername;
    }

    public String getSchemaPassword() {
        return this.schemaPassword;
    }

    public boolean isAutoUpdateEnabled() {
        return this.autoUpdateEnabled;
    }

    public int getInitialSize() {
        return this.initialSize;
    }

    public long getValidationInterval() {
        return this.validationInterval;
    }

    public boolean isRemoveAbandoned() {
        return this.removeAbandoned;
    }

    public int getRemoveAbandonedTimeout() {
        return this.removeAbandonedTimeout;
    }

    public boolean isLogAbandoned() {
        return this.logAbandoned;
    }

    public int getAbandonWhenPercentageFull() {
        return this.abandonWhenPercentageFull;
    }

    public int getMaxActive() {
        return this.maxActive;
    }

    public int getMinIdle() {
        return this.minIdle;
    }

    public int getMaxIdle() {
        return this.maxIdle;
    }

    public int getSuspectTimeout() {
        return this.suspectTimeout;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }

    public int getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }

    public int getMaxRetriesOnDeadlock() {
        return this.maxRetriesOnDeadlock;
    }

    public int getMaxIntervalBetweenRetries() {
        return this.maxIntervalBetweenRetries;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public Long getConnectionId() {
        return connectionId;
    }

    public String getSchemaName() {
        return schemaName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.schemaName).append(":").append(this.schemaServer).append(":")
                .append(this.schemaServerPort);
        if (this.schemaConnectionParameters != null && !this.schemaConnectionParameters.isEmpty()) {
            sb.append('?').append(this.schemaConnectionParameters);
        }
        return sb.toString();
    }

    public static String toJdbcUrl(String protocol, String host, String port, String db, String parameters) {
        StringBuilder sb = new StringBuilder(protocol).append("://").append(host).append(":").append(port).append('/').append(db);

        if (!StringUtils.isEmpty(parameters)) {
            sb.append('?').append(parameters);
        }

        return sb.toString();
    }

    public static String toProtocol(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            String url = connection.getMetaData().getURL();
            return url.substring(0, url.indexOf("://"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
