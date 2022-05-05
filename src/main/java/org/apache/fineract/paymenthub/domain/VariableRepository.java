package org.apache.fineract.paymenthub.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VariableRepository extends CrudRepository<Variable, Long> {

    List<Variable> findByWorkflowInstanceKeyOrderByTimestamp(Long workflowInstanceKey);

}
