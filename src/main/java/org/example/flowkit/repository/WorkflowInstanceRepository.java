package org.example.flowkit.repository;

import org.example.flowkit.entity.ActivityInstance;
import org.example.flowkit.entity.Associates;
import org.example.flowkit.entity.WorkflowInstance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowInstanceRepository extends CrudRepository<WorkflowInstance, Long> {

    @Query(value = "select a from workflow_instance a WHERE a.workflowFor = ?1")
    List<WorkflowInstance> findByCustomerId(Associates customer);

    @Query(value = "select a from workflow_instance a join activity_instance b on b=?1 and b.workflowInstance=a")
    WorkflowInstance getWorkflowInstanceByActivityInstances(ActivityInstance a);


}
