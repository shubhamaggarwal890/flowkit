package org.example.flowkit.repository;

import org.example.flowkit.entity.Individual;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualRepository extends CrudRepository<Individual, Long> {
}
