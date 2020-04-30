package org.example.flowkit.repository;

import org.example.flowkit.entity.WorkflowInstance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowInstanceRepository extends CrudRepository<WorkflowInstance, Long> {
    @Query(value = "select a from workflow_instance a WHERE a.workflowFor = ?1")
    List<WorkflowInstance> findByCustomerId(Long customer_id);

}
