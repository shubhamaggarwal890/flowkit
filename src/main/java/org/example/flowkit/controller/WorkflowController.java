package org.example.flowkit.controller;

import org.example.flowkit.entity.*;
import org.example.flowkit.jsonobject.*;
import org.example.flowkit.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class WorkflowController {
    private WorkflowService workflowService;
    private AssociateService associateService;
    private ActivityService activityService;
    private IndividualService individualService;
    private RolesService rolesService;

    @Autowired
    public void setWorkflowService(WorkflowService workflowService) {
        this.workflowService = workflowService;
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
    public void setRolesService(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @Autowired
    public void setIndividualService(IndividualService individualService) {
        this.individualService = individualService;
    }

    @PostMapping("/add_workflow")
    public String addWorkflow(@RequestBody WorkflowRequest workflow) {
        Associates associate = associateService.getAssociateById(workflow.getCreator().getId());
        if (associate == null) {
            return "Error:";
        }

        Workflow workflow1 = workflowService.createWorkflow(workflow.getTitle(), workflow.getDescription(),
                workflow.getDeadline(), associate);

        if (workflow1 == null) {
            return "Error:";
        }

        List<ActivityRequest> activities = workflow.getActivities();
        List<Activity> createdActivities = new ArrayList<>();

        for (ActivityRequest activity : activities) {
            Associates otherAssociate = null;
            Roles roles = null;
            Individual individual = null;
            if (activity.getOther().getEmail() != null) {
                otherAssociate = associateService.getAssociateByEmailID(activity.getOther().getEmail());
                if (otherAssociate == null) {
                    return "Error:";
                }
            }
            if (activity.getRole().getId() != null) {
                roles = rolesService.getRoleById(Long.valueOf(activity.getRole().getId()));
                if(roles==null){
                    return "Error:";
                }
            }
            if (activity.getIndividual().getId() != null) {
                individual = individualService.getByIndividualId(Long.valueOf(activity.getIndividual().getId()));
                if(individual==null){
                    return "Error:";
                }
            }

            Activity activity1 = activityService.createActivity(activity.getTitle(), activity.getDescription(),
                    activity.isAuto(), activity.getOffsetX(), activity.getOffsetY(), activity.getShape(), otherAssociate,
                    individual, roles, activity.isAny_all(), workflow1);
            if (activity1 == null) {
                return "Error:";
            }
            createdActivities.add(activity1);
        }

        for (int i = 0; i < createdActivities.size(); i++) {
            for (int j = 0; j < createdActivities.size(); j++) {
                if (activities.get(i).getId().equals(activities.get(j).getPredecessor())) {
                    if (activityService.updateActivitySuccessor(createdActivities.get(i), createdActivities.get(j)) == null) {
                        return "Error:";
                    }
                    if (activityService.updateActivityPredecessor(createdActivities.get(j), createdActivities.get(i)) == null) {
                        return "Error";
                    }
                }
                if (activities.get(i).getId().equals(activities.get(j).getSuccessor())) {
                    if (activityService.updateActivitySuccessor(createdActivities.get(j), createdActivities.get(i)) == null) {
                        return "Error:";
                    }
                    if (activityService.updateActivityPredecessor(createdActivities.get(i), createdActivities.get(j)) == null) {
                        return "Error";
                    }
                }
            }
        }
        return "Success:";
    }

    @GetMapping("/get_workflows_designer")
    public List<WorkflowResponse> getAllWorkflows() {
        List<WorkflowResponse> workflows = new ArrayList<>();
        for (Workflow workflow : workflowService.getAllWorkflows()) {
            Associates associates = workflow.getCreator();
            WorkflowResponse workflowResponse = new WorkflowResponse();
            workflowResponse.setId(workflow.getId());
            workflowResponse.setTitle(workflow.getTitle());
            workflowResponse.setDescription(workflow.getDescription());
            workflowResponse.setName(associates.getFirstName() + " " + associates.getLastName());
            workflowResponse.setEmail(associates.getEmailId());
            workflowResponse.setDeadline(workflow.getDeadlineDays());
            workflows.add(workflowResponse);
        }
        if (workflows.isEmpty()) {
            return null;
        }
        return workflows;
    }

    @GetMapping("/get_workflows")
    public List<WorkflowResponse> getAllWorkflowsAssociate() {
        List<WorkflowResponse> workflows = new ArrayList<>();
        for (Workflow workflow : workflowService.getAllWorkflows()) {
            WorkflowResponse workflowResponse = new WorkflowResponse();
            workflowResponse.setId(workflow.getId());
            workflowResponse.setTitle(workflow.getTitle());
            workflowResponse.setDescription(workflow.getDescription());
            workflowResponse.setDeadline(workflow.getDeadlineDays());
            workflows.add(workflowResponse);
        }
        if (workflows.isEmpty()) {
            return null;
        }
        return workflows;
    }

    @PostMapping("/get_workflow_activity")
    public List<ActivityRequest> getAllActivitiesWorkflow(@RequestBody Workflow workflow) {
        Workflow workflow1 = workflowService.getWorkflowById(workflow.getId());
        if(workflow1==null){
            return null;
        }
        List<ActivityRequest> activityRequests = new ArrayList<>();
        List<Activity> activities = activityService.getAllActivitiesForWorkflow(workflow1);
        for (Activity activity: activities) {

            ActivityRequest activityRequest = new ActivityRequest();
            activityRequest.setId(String.valueOf(activity.getId()));
            activityRequest.setTitle(activity.getName());
            activityRequest.setDescription(activity.getDescription());
            activityRequest.setOffsetX(activity.getOffsetX());
            activityRequest.setOffsetY(activity.getOffsetY());
            activityRequest.setAny_all(activity.isAll_any_role());
            activityRequest.setAuto(activity.isAuto());
            activityRequest.setShape(activity.getShape());
            Activity predecessor = activity.getSource();
            Activity successor = activity.getDestination();
            if(predecessor!=null){
                activityRequest.setPredecessor(String.valueOf(predecessor.getId()));
            }else{
                activityRequest.setPredecessor(null);
            }

            if(successor!=null){
                activityRequest.setSuccessor(String.valueOf(successor.getId()));
            }else{
                activityRequest.setSuccessor(null);
            }

            RoleRequest roleRequest = new RoleRequest();
            IndividualRequest individualRequest = new IndividualRequest();
            Roles roles = activity.getRoles();
            Individual individual = activity.getParticularIndividual();
            Associates other = associateService.findOtherAssociateForActivity(activity);

            if(roles==null){
                activityRequest.setRole(null);
            }else{
                roleRequest.setId(String.valueOf(roles.getId()));
                roleRequest.setName(roles.getName());
                activityRequest.setRole(roleRequest);
            }

            if(individual==null){
                activityRequest.setIndividual(null);
            }else{
                individualRequest.setId(String.valueOf(individual.getId()));
                individualRequest.setName(individual.getVertical());
                Associates individualAssociate = associateService.findAssociateBasedIndividual(individual);
                if(individualAssociate==null){
                    individualRequest.setIndividual(null);
                }else{
                    AssociateRequest associateRequest = new AssociateRequest();
                    associateRequest.setId(individualAssociate.getId());
                    associateRequest.setName(individualAssociate.getFirstName()+" "+individualAssociate.getLastName());
                    associateRequest.setEmail(individualAssociate.getEmailId());
                    individualRequest.setIndividual(associateRequest);
                }
                activityRequest.setIndividual(individualRequest);
            }

            if(other==null){
                activityRequest.setOther(null);
            }else{
                AssociateRequest associateRequest = new AssociateRequest();
                associateRequest.setId(other.getId());
                associateRequest.setName(other.getFirstName()+" "+other.getLastName());
                associateRequest.setEmail(other.getEmailId());
                activityRequest.setOther(associateRequest);
                activityRequest.setIndividual(null);
            }
            activityRequests.add(activityRequest);
        }
        if(activityRequests.isEmpty()){
            return null;
        }
        return activityRequests;
    }


//    @PostMapping("/delete_workflow")
//    public String deleteWorkflow(@RequestBody Workflow workflow) {
//        Workflow workflow1 = workflowService.getWorkflowById(workflow.getId());
//        if (workflow1 == null) {
//            return "Error:";
//        }
//        return workflowService.deleteWorkflow(workflow1);
//    }
}
