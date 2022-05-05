package org.apache.fineract.paymenthub.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRequestRepository extends JpaRepository<TransactionRequest, Long>, JpaSpecificationExecutor<TransactionRequest> {

    TransactionRequest findFirstByWorkflowInstanceKey(Long workflowInstanceKey);

}
