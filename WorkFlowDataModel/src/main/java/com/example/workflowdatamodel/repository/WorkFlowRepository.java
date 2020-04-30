package com.example.workflowdatamodel.repository;

import com.example.workflowdatamodel.entity.Associates;
import com.example.workflowdatamodel.entity.Workflow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkFlowRepository extends CrudRepository<Workflow, Long> {

}
