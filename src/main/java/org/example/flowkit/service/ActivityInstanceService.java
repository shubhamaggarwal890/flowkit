package org.example.flowkit.service;

import org.example.flowkit.entity.*;
import org.example.flowkit.repository.ActivityInstanceRepository;
import org.example.flowkit.service.implementation.ActivityInstanceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityInstanceService implements ActivityInstanceServiceImpl {

    private ActivityInstanceRepository activityInstanceRepository;
    private ActivityService activityService;

    public ActivityInstanceService() {
    }

    @Autowired
    public void setActivityInstanceRepository(ActivityInstanceRepository activityInstanceRepository) {
        this.activityInstanceRepository = activityInstanceRepository;
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    public ActivityInstance createActivityInstance(String title, String remark, String status, Activity activity,
                                                   Document document, WorkflowInstance workflowInstance) {
        ActivityInstance activityInstance = new ActivityInstance();
        activityInstance.setTitle(title);
        activityInstance.setRemark(remark);
        activityInstance.setStatus(status);
        activityInstance.setActivity(activity);
        activityInstance.setDocument(document);
        activityInstance.setWorkflowInstance(workflowInstance);
        try {
            activityInstanceRepository.save(activityInstance);
            return activityInstance;
        } catch (DataAccessException error) {
            System.out.println("Error: [createActivityInstance][ActivityInstanceService] " +
                    error.getLocalizedMessage());
        }
        return null;
    }

    public ActivityInstance updateActivityInstancePredecessor(ActivityInstance toBeUpdated,
                                                              ActivityInstance predecessor) {
        toBeUpdated.setSource(predecessor);
        try {
            activityInstanceRepository.save(toBeUpdated);
            return toBeUpdated;
        } catch (DataAccessException error) {
            System.out.println("Error: [updateActivityInstancePredecessor][ActivityInstanceService] " +
                    error.getLocalizedMessage());
        }
        return null;
    }

    public ActivityInstance updateActivityInstanceSuccessor(ActivityInstance toBeUpdated,
                                                            ActivityInstance successor) {
        toBeUpdated.setDestination(successor);
        try {
            activityInstanceRepository.save(toBeUpdated);
            return toBeUpdated;
        } catch (DataAccessException error) {
            System.out.println("Error: [updateActivityInstanceSuccessor][ActivityInstanceService] " +
                    error.getLocalizedMessage());
        }
        return null;
    }

    public List<ActivityInstance> getAllActivitiesInstanceForWorkflowInstance(WorkflowInstance workflowInstance) {
        List<ActivityInstance> activityInstances = activityInstanceRepository.getActivityInstanceByWorkflowInstance(workflowInstance);
        if (activityInstances.isEmpty()) return null;
        return activityInstances;
    }

//    public ActivityInstance getActivityInstanceById(Long activity_instance_id) {
//        Optional<ActivityInstance> activityInstance = activityInstanceRepository.findById(activity_instance_id);
//        return activityInstance.orElse(null);
//    }

//    public List<ActivityInstance> getAllActivitiesInstancesForAssociateForWorkflowInstance(WorkflowInstance workflowInstance) {
//        List<ActivityInstance> activityInstancesForAssociate = new ArrayList<>();
//        List<ActivityInstance> activityInstances = getAllActivitiesInstanceForWorkflowInstance(workflowInstance);
//        if (activityInstances == null) {
//            return null;
//        }
//        ActivityInstance activityInstanceStart = null;
//        for (ActivityInstance activityInstance : activityInstances) {
//            if (activityInstance.getPredecessor() == null) {
//                activityInstanceStart = activityInstance;
//                break;
//            }
//        }
//        if (activityInstanceStart == null) {
//            return null;
//        }
//
//        while (activityInstanceStart.getStatus().equals("ACCEPT")) {
//            activityInstancesForAssociate.add(activityInstanceStart);
//            activityInstanceStart = activityInstanceStart.getSuccessor();
//        }
//        activityInstancesForAssociate.add(activityInstanceStart);
//
//        if (activityInstancesForAssociate.isEmpty()) {
//            return null;
//        }
//        return activityInstancesForAssociate;
//    }

    public ActivityInstance setActivityInstanceStatus(ActivityInstance activityInstance, String status) {
        activityInstance.setStatus(status);
        try {
            activityInstanceRepository.save(activityInstance);
            return activityInstance;
        } catch (DataAccessException error) {
            System.out.println("Error: [setActivityInstanceStatus][ActivityInstanceService] " +
                    error.getLocalizedMessage());
        }
        return null;
    }

    public ActivityInstance updateActivityInstanceStatusBasedRole(ActivityInstance activityInstance) {
        Activity activity = activityInstance.getActivity();
        boolean flag = false;
        List<ActivityAssociates> activityAssociates = activityInstance.getActivityInstanceAssociates();
        if (activity.isAll_any_role()) {
            for (ActivityAssociates activityAssociate : activityAssociates) {
                if (activityAssociate.getStatus().equals("REJECT")) {
                    return setActivityInstanceStatus(activityInstance, "REJECT");
                } else if (activityAssociate.getStatus().equals("ACCEPT")) {
                    activityInstance.setStatus("ACCEPT");
                } else {
                    flag = true;
                    activityInstance.setStatus("PENDING");
                }
            }
        } else {
            for (ActivityAssociates activityAssociate : activityInstance.getActivityInstanceAssociates()) {
                if (activityAssociate.getStatus().equals("REJECT")) {
                    return setActivityInstanceStatus(activityInstance, "REJECT");
                } else if (activityAssociate.getStatus().equals("ACCEPT")) {
                    return setActivityInstanceStatus(activityInstance, "ACCEPT");
                } else {
                    flag = true;
                }
            }
        }
        if (flag) {
            return setActivityInstanceStatus(activityInstance, "PENDING");
        }
        return setActivityInstanceStatus(activityInstance, "ACCEPT");
    }

    public ActivityInstance updateActivityInstanceStatusBasedIndividual(ActivityInstance activityInstance) {
        for (ActivityAssociates activityAssociates : activityInstance.getActivityInstanceAssociates()) {
            return setActivityInstanceStatus(activityInstance, activityAssociates.getStatus());
        }
        return null;
    }

    public ActivityInstance updateActivityInstanceStatusBasedUserSpecific(ActivityInstance activityInstance) {
        for (ActivityAssociates activityAssociates : activityInstance.getActivityInstanceAssociates()) {
            return setActivityInstanceStatus(activityInstance, activityAssociates.getStatus());
        }
        return null;
    }

    public ActivityInstance updateNextActivityInstance(List<ActivityInstance> activityInstances){

        ActivityInstance start = null;
        for (ActivityInstance activityInstance: activityInstances) {
            ActivityInstance predecessor = activityInstance.getSource();
            if (predecessor == null) {
                start = activityInstance;
            }
        }

        while(start!=null) {
            Activity activity = activityService.getActivityByInstanceId(start);
            if(activity.isAuto()) {
                start = setActivityInstanceStatus(start, "ACCEPT");
                if(start == null) {
                    return null;
                }
                start = start.getDestination();
            }else {
                return start;
            }
        }
        return null;
    }
}
