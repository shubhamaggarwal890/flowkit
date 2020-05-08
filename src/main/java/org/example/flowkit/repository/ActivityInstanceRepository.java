package org.example.flowkit.repository;

import org.example.flowkit.entity.ActivityInstance;
import org.example.flowkit.entity.Associates;
import org.example.flowkit.entity.Roles;
import org.example.flowkit.entity.WorkflowInstance;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ActivityInstanceRepository extends CrudRepository<ActivityInstance, Long> {

    @Query(value = "select a from activity_instance a join workflow_instance b on b=?1 and a.workflowInstance=b")
    List<ActivityInstance> getActivityInstanceByWorkflowInstance(WorkflowInstance workflowInstance);

    @Query(value = "select b from activity_associates a join activity_instance b on a.associates=?1 and a.status=?2 and a.activity_instance_associate=b")
    List<ActivityInstance> getActivityInstanceByActivityAssociate(Associates a, String status);

    @Transactional
    @Modifying
    @Query(value = "update activity_instance a set a.status=?2 where a=?1")
    void updateActivityInstance(ActivityInstance a, String status);


}
