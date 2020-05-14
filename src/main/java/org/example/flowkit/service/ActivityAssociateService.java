package org.example.flowkit.service;

import org.example.flowkit.entity.*;
import org.example.flowkit.jsonobject.ActivityInstanceRequest;
import org.example.flowkit.repository.ActivityAssociateRepository;
import org.example.flowkit.service.implementation.ActivityAssociateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityAssociateService implements ActivityAssociateServiceImpl {

    private ActivityAssociateRepository activityAssociateRepository;
    private AssociateService associateService;

    public ActivityAssociateService() {
    }

    @Autowired
    public void setAssociateService(AssociateService associateService) {
        this.associateService = associateService;
    }

    @Autowired
    public void setActivityAssociateRepository(ActivityAssociateRepository activityAssociateRepository) {
        this.activityAssociateRepository = activityAssociateRepository;
    }

    public List<ActivityAssociates> addActivityInstanceAssociatesBasedRole(ActivityInstance activityInstance,
                                                                           Roles role) {
        List<Associates> associates = associateService.findAssociateBasedRole(role);
        if (associates == null) {
            return null;
        }
        List<ActivityAssociates> activityAssociates = new ArrayList<>();
        for (Associates associate : associates) {
            ActivityAssociates activityAssociate = new ActivityAssociates();
            activityAssociate.setActivity_instance_associate(activityInstance);
            activityAssociate.setAssociates(associate);
            activityAssociate.setStatus("PENDING");
                try {
                activityAssociateRepository.save(activityAssociate);
                activityAssociates.add(activityAssociate);
            } catch (DataAccessException error) {
                System.out.println("ERROR: [addActivityInstanceAssociatesBasedRole][ActivityAssociateService] " +
                        error.getLocalizedMessage());
                return null;
            }
        }
        return activityAssociates;
    }

    public ActivityAssociates addActivityInstanceAssociatesBasedIndividual(ActivityInstance activityInstance,
                                                                           Individual individual, Associates creator,
                                                                           ActivityInstanceRequest activity) {
        Associates individualAssociate = associateService.findAssociateBasedIndividual(individual);
        Associates manager = null;
        if (individualAssociate == null) {
            manager = associateService.findAssociateBasedIndividualVertical(creator, individual.getVertical());
            if (manager == null) {
                String other = activity.getAssociate();
                System.out.println(other);
                if (other == null) {
                    System.out.println("Error: [addActivityInstanceAssociatesBasedIndividual] " +
                            "[ActivityAssociateService] couldn't find an associate to it");
                    return null;
                } else {
                    manager = associateService.getAssociateByEmailID(other);

                    if (manager == null) {
                        System.out.println("Error: [addActivityInstanceAssociatesBasedIndividual] " +
                                "[ActivityAssociateService] couldn't find manager to it");
                    }
                    individualAssociate = manager;
                }
            } else {
                individualAssociate = manager;
            }
        }

        ActivityAssociates activityAssociates = new ActivityAssociates();
        activityAssociates.setStatus("PENDING");
        activityAssociates.setAssociates(individualAssociate);
        activityAssociates.setActivity_instance_associate(activityInstance);
        try {
            activityAssociateRepository.save(activityAssociates);
            return activityAssociates;
        } catch (DataAccessException error) {
            System.out.println("ERROR: [addActivityInstanceAssociatesBasedIndividual][ActivityAssociateService] " +
                    error.getLocalizedMessage());
            return null;
        }
    }

    public ActivityAssociates addActivityInstanceAssociatesBasedUser(ActivityInstance activityInstance,
                                                                     Associates associate) {
        ActivityAssociates activityAssociates = new ActivityAssociates();
        activityAssociates.setStatus("PENDING");
        activityAssociates.setAssociates(associate);
        activityAssociates.setActivity_instance_associate(activityInstance);
        try {
            activityAssociateRepository.save(activityAssociates);
            return activityAssociates;
        } catch (DataAccessException error) {
            System.out.println("ERROR: [addActivityInstanceAssociatesBasedUser][ActivityAssociateService] " +
                    error.getLocalizedMessage());
            return null;
        }
    }

    public List<ActivityAssociates> getActivityAssociatesByActivityInstance(ActivityInstance activityInstance) {
        return activityAssociateRepository.getActivityAssociatesByActivityInstance(activityInstance);
    }

    public List<ActivityAssociates> getActivityAssociatesPendingByActivityInstance(ActivityInstance activityInstance) {
        return activityAssociateRepository.getActivityAssociatesPendingByActivityInstance(activityInstance);
    }


    public ActivityAssociates updateStatusRemarkActivityAssociate(ActivityAssociates activityAssociates, String status,
                                                                  String remark) {
        if(remark == null){
            if(activityAssociates.getRemark() == null){
                activityAssociates.setRemark(null);
            }
        }else{
            activityAssociates.setRemark(remark);
        }
        if(activityAssociates.getStatus().equals("PENDING")){
            activityAssociates.setStatus(status);
        }
        try {
            activityAssociateRepository.save(activityAssociates);
            return activityAssociates;
        } catch (DataAccessException error) {
            System.out.println("ERROR: [updateStatusRemarkActivityAssociate][ActivityAssociateService] " +
                    error.getLocalizedMessage());
            return null;
        }
    }

}
