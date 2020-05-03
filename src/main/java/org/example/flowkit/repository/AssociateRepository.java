package org.example.flowkit.repository;

import org.example.flowkit.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssociateRepository extends CrudRepository<Associates, Long> {

    @Query(value = "select a from Associates a WHERE a.emailId = ?1")
    Associates findByEmailId(String emailId);

    @Query(value = "select a from Associates a join Activity b on b.other_associate is NOT NULL and b.other_associate=a AND b=?1")
    Associates findOtherAssociateForActivity(Activity activity);

    @Query(value = "select a from Associates a join Individual b on b.associates is NOT NULL and b.associates = a AND b=?1")
    Associates findAssociateForIndividual(Individual individual);

    @Query(value = "select a from Associates a where a.role is NOT NULL and a.role=?1")
    List<Associates> findAssociateForRole(Roles role);

    @Query(value = "select b from Associates a join Associates b on a.manager is NOT NULL and a.manager=b AND a=?1")
    Associates findAssociateManager(Associates associate);

    @Query(value = "select b from workflow_instance a join Associates b on a=?1 and a.workflowFor=b")
    Associates findAssociateByWorkflowInstances(WorkflowInstance workflowInstance);

    @Query(value = "select b from activity_associates a join Associates b on a=?1 and a.associates=b")
    Associates findAssociateByActivityAssociates(ActivityAssociates activityAssociate);

}
