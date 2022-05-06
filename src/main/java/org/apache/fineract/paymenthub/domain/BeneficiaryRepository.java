package org.apache.fineract.paymenthub.domain;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BeneficiaryRepository extends CrudRepository<Beneficiary, Long>, JpaSpecificationExecutor<Beneficiary> {

    List<Beneficiary> findBycustIdentifier(String custIdentifier);

    Beneficiary findOneByCustIdentifierAndIdentifier(String custIdentifier, String identifier);
}
