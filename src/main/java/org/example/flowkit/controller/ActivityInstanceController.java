package org.example.flowkit.controller;

import org.example.flowkit.entity.*;
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
        }
        return activityInstanceResponses;
    }

}

