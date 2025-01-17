package org.apache.fineract.paymenthub.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {

    List<Task> findByWorkflowInstanceKeyOrderByTimestamp(Long workflowInstanceKey);

}
