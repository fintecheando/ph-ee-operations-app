package org.apache.fineract.infrastructure.security.domain;

public interface PlatformUserRepository {

    PlatformUser findByUsernameAndDeletedAndEnabled(String username, boolean deleted, boolean enabled);

}