package org.example.flowkit.controller;

import org.example.flowkit.entity.*;
import org.example.flowkit.jsonobject.ActivityAssociateRequest;
import org.example.flowkit.jsonobject.ActivityAssociatesResponse;
import org.example.flowkit.jsonobject.ActivityInstanceResponse;
import org.example.flowkit.jsonobject.DocumentRequest;
import org.example.flowkit.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ActivityInstanceController {

    private WorkflowInstanceService workflowInstanceService;
    private ActivityInstanceService activityInstanceService;
    private AssociateService associateService;
    private ActivityAssociateService activityAssociateService;
    private ActivityService activityService;
    private ToastsService toastsService;

    @Autowired
    public void setActivityAssociateService(ActivityAssociateService activityAssociateService) {
        this.activityAssociateService = activityAssociateService;
    }

    @Autowired
    public void setWorkflowInstanceService(WorkflowInstanceService workflowInstanceService) {
        this.workflowInstanceService = workflowInstanceService;
    }

    @Autowired
    public void setActivityInstanceService(ActivityInstanceService activityInstanceService) {
        this.activityInstanceService = activityInstanceService;
    }

    @Autowired
    public void setAssociateService(AssociateService associateService) {
        this.associateService = associateService;
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setToastsService(ToastsService toastsService) {
        this.toastsService = toastsService;
    }

    @PostMapping("/get_workflow_instance_activity")
    public List<ActivityInstanceResponse> getAllActivitiesInstanceWorkflowInstance(@RequestBody WorkflowInstance workflowInstance) {
        WorkflowInstance workflowInstance1 = workflowInstanceService.getWorkflowInstanceById(workflowInstance.getId());

        if (workflowInstance1 == null) {
            System.out.println("Error: [getAllActivitiesInstanceWorkflowInstance] [ActivityInstanceController] " +
                    "Workflow instance not found");
            return null;
        }

        List<ActivityInstance> activityInstances = activityInstanceService.getAllActivitiesInstanceForWorkflowInstance(workflowInstance1);

        if (activityInstances == null) {
            System.out.println("Error: [getAllActivitiesInstanceWorkflowInstance] [ActivityInstanceController] " +
                    "Activity Instances not found");
            return null;
        }
        boolean flag = false;
        int count = 0;

        List<ActivityInstanceResponse> activityInstanceResponses = new ArrayList<>();
        for (ActivityInstance activityInstance : activityInstances) {
            Activity activity = activityService.getActivityByInstanceId(activityInstance);
            ActivityInstanceResponse activityInstanceResponse = new ActivityInstanceResponse();
            activityInstanceResponse.setId(String.valueOf(activityInstance.getId()));
            activityInstanceResponse.setTitle(activityInstance.getTitle());
            activityInstanceResponse.setDescription(activityInstance.getRemark());
            activityInstanceResponse.setOffsetX(activity.getOffsetX());
            activityInstanceResponse.setOffsetY(activity.getOffsetY());
            activityInstanceResponse.setShape(activity.getShape());
            ActivityInstance predecessor = activityInstance.getSource();
            if (predecessor == null) {
                activityInstanceResponse.setPredecessor(null);
            } else {
                activityInstanceResponse.setPredecessor(String.valueOf(predecessor.getId()));
            }

            ActivityInstance successor = activityInstance.getDestination();
            if (successor == null) {
                activityInstanceResponse.setSuccessor(null);
            } else {
                activityInstanceResponse.setSuccessor(String.valueOf(successor.getId()));
            }
            activityInstanceResponse.setAuto(activity.isAuto());
            activityInstanceResponse.setStatus(activityInstance.getStatus());
            activityInstanceResponse.setAny_all(activity.isAll_any_role());
            DocumentRequest documentRequest = new DocumentRequest();
            Document document = activityInstance.getDocument();
            if (document != null) {
                documentRequest.setId(document.getId());
                activityInstanceResponse.setDocument(documentRequest);
            } else {
                activityInstanceResponse.setDocument(null);
            }

            List<ActivityAssociates> activityAssociates = activityAssociateService.getActivityAssociatesByActivityInstance(activityInstance);
            List<ActivityAssociatesResponse> activityAssociatesResponses = new ArrayList<>();
            if (activityAssociates == null || activityAssociates.isEmpty()) {
                activityInstanceResponse.setAssociates(null);
            } else {
                for (ActivityAssociates activityAssociate : activityAssociates) {
                    ActivityAssociatesResponse activityAssociatesResponse = new ActivityAssociatesResponse();
                    Associates associate = associateService.findAssociateByActivityAssociate(activityAssociate);
                    if (associate == null) {
                        System.out.println("Error: [getAllActivitiesInstanceWorkflowInstance] [ActivityInstanceController] " +
                                "associate not found for " + activityAssociate.getId());
                        return null;
                    }
                    activityAssociatesResponse.setName(associate.getFirstName() + " " + associate.getLastName());
                    activityAssociatesResponse.setEmail(associate.getEmailId());
                    activityAssociatesResponse.setRemark(activityAssociate.getRemark());
                    activityAssociatesResponse.setStatus(activityAssociate.getStatus());
                    activityAssociatesResponses.add(activityAssociatesResponse);
                }
                activityInstanceResponse.setAssociates(activityAssociatesResponses);
            }
            activityInstanceResponses.add(activityInstanceResponse);
            if(activityInstanceResponse.getStatus().equals("REJECT")){
                flag = true;
            }else if(activityInstanceResponse.getStatus().equals("ACCEPT")){
                count++;
            }
        }
        if(flag || count==activityInstanceResponses.size()){
            toastsService.dismissNotificationByActivityInstances(activityInstances);
        }

        return activityInstanceResponses;
    }

    @PostMapping("/get_workflow_instance_associate_activity")
    public List<ActivityInstanceResponse> getAllActivitiesInstanceWorkflowInstanceBasedAssociate(
            @RequestBody WorkflowInstance workflowInstance) {
        WorkflowInstance workflowInstance1 = workflowInstanceService.getWorkflowInstanceById(workflowInstance.getId());

        if (workflowInstance1 == null) {
            System.out.println("Error: [getAllActivitiesInstanceWorkflowInstanceBasedAssociate] " +
                    "[ActivityInstanceController] Workflow instance not found");
            return null;
        }

        List<ActivityInstance> activityInstances = activityInstanceService.getAllActivitiesInstanceAssociateForWorkflowInstance(
                workflowInstance1);

        if (activityInstances == null) {
            System.out.println("Error: [getAllActivitiesInstanceWorkflowInstanceBasedAssociate] " +
                    "[ActivityInstanceController] Activity Instances not found");
            return null;
        }

        List<ActivityInstanceResponse> activityInstanceResponses = new ArrayList<>();
        for (ActivityInstance activityInstance : activityInstances) {
            Activity activity = activityService.getActivityByInstanceId(activityInstance);
            ActivityInstanceResponse activityInstanceResponse = new ActivityInstanceResponse();
            activityInstanceResponse.setId(String.valueOf(activityInstance.getId()));
            activityInstanceResponse.setTitle(activityInstance.getTitle());
            activityInstanceResponse.setDescription(activityInstance.getRemark());
            activityInstanceResponse.setOffsetX(activity.getOffsetX());
            activityInstanceResponse.setOffsetY(activity.getOffsetY());
            activityInstanceResponse.setShape(activity.getShape());
            ActivityInstance predecessor = activityInstance.getSource();
            if (predecessor == null) {
                activityInstanceResponse.setPredecessor(null);
            } else {
                activityInstanceResponse.setPredecessor(String.valueOf(predecessor.getId()));
            }

            ActivityInstance successor = activityInstance.getDestination();
            if (successor == null) {
                activityInstanceResponse.setSuccessor(null);
            } else {
                activityInstanceResponse.setSuccessor(String.valueOf(successor.getId()));
            }
            activityInstanceResponse.setAuto(activity.isAuto());
            activityInstanceResponse.setStatus(activityInstance.getStatus());
            activityInstanceResponse.setAny_all(activity.isAll_any_role());
            DocumentRequest documentRequest = new DocumentRequest();
            Document document = activityInstance.getDocument();
            if (document != null) {
                documentRequest.setId(document.getId());
                activityInstanceResponse.setDocument(documentRequest);
            } else {
                activityInstanceResponse.setDocument(null);
            }
            activityInstanceResponses.add(activityInstanceResponse);
        }

        return activityInstanceResponses;
    }

    @PostMapping("/save_associate_response")
    public String saveAssociateResponseActivityInstance(
            @RequestBody ActivityAssociateRequest activityAssociateRequest) {

        ActivityInstance activityInstance = activityInstanceService.getActivityInstanceById(
                activityAssociateRequest.getActivity_instance());

        if (activityInstance == null) {
            System.out.println("Error: [saveAssociateResponseActivityInstance] " +
                    "[ActivityInstanceController] Activity Instances not found");
            return null;
        }

        Associates associate = associateService.getAssociateById(activityAssociateRequest.getAssociate());
        if (associate == null) {
            System.out.println("Error: [saveAssociateResponseActivityInstance] " +
                    "[ActivityInstanceController] Associate not found");
            return null;
        }

        ActivityInstance activityInstances = activityInstanceService.saveAssociateResponseActivity(activityInstance,
                associate, activityAssociateRequest.getStatus(), activityAssociateRequest.getRemark(),
                activityAssociateRequest.getAny_all());
        if (activityInstances == null) {
            System.out.println("Error: [saveAssociateResponseActivityInstance] " +
                    "[ActivityInstanceController] Couldn't update activity instance associate");
            return null;
        }

        WorkflowInstance workflowInstance = workflowInstanceService.getWorkflowInstanceById(
                activityAssociateRequest.getWorkflow());
        if (workflowInstance == null) {
            System.out.println("Error: [saveAssociateResponseActivityInstance] " +
                    "[ActivityInstanceController] Couldn't find workflow instance");
            return null;
        }
        List<ActivityInstance> activityInstances1 = activityInstanceService.
                getAllActivitiesInstanceForWorkflowInstance(workflowInstance);
        if (activityInstanceService.updateActivityInstancesIfAuto(activityInstances1) == null) {
            System.out.println("Error: [saveAssociateResponseActivityInstance] [ActivityInstanceController] couldn't update next activity instance");
            return null;
        }

        return "Success";
    }


}

