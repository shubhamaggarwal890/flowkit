package org.example.flowkit.repository;

import org.example.flowkit.entity.Workflow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkFlowRepository extends CrudRepository<Workflow, Long> {

}
