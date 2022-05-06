package org.apache.fineract.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EndpointSetting {
    private String endpoint;
    private String authority;
}
