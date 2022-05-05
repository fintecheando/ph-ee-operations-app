package org.apache.fineract.infrastructure.security.service;

import org.apache.fineract.core.domain.FineractPlatformTenant;

public interface BasicAuthTenantDetailsService {

    FineractPlatformTenant loadTenantById(String tenantId, boolean isReport);
}
