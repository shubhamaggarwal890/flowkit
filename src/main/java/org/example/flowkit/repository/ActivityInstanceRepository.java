package org.example.flowkit.repository;

import org.example.flowkit.entity.ActivityInstance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityInstanceRepository extends CrudRepository<ActivityInstance, Long> {
}
