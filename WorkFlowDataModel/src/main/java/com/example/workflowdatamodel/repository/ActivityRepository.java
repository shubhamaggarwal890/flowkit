package com.example.workflowdatamodel.repository;

import com.example.workflowdatamodel.entity.Activity;
import com.example.workflowdatamodel.entity.Associates;
import com.example.workflowdatamodel.entity.Workflow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {

    @Query(value = "select a from Activity a WHERE a.workflow = ?1")
    List<Activity> findActivitiesByWorkflow(Workflow workflow);

}
