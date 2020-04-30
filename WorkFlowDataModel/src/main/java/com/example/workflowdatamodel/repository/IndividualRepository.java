package com.example.workflowdatamodel.repository;

import com.example.workflowdatamodel.entity.Individual;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualRepository extends CrudRepository<Individual, Long> {
}
