package org.example.flowkit.service;

import org.example.flowkit.entity.*;
import org.example.flowkit.repository.ActivityInstanceRepository;
import org.example.flowkit.repository.WorkflowInstanceRepository;
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
    private ActivityAssociateService activityAssociateService;
    private ActivityService activityService;
    private AssociateService associateService;
    private ToastsService toastService;
    private WorkflowInstanceRepository workflowInstanceRepository;

    public ActivityInstanceService() {
    }

    @Autowired
    public void setAssociateService(AssociateService associateService) {
        this.associateService = associateService;
    }

    @Autowired
    public void setActivityInstanceRepository(ActivityInstanceRepository activityInstanceRepository) {
        this.activityInstanceRepository = activityInstanceRepository;
    }

    @Autowired
    public void setActivityAssociateService(ActivityAssociateService activityAssociateService) {
        this.activityAssociateService = activityAssociateService;
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setToastService(ToastsService toastService) {
        this.toastService = toastService;
    }


    @Autowired
    public void setWorkflowInstanceRepository(WorkflowInstanceRepository workflowInstanceRepository) {
        this.workflowInstanceRepository = workflowInstanceRepository;
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
        List<ActivityInstance> activityInstances = new ArrayList<>();
        ActivityInstance start = null;
        for (ActivityInstance activityInstance : activityInstanceRepository.getActivityInstanceByWorkflowInstance(workflowInstance)) {
            ActivityInstance predecessor = activityInstance.getSource();
            if (predecessor == null) {
                start = activityInstance;
            }
        }
        while (start != null) {
            activityInstances.add(start);
            start = start.getDestination();
        }
        if (activityInstances.isEmpty()) return null;
        return activityInstances;
    }

    public List<ActivityInstance> getAllActivitiesInstanceAssociateForWorkflowInstance(
            WorkflowInstance workflowInstance) {

        List<ActivityInstance> activityInstances = activityInstanceRepository.getActivityInstanceByWorkflowInstance(workflowInstance);
        if (activityInstances.isEmpty()) return null;
        ActivityInstance start = null;
        List<ActivityInstance> activityInstanceList = new ArrayList<>();
        for (ActivityInstance activityInstance : activityInstances) {
            ActivityInstance predecessor = activityInstance.getSource();
            if (predecessor == null) {
                start = activityInstance;
            }
        }
        while (start != null) {
            if (start.getStatus().equals("ACCEPT")) {
                activityInstanceList.add(start);
            } else {
                start.setDestination(null);
                activityInstanceList.add(start);
                return activityInstanceList;
            }
            start = start.getDestination();
        }
        return null;
    }

    public ActivityInstance getActivityInstanceById(Long activity_instance_id) {
        Optional<ActivityInstance> activityInstance = activityInstanceRepository.findById(activity_instance_id);
        return activityInstance.orElse(null);
    }

    public ActivityInstance saveAssociateResponseActivity(ActivityInstance activityInstance, Associates associate,
                                                          String status, String remark, boolean all_any) {
        List<ActivityAssociates> activityAssociates = activityAssociateService.getActivityAssociatesByActivityInstance(
                activityInstance);
        if (activityAssociates == null || activityAssociates.isEmpty()) {
            return null;
        }
        WorkflowInstance workflowInstance = workflowInstanceRepository.getWorkflowInstanceByActivityInstances(
                activityInstance);
        if (workflowInstance == null) {
            System.out.println("Error: [saveAssociateResponseActivity][ActivityInstanceService] no workflow instance " +
                    "for given activity instance found");
            return null;
        }
        Associates customer = associateService.findAssociateByWorkflowInstance(workflowInstance);
        if (customer == null) {
            System.out.println("Error: [saveAssociateResponseActivity][ActivityInstanceService] no customer for " +
                    "workflow instance found");
            return null;
        }

        if (all_any) {
            if (status.equals("REJECT")) {
                for (ActivityAssociates activityAssociate : activityAssociates) {
                    Associates associates = associateService.findAssociateByActivityAssociate(activityAssociate);
                    if (associates.getId().equals(associate.getId())) {
                        activityAssociateService.updateStatusRemarkActivityAssociate(activityAssociate, status, remark);
                        String message = "Hey, Your " + workflowInstance.getTitle() + " is rejected by " +
                                associate.getFirstName() + " " + associate.getLastName();
                        toastService.addNotificationForAssociate(message, customer, activityInstance);

                    } else {
                        activityAssociateService.updateStatusRemarkActivityAssociate(activityAssociate, status, null);
                    }
                }
                return setActivityInstanceStatus(activityInstance, status);
            }
            int count = 0;
            for (ActivityAssociates activityAssociate : activityAssociates) {
                Associates associates = associateService.findAssociateByActivityAssociate(activityAssociate);
                if (associates.getId().equals(associate.getId())) {
                    count += 1;
                    activityAssociateService.updateStatusRemarkActivityAssociate(activityAssociate, status, remark);
                    String message = "Hey, " + associate.getFirstName() + " " + associate.getLastName()
                            + " has approved your " + activityInstance.getTitle() + " in " + workflowInstance.getTitle();
                    toastService.addNotificationForAssociate(message, customer, activityInstance);
                } else if (activityAssociate.getStatus().equals("ACCEPT")) {
                    count += 1;
                }
            }
            if (activityAssociates.size() == count) {
                String message = "Hey, Your " + activityInstance.getTitle() + " in " + workflowInstance.getTitle()
                        + " is approved by all the associates";
                toastService.addNotificationForAssociate(message, customer, activityInstance);
                return setActivityInstanceStatus(activityInstance, status);
            }else{
                toastService.dismissNotificationByActivityInstanceAndAssociate(activityInstance, associate);
            }
            return activityInstance;
        } else {
            for (ActivityAssociates activityAssociate : activityAssociates) {
                Associates associates = associateService.findAssociateByActivityAssociate(activityAssociate);
                if (associates.getId().equals(associate.getId())) {
                    activityAssociateService.updateStatusRemarkActivityAssociate(activityAssociate, status, remark);
                    String message = "Hey, Your " + activityInstance.getTitle() + " in " + workflowInstance.getTitle()
                            + " is " + status.toLowerCase() + "ed by " + associate.getFirstName() + " " +
                            associate.getLastName();
                    toastService.addNotificationForAssociate(message, customer, activityInstance);

                } else {
                    activityAssociateService.updateStatusRemarkActivityAssociate(activityAssociate, status, null);
                }
            }
            return setActivityInstanceStatus(activityInstance, status);
        }
    }


    public ActivityInstance setActivityInstanceStatus(ActivityInstance activityInstance, String status) {
        try {
            activityInstanceRepository.updateActivityInstance(activityInstance, status);
            return activityInstance;
        } catch (DataAccessException error) {
            System.out.println("Error: [setActivityInstanceStatus][ActivityInstanceService] " +
                    error.getLocalizedMessage());
        }
        return null;
    }

    public ActivityInstance updateActivityInstancesIfAuto(List<ActivityInstance> activityInstances) {
        ActivityInstance start = null;
        ActivityInstance previous = null;
        for (ActivityInstance activityInstance : activityInstances) {
            ActivityInstance predecessor = activityInstance.getSource();
            if (predecessor == null) {
                start = activityInstance;
            }
        }
        while (start != null) {
            Activity activity = activityService.getActivityByInstanceId(start);
            if (activity == null) {
                System.out.println("Error: [updateActivityInstances][ActivityInstanceService] activity not found");
                return null;
            }
            if (activity.isAuto() && start.getStatus().equals("PENDING") &&
                    (previous == null || previous.getStatus().equals("ACCEPT"))) {
                start = setActivityInstanceStatus(start, "ACCEPT");
                toastService.setToastsForAssociate(previous,  null);
                if (start == null) {
                    System.out.println("Error: [updateActivityInstances][ActivityInstanceService] failed to update " +
                            "activity instance for auto");
                    return null;
                }else{
                    start.setStatus("ACCEPT");
                }
            } else if (activity.isAll_any_role() && start.getStatus().equals("PENDING") &&
                    (previous == null || previous.getStatus().equals("ACCEPT"))) {
                toastService.setToastsForAllRoleAssociate(previous, start);
                return start;
            } else if (start.getStatus().equals("PENDING") &&
                    (previous == null || previous.getStatus().equals("ACCEPT"))) {
                System.out.println("When current is pending and previous is null and has status accept");
                toastService.setToastsForAssociate(previous, start);
                return start;
            } else if (start.getStatus().equals("PENDING") && (previous == null ||
                    previous.getStatus().equals("REJECT"))) {
                toastService.setToastsForAssociate(previous, null);
                return start;
            }
            previous = start;
            start = start.getDestination();
            if (start == null) {
                WorkflowInstance workflowInstance = workflowInstanceRepository.getWorkflowInstanceByActivityInstances(
                        previous);
                if (workflowInstance == null) {
                    System.out.println("Error: [updateActivityInstancesIfAuto][ActivityInstanceService] no " +
                            "workflow instance for given activity instance found");
                    return null;
                } else {
                    Associates customer = associateService.findAssociateByWorkflowInstance(workflowInstance);
                    if (customer == null) {
                        System.out.println("Error: [updateActivityInstancesIfAuto][ActivityInstanceService] no " +
                                "customer for workflow instance found");
                        return null;
                    }
                    String message = "Hey, Your " + workflowInstance.getTitle()
                            + " is approved by all the associates";
                    toastService.addNotificationForAssociate(message, customer, previous);
                }
            }
        }
        return previous;
    }
}
