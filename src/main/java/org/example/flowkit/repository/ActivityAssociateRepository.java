package org.example.flowkit.repository;

import org.example.flowkit.entity.ActivityAssociates;
import org.example.flowkit.entity.ActivityInstance;
import org.example.flowkit.entity.WorkflowInstance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ActivityAssociateRepository extends CrudRepository<ActivityAssociates, Long> {

    @Query(value = "select a from activity_associates a join activity_instance b on b=?1 and " +
            "a.activity_instance_associate=b")
    List<ActivityAssociates> getActivityAssociatesByActivityInstance(ActivityInstance activityInstance);

    @Query(value = "select a from activity_associates a join activity_instance b on b=?1 and " +
            "a.activity_instance_associate=b and a.status='PENDING'")
    List<ActivityAssociates> getActivityAssociatesPendingByActivityInstance(ActivityInstance activityInstance);


}
