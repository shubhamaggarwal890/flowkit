package org.example.flowkit.repository;

import org.example.flowkit.entity.Activity;
import org.example.flowkit.entity.Associates;
import org.example.flowkit.entity.Individual;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociateRepository extends CrudRepository<Associates, Long> {

    @Query(value = "select a from Associates a WHERE a.emailId = ?1")
    Associates findByEmailId(String emailId);

    @Query(value = "select a from Associates a join Activity b on b.other_associate=a AND b=?1")
    Associates findOtherAssociateForActivity(Activity activity);

    @Query(value = "select a from Associates a join Individual b on b.associates=a AND b=?1")
    Associates findAssociateForIndividual(Individual individual);

    @Query(value = "select b from Associates a join Associates b on a.manager=b AND a=?1")
    Associates findAssociateManager(Associates associate);
}
