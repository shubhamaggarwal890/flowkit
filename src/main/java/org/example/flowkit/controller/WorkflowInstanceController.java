package org.example.flowkit.controller;

import org.example.flowkit.entity.*;
import org.example.flowkit.jsonobject.ActivityInstanceRequest;
import org.example.flowkit.jsonobject.AssociateRequest;
import org.example.flowkit.jsonobject.WorkflowInstanceRequest;
import org.example.flowkit.jsonobject.WorkflowInstanceResponse;
import org.example.flowkit.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class WorkflowInstanceController {
    private WorkflowService workflowService;
    private AssociateService associateService;
    private WorkflowInstanceService workflowInstanceService;
    private DocumentService documentService;
    private ActivityService activityService;
    private ActivityInstanceService activityInstanceService;
    private ActivityAssociateService activityAssociateService;

    @Autowired
    public void setWorkflowService(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @Autowired
    public void setAssociateService(AssociateService associateService) {
        this.associateService = associateService;
    }

    @Autowired
    public void setWorkflowInstanceService(WorkflowInstanceService workflowInstanceService) {
        this.workflowInstanceService = workflowInstanceService;
    }

    @Autowired
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setActivityInstanceService(ActivityInstanceService activityInstanceService) {
        this.activityInstanceService = activityInstanceService;
    }

    @Autowired
    public void setActivityAssociateService(ActivityAssociateService activityAssociateService) {
        this.activityAssociateService = activityAssociateService;
    }

    @PostMapping("/add_workflow_instance")
    public String addWorkflowInstance(@RequestBody WorkflowInstanceRequest workflowInstanceRequest) {
        Associates creator = associateService.getAssociateById(workflowInstanceRequest.getCreator());
        if (creator == null) {
            System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] Creator not found");
            return "Error:";
        }
        Associates customer = null;
        if (workflowInstanceRequest.getCustomer() != null) {
            customer = associateService.getAssociateByEmailID(workflowInstanceRequest.getCustomer());
        } else {
            customer = creator;
        }
        Workflow workflow = workflowService.getWorkflowById(workflowInstanceRequest.getId());
        if (workflow == null) {
            System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] workflow not found");
            return "Error:";
        }

        WorkflowInstance workflowInstance = workflowInstanceService.createWorkflowInstance(
                workflowInstanceRequest.getTitle(), workflowInstanceRequest.getDescription(),
                creator, customer, workflow);

        if (workflowInstance == null) {
            System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] workflow instance didn't create");
            return "Error:";
        }

        List<ActivityInstanceRequest> activities = workflowInstanceRequest.getActivities();
        List<ActivityInstance> createdActivitiesInstance = new ArrayList<>();

        for (ActivityInstanceRequest activity : activities) {
            Document document = null;
            if (activity.getDocument().getId() != null) {
                document = documentService.getDocumentById(activity.getDocument().getId());
            }
            Activity activity1 = activityService.getActivityById(activity.getId());
            if (activity1 == null) {
                System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] activity not found by id");
                return "Error:";
            }
            ActivityInstance activityInstance = activityInstanceService.createActivityInstance(activity.getTitle(),
                    activity.getRemark(), "PENDING", activity1, document, workflowInstance);
            if (activityInstance == null) {
                return "Error:";
            }
            createdActivitiesInstance.add(activityInstance);
            Roles roles = activity1.getRoles();
            Individual individual = activity1.getParticularIndividual();
            Associates other = associateService.findOtherAssociateForActivity(activity1);

            if (roles != null) {
                List<ActivityAssociates> activityAssociates = activityAssociateService.
                        addActivityInstanceAssociatesBasedRole(activityInstance, roles);
                if (activityAssociates == null || activityAssociates.isEmpty()) {
                    System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] activity associate found to be null for roles");
                    return "Error:";
                }
            } else if (individual != null && other == null) {
                ActivityAssociates activityAssociates = activityAssociateService.addActivityInstanceAssociatesBasedIndividual(
                        activityInstance, individual, creator, activity);
                if (activityAssociates == null) {
                    System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] activity associates found to be null for individual");
                    return "Error:";
                }
            } else if (other != null) {
                ActivityAssociates activityAssociates = activityAssociateService.addActivityInstanceAssociatesBasedUser(
                        activityInstance, other);
                if (activityAssociates == null) {
                    System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] activity associates found to be null for others");
                    return "Error:";
                }
            } else if (!activity1.isAuto()) {
                if (activity.getAssociate() == null) {
                    System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] couldn't find associate to activity instance for manual");
                    return "Error:";
                }
                Associates user_specified = associateService.getAssociateByEmailID(activity.getAssociate());
                ActivityAssociates activityAssociates = activityAssociateService.addActivityInstanceAssociatesBasedUser(
                        activityInstance, user_specified);
                if (activityAssociates == null) {
                    System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] activity associate found to be null for user specified");
                    return "Error:";
                }
            }
        }

        for (int i = 0; i < createdActivitiesInstance.size(); i++) {
            for (int j = 0; j < createdActivitiesInstance.size(); j++) {
                if (activities.get(i).getId().equals(activities.get(j).getPredecessor())) {
                    if (activityInstanceService.updateActivityInstanceSuccessor(createdActivitiesInstance.get(i),
                            createdActivitiesInstance.get(j)) == null) {
                        System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] problem while updating successor");
                        return "Error:";
                    }
                    if (activityInstanceService.updateActivityInstancePredecessor(createdActivitiesInstance.get(j),
                            createdActivitiesInstance.get(i)) == null) {
                        System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] error while updating predecessor");
                        return "Error";
                    }
                }

                if (activities.get(i).getId().equals(activities.get(j).getSuccessor())) {
                    if (activityInstanceService.updateActivityInstanceSuccessor(createdActivitiesInstance.get(j),
                            createdActivitiesInstance.get(i)) == null) {
                        System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] error while updating successor");
                        return "Error:";
                    }
                    if (activityInstanceService.updateActivityInstancePredecessor(createdActivitiesInstance.get(i),
                            createdActivitiesInstance.get(j)) == null) {
                        System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] error while updating predecessor");
                        return "Error";
                    }
                }
            }
        }

        if (activityInstanceService.updateActivityInstancesIfAuto(createdActivitiesInstance) == null) {
            System.out.println("Error: [addWorkflowInstance] [WorkflowInstanceController] couldn't update next activity instance");
            return "Error:";
        }
        return "Success:";
    }

    @PostMapping("/get_workflows_instance")
    public ResponseEntity<List<WorkflowInstanceResponse>> getWorkflowInstanceBasedInitiator(@RequestBody Associates associate) {
        Associates customer = associateService.getAssociateById(associate.getId());
        if (customer == null) {
            System.out.println("Error: [getWorkflowInstanceBasedInitiator] [WorkflowInstanceController] " +
                    "couldn't find customer for the associate provided");
            return ResponseEntity.notFound().build();
        }

        List<WorkflowInstance> workflowInstances = workflowInstanceService.getAllWorkflowInstanceForCustomer(customer);
        if (workflowInstances == null) {
            System.out.println("Error: [getWorkflowInstanceBasedInitiator] [WorkflowInstanceController] " +
                    "workflow instance list is empty");
            return ResponseEntity.ok().body(null);
        }
        String pattern = "E, dd MMM yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        List<WorkflowInstanceResponse> workflowInstanceResponses = new ArrayList<>();

        for (WorkflowInstance workflowInstance : workflowInstances) {
            Workflow workflow = workflowInstance.getWorkflow();
            WorkflowInstanceResponse workflowInstanceResponse = new WorkflowInstanceResponse();
            List<ActivityInstance> activityInstances = activityInstanceService.
                    getAllActivitiesInstanceForWorkflowInstance(workflowInstance);
            String status = "";
            for (ActivityInstance activityInstance: activityInstances){
                if(activityInstance.getStatus().equals("PENDING")){
                    status = activityInstance.getStatus();
                    break;
                }else if (activityInstance.getStatus().equals("REJECT")){
                    status = activityInstance.getStatus()+"ED";
                    break;
                }else{
                    status = activityInstance.getStatus()+"ED";
                }
            }
            workflowInstanceResponse.setId(workflowInstance.getId());
            workflowInstanceResponse.setTitle(workflowInstance.getTitle()+" - "+status);
            workflowInstanceResponse.setDescription(workflowInstance.getDescription());
            workflowInstanceResponse.setWk_description(workflow.getDescription());
            Date deadline = new Date();

            deadline.setTime(workflowInstance.getInstance_date().getTime() + Long.valueOf(workflow.getDeadlineDays()) * 24 * 60 * 60 * 1000);
            workflowInstanceResponse.setDate(simpleDateFormat.format(workflowInstance.getInstance_date()));
            workflowInstanceResponse.setDeadline(simpleDateFormat.format(deadline));
            Associates initiator = associateService.findAssociateByWorkflowInstance(workflowInstance);
            AssociateRequest associateRequest = new AssociateRequest();
            associateRequest.setName(initiator.getFirstName() + " " + initiator.getLastName());
            associateRequest.setEmail(initiator.getEmailId());
            workflowInstanceResponse.setInitiator(associateRequest);
            workflowInstanceResponses.add(workflowInstanceResponse);
        }
        return ResponseEntity.ok().body(workflowInstanceResponses);
    }

    @PostMapping("/get_workflows_instance_associate")
    public ResponseEntity<List<WorkflowInstanceResponse>> getWorkflowInstanceBasedAssociate(@RequestBody Associates associate) {
        Associates associated = associateService.getAssociateById(associate.getId());
        if (associated == null) {
            System.out.println("Error: [getWorkflowInstanceBasedAssociate] [WorkflowInstanceController] " +
                    "couldn't find customer for the associate provided");
            return ResponseEntity.notFound().build();
        }

        List<WorkflowInstance> workflowInstances = workflowInstanceService.getAllWorkflowInstanceRelatedAssociate(
                associated);
        if (workflowInstances == null || workflowInstances.isEmpty()) {
            System.out.println("Error: [getWorkflowInstanceBasedAssociate] [WorkflowInstanceController] " +
                    "workflow instance list is empty");
            return ResponseEntity.ok().body(null);
        }

        String pattern = "E, dd MMM yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        List<WorkflowInstanceResponse> workflowInstanceResponses = new ArrayList<>();

        for (WorkflowInstance workflowInstance : workflowInstances) {
            Workflow workflow = workflowInstance.getWorkflow();
            WorkflowInstanceResponse workflowInstanceResponse = new WorkflowInstanceResponse();
            workflowInstanceResponse.setId(workflowInstance.getId());
            workflowInstanceResponse.setTitle(workflowInstance.getTitle());
            workflowInstanceResponse.setDescription(workflowInstance.getDescription());
            workflowInstanceResponse.setWk_description(workflow.getDescription());
            Date deadline = new Date();
            deadline.setTime(workflowInstance.getInstance_date().getTime() + Long.valueOf(workflow.getDeadlineDays()) * 24 * 60 * 60 * 1000);
            workflowInstanceResponse.setDate(simpleDateFormat.format(workflowInstance.getInstance_date()));
            workflowInstanceResponse.setDeadline(simpleDateFormat.format(deadline));
            Associates initiator = associateService.findAssociateByWorkflowInstance(workflowInstance);
            AssociateRequest associateRequest = new AssociateRequest();
            associateRequest.setName(initiator.getFirstName() + " " + initiator.getLastName());
            associateRequest.setEmail(initiator.getEmailId());
            workflowInstanceResponse.setInitiator(associateRequest);
            workflowInstanceResponses.add(workflowInstanceResponse);
        }
        return ResponseEntity.ok().body(workflowInstanceResponses);
    }
}
