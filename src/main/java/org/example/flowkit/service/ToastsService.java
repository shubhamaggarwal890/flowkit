package org.example.flowkit.service;

import org.example.flowkit.entity.*;
import org.example.flowkit.repository.ToastsRepository;
import org.example.flowkit.service.implementation.ToastsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToastsService implements ToastsServiceImpl {
    private ToastsRepository toastsRepository;
    private ActivityAssociateService activityAssociateService;
    private WorkflowInstanceService workflowInstanceService;
    private AssociateService associateService;


    public ToastsService() {
    }

    @Autowired
    public void setToastsRepository(ToastsRepository toastsRepository) {
        this.toastsRepository = toastsRepository;
    }

    @Autowired
    public void setActivityAssociateService(ActivityAssociateService activityAssociateService) {
        this.activityAssociateService = activityAssociateService;
    }

    @Autowired
    public void setWorkflowInstanceService(WorkflowInstanceService workflowInstanceService) {
        this.workflowInstanceService = workflowInstanceService;
    }

    @Autowired
    public void setAssociateService(AssociateService associateService) {
        this.associateService = associateService;
    }

    public Toasts addNotificationForAssociate(String message, Associates notify, ActivityInstance activityInstance) {
        Toasts toasts = new Toasts();
        toasts.setMessage(message);
        toasts.setNotify(notify);
        toasts.setNotified(false);
        toasts.setActivityInstance(activityInstance);
        try {
            return toastsRepository.save(toasts);

        } catch (DataAccessException error) {
            System.out.println("Error: [addNotificationForAssociate][NotificationService] " + error.getLocalizedMessage());
        }
        return null;
    }

    public List<Toasts> getNotificationForAssociate(Associates notify) {
        return toastsRepository.getNotificationByAssociate(notify);
    }

    public void dismissNotificationByActivityInstance(ActivityInstance activityInstance) {
        List<ActivityAssociates> activityAssociates = activityAssociateService.getActivityAssociatesByActivityInstance(activityInstance);
        for (ActivityAssociates act_associates : activityAssociates) {
            Associates associates = associateService.findAssociateByActivityAssociate(act_associates);
            Toasts toast = toastsRepository.getNotificationByActivityInstanceAndNotifier(activityInstance, associates);
            if (toast == null) {
                return;
            }
            toast.setNotified(true);
            try {
                toastsRepository.save(toast);
            } catch (DataAccessException error) {
                System.out.println("Error: [dismissNotificationByActivityInstance][NotificationService] " +
                        error.getLocalizedMessage());
            }
        }
    }

    public void dismissNotificationByActivityInstanceAndAssociate(ActivityInstance activityInstance,
                                                                  Associates associate) {
        Toasts toast = toastsRepository.getNotificationByActivityInstanceAndNotifier(activityInstance, associate);
        if (toast == null) {
            return;
        }
        toast.setNotified(true);
        try {
            toastsRepository.save(toast);
        } catch (DataAccessException error) {
            System.out.println("Error: [dismissNotificationByActivityInstance][NotificationService] " +
                    error.getLocalizedMessage());
        }
    }


    public void dismissNotificationByActivityInstances(List<ActivityInstance> activityInstances) {
        for (ActivityInstance activityInstance : activityInstances) {
            List<Toasts> toasts = toastsRepository.getNotificationByActivityInstance(activityInstance);
            for (Toasts toast : toasts) {
                toast.setNotified(true);
                try {
                    toastsRepository.save(toast);
                } catch (DataAccessException error) {
                    System.out.println("Error: [dismissNotificationByActivityInstance][NotificationService] " +
                            error.getLocalizedMessage());
                }
            }
        }
    }

    public Toasts dismissNotification(Long notification_id) {
        Toasts toasts = toastsRepository.findById(notification_id).orElse(null);
        if (toasts == null) {
            return null;
        }
        toasts.setNotified(true);
        try {
            return toastsRepository.save(toasts);
        } catch (DataAccessException error) {
            System.out.println("Error: [dismissNotification][NotificationService] " + error.getLocalizedMessage());
        }
        return null;
    }

    public void setToastsForAssociate(ActivityInstance current, ActivityInstance next) {
        if (current != null) {
            dismissNotificationByActivityInstance(current);
        }
        if (next == null || next.getDestination() == null) {
            return;
        }
        WorkflowInstance workflowInstance = workflowInstanceService.getWorkflowInstanceByActivityInstance(next);
        if (workflowInstance == null) {
            System.out.println("Error: [setToastsForAssociate][NotificationService] could not find workflow instance " +
                    "for next activity instance");
            return;
        }
        Associates customer = associateService.findAssociateByWorkflowInstance(workflowInstance);
        if (customer == null) {
            System.out.println("Error: [setToastsForAssociate][NotificationService] could not find customer associate " +
                    "for next activity instance");
            return;
        }
        List<ActivityAssociates> activityAssociates = activityAssociateService.getActivityAssociatesByActivityInstance(
                next);
        String message = "Hey, You have a pending request from " + customer.getFirstName() + " " + customer.getLastName();
        Toasts toast = null;
        for (ActivityAssociates act_as : activityAssociates) {
            Associates notify = associateService.findAssociateByActivityAssociate(act_as);
            if (notify == null) {
                System.out.println("Error: [setToastsForAssociate][NotificationService] could not find notifier " +
                        "for next activity instance");
                return;
            }
            toast = addNotificationForAssociate(message, notify, next);
        }
    }

    public void setToastsForAllRoleAssociate(ActivityInstance current, ActivityInstance next) {
        if (current != null) {
            dismissNotificationByActivityInstance(current);
        }
        if (next == null || next.getDestination() == null) {
            return;
        }
        List<Toasts> toasts = toastsRepository.getNotificationByActivityInstance(next);
        if (!toasts.isEmpty()) {
            return;
        }
        WorkflowInstance workflowInstance = workflowInstanceService.getWorkflowInstanceByActivityInstance(next);
        if (workflowInstance == null) {
            System.out.println("Error: [setToastsForAssociate][NotificationService] could not find workflow instance " +
                    "for next activity instance");
            return;
        }
        Associates customer = associateService.findAssociateByWorkflowInstance(workflowInstance);
        if (customer == null) {
            System.out.println("Error: [setToastsForAssociate][NotificationService] could not find customer associate " +
                    "for next activity instance");
            return;
        }
        List<ActivityAssociates> activityAssociates = activityAssociateService.getActivityAssociatesPendingByActivityInstance(
                next);
        String message = "Hey, You have a pending request from " + customer.getFirstName() + " " + customer.getLastName();
        Toasts toast = null;
        for (ActivityAssociates act_as : activityAssociates) {
            Associates notify = associateService.findAssociateByActivityAssociate(act_as);
            if (notify == null) {
                System.out.println("Error: [setToastsForAssociate][NotificationService] could not find notifier " +
                        "for next activity instance");
                return;
            }
            toast = addNotificationForAssociate(message, notify, next);
        }
    }

}
