package org.example.flowkit.repository;

import org.example.flowkit.entity.Activity;
import org.example.flowkit.entity.ActivityInstance;
import org.example.flowkit.entity.Workflow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {

    @Query(value = "select a from Activity a WHERE a.workflow = ?1")
    List<Activity> findActivitiesByWorkflow(Workflow workflow);

    @Query(value = "select b from activity_instance a join Activity b on a.activity = b AND a=?1")
    Activity findActivitiesByInstance(ActivityInstance activityInstance);


}
