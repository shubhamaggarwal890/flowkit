package org.example.flowkit.repository;

import org.example.flowkit.entity.ActivityInstance;
import org.example.flowkit.entity.Associates;
import org.example.flowkit.entity.Roles;
import org.example.flowkit.entity.WorkflowInstance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityInstanceRepository extends CrudRepository<ActivityInstance, Long> {

    @Query(value = "select a from activity_instance a join workflow_instance b on b=?1 and a.workflowInstance=b")
    List<ActivityInstance> getActivityInstanceByWorkflowInstance(WorkflowInstance workflowInstance);



}
