package com.example.workflowdatamodel.repository;

import com.example.workflowdatamodel.entity.ActivityInstance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityInstanceRepository extends CrudRepository<ActivityInstance, Long> {
}
