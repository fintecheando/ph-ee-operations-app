package org.apache.fineract.infrastructure.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fineract")
public class FineractProperties {

    private String nodeId;

    private FineractTenantProperties tenant;

    private FineractModeProperties mode;

    @Getter
    @Setter
    public static class FineractTenantProperties {

        private String host;
        private Integer port;
        private String username;
        private String password;
        private String parameters;
        private String timezone;
        private String identifier;
        private String name;
        private String description;
    }

    @Getter
    @Setter
    public static class FineractModeProperties {

        private boolean readEnabled;
        private boolean writeEnabled;
        private boolean batchEnabled;

        public boolean isReadOnlyMode() {
            return readEnabled && !writeEnabled && !batchEnabled;
        }
    }
}
