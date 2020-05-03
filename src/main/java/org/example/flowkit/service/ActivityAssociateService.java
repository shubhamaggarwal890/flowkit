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
    public void SetActivityAssociateService(ActivityAssociateRepository activityAssociateRepository) {
        this.activityAssociateRepository = activityAssociateRepository;
    }

    @Autowired
    public void setAssociateService(AssociateService associateService) {
        this.associateService = associateService;
    }

    public ActivityAssociates saveActivityInstanceAssociateStatus(ActivityAssociates activityAssociates,
                                                                  String status) {
        activityAssociates.setStatus(status);
        try {
            activityAssociateRepository.save(activityAssociates);
            return activityAssociates;
        } catch (DataAccessException error) {
            System.out.println("ERROR: [saveActivityInstanceAssociateStatus][ActivityAssociateService] " +
                    error.getLocalizedMessage());
        }
        return null;
    }

    // Role, individual and user specified based status update in activity_associate
    public ActivityAssociates updateActivityInstanceAssociateBasedRole(ActivityInstance activityInstance,
                                                                       ActivityAssociates activityAssociate,
                                                                       String remark, String status) {
        Activity activity = activityInstance.getActivity();
        List<ActivityAssociates> activityAssociates = activityInstance.getActivityInstanceAssociates();
        ActivityAssociates returnActivityAssociate = null;
        if (activity.isAll_any_role()) {
            if (status.equals("REJECT")) {
                for (ActivityAssociates associate : activityAssociates) {
                    if (activityAssociate.equals(associate)) {
                        associate.setRemark(remark);
                        returnActivityAssociate = saveActivityInstanceAssociateStatus(associate, "REJECT");
                    } else {
                        associate.setRemark(null);
                        saveActivityInstanceAssociateStatus(associate, "REJECT");
                    }

                }
            } else {
                for (ActivityAssociates associate : activityAssociates) {
                    if (activityAssociate.equals(associate)) {
                        associate.setRemark(remark);
                        returnActivityAssociate = saveActivityInstanceAssociateStatus(associate, "APPROVE");
                        break;
                    }
                }
            }
        } else {
            for (ActivityAssociates associate : activityAssociates) {
                if (activityAssociate.equals(associate)) {
                    associate.setRemark(remark);
                    returnActivityAssociate = saveActivityInstanceAssociateStatus(associate, status);
                } else {
                    associate.setRemark(null);
                    saveActivityInstanceAssociateStatus(associate, status);
                }
            }
        }
        return returnActivityAssociate;
    }

    public ActivityAssociates updateActivityInstanceAssociateBasedIndividual(ActivityInstance activityInstance,
                                                                             ActivityAssociates activityAssociate,
                                                                             String remark, String status) {
        List<ActivityAssociates> activityAssociates = activityInstance.getActivityInstanceAssociates();
        ActivityAssociates returnActivityAssociate = null;
        for (ActivityAssociates associate : activityAssociates) {
            if (activityAssociate.equals(associate)) {
                associate.setRemark(remark);
                returnActivityAssociate = saveActivityInstanceAssociateStatus(associate, status);
            } else {
                associate.setRemark(null);
                saveActivityInstanceAssociateStatus(associate, status);
            }
        }
        return returnActivityAssociate;
    }

    public ActivityAssociates updateActivityInstanceAssociateBasedUser(ActivityInstance activityInstance,
                                                                       ActivityAssociates activityAssociate,
                                                                       String remark, String status) {
        return updateActivityInstanceAssociateBasedIndividual(activityInstance, activityAssociate, remark, status);
    }


    public ActivityAssociates getActivityAssociateById(Long id) {
        return activityAssociateRepository.findById(id).orElse(null);
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
            activityAssociate.setAlert(false);
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
                    System.out.println("Error: [addActivityInstanceAssociatesBasedIndividual] [ActivityAssociateService] couldn't find an associate to it");
                    return null;
                } else {
                    manager = associateService.getAssociateByEmailID(other);

                    if(manager == null){
                        System.out.println("Error: [addActivityInstanceAssociatesBasedIndividual] [ActivityAssociateService] couldn't find manager to it");
                    }
                    individualAssociate = manager;
                }
            }else{
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

    public List<ActivityAssociates> getActivityAssociatesByActivityInstance(ActivityInstance activityInstance){
        return activityAssociateRepository.getActivityAssociatesByActivityInstance(activityInstance);
    }

}
