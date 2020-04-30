package org.example.flowkit.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class WorkflowInstanceController {
//    private WorkflowService workflowService;
//    private AssociateService associateService;
//    private WorkflowInstanceService workflowInstanceService;
//
//    @Autowired
//    public void setWorkflowService(WorkflowService workflowService) {
//        this.workflowService = workflowService;
//    }
//
//    @Autowired
//    public void setAssociateService(AssociateService associateService) {
//        this.associateService = associateService;
//    }
//
//    @Autowired
//    public void setWorkflowInstanceService(WorkflowInstanceService workflowInstanceService) {
//        this.workflowInstanceService = workflowInstanceService;
//    }
//
//    @PostMapping("/get_workflows_instance")
//    public List<WorkflowResponse> getWorkflowInstance(@RequestBody Associates associate) {
//        List<WorkflowResponse> workflows = new ArrayList<>();
//        List<WorkflowInstance> workflowInstances = workflowInstanceService.GetAllWorkflowInstanceForCustomer(
//                String.valueOf(associate.getId()));
//
//        if (workflowInstances == null) {
//            return null;
//        }
//
//        for (WorkflowInstance workflowInstance : workflowInstances) {
//            WorkflowResponse workflowResponse = new WorkflowResponse();
//            Integer deadline_days = workflowInstance.getWorkflow().getDeadlineDays();
//            Date deadline = new Date();
//            deadline.setTime(workflowInstance.getInstance_date().getTime() + deadline_days * 24 * 60 * 60 * 1000);
//            Associates associates = workflowInstance.getInitiator();
//            workflowResponse.setWorkflow_id(workflowInstance.getId());
//            workflowResponse.setWorkflow_title(workflowInstance.getTitle());
//            workflowResponse.setCreator_name(associates.getFirstName() + " " + associates.getLastName());
//            workflowResponse.setCreator_email(associates.getEmailId());
//            workflowResponse.setInitiate_date(workflowInstance.getInstance_date().toString());
//            workflowResponse.setDeadline_date(deadline.toString());
//            workflows.add(workflowResponse);
//        }
//        if (workflows.isEmpty()) {
//            return null;
//        }
//        return workflows;
//    }
//
//    @PostMapping("/get_workflows_instance_associate")
//    public List<WorkflowResponse> getWorkflowInstanceInAssociate(@RequestBody Associates associate) {
//        List<WorkflowResponse> workflows = new ArrayList<>();
//        List<WorkflowInstance> workflowInstances = workflowInstanceService.GetAllWorkflowInstanceForAssociate(
//                String.valueOf(associate.getId()));
//        if (workflowInstances == null) {
//            return null;
//        }
//        for (WorkflowInstance workflowInstance : workflowInstances) {
//            WorkflowResponse workflowResponse = new WorkflowResponse();
//            Integer deadline_days = workflowInstance.getWorkflow().getDeadlineDays();
//            Date deadline = new Date();
//            deadline.setTime(workflowInstance.getInstance_date().getTime() + deadline_days * 24 * 60 * 60 * 1000);
//            Associates associates = workflowInstance.getInitiator();
//            Associates customer = workflowInstance.getWorkflowFor();
//            workflowResponse.setWorkflow_id(workflowInstance.getId());
//            workflowResponse.setWorkflow_title(workflowInstance.getTitle());
//            workflowResponse.setCustomer_email(customer.getEmailId());
//            workflowResponse.setCustomer_name(customer.getFirstName() + " " + customer.getLastName());
//            workflowResponse.setCreator_name(associates.getFirstName() + " " + associates.getLastName());
//            workflowResponse.setCreator_email(associates.getEmailId());
//            workflowResponse.setInitiate_date(workflowInstance.getInstance_date().toString());
//            workflowResponse.setDeadline_date(deadline.toString());
//            workflows.add(workflowResponse);
//        }
//        if (workflows.isEmpty()) {
//            return null;
//        }
//        return workflows;
//    }
//
//
//    @PostMapping("/add_workflow_instance")
//    public String addWorkflowInstance(@ModelAttribute WorkflowJson workflow) {
//
//        System.out.println("here");
//        System.out.println(workflow);
//        System.out.println(workflow.getWorkflow_id());
//        Associates initiator = associateService.GetById(String.valueOf(workflow.getWorkflow_creator()));
//        Associates customer = associateService.GetByEmailId(workflow.getWorkflow_customer());
//        Workflow workflow1 = workflowService.GetById(String.valueOf(workflow.getWorkflow_id()));
//
//        for (ActivityJson activityJson : workflow.getActivity_data()) {
//            System.out.println(activityJson.getActivity_title());
//            if (activityJson.getActivity_document() != null) {
//                System.out.println(activityJson.getActivity_document().getOriginalFilename());
//            }
//            System.out.println("-----------------------------------------------------------");
//        }
//
//        Workflow workflow1 = workflowService.CreateWorkflow(workflow.getWorkflow_title(), associate,
//                workflow.getDeadline());
//        if (workflow1 == null) {
//            return "Error:";
//        }
//        List<ActivityJson> activityJsons = workflow.getActivity_data();
//        List<Activity> activities = new ArrayList<>();
//        for (ActivityJson activity_data : activityJsons) {
//            Activity activity = activityService.CreateActivity(activity_data.getActivity_title(),
//                    activity_data.getActivity_offsetX(), activity_data.getActivity_offsetY(),
//                    activity_data.getActivity_shape(), workflow1);
//            activities.add(activity);
//        }
//
//        for (int i = 0; i < activities.size(); i++) {
//            for (int j = 0; j < activities.size(); j++) {
//                if (activityJsons.get(i).getActivity_id().equals(activityJsons.get(j).getActivity_predecessor())) {
//                    if (activityService.UpdateActivitySuccessor(activities.get(i), activities.get(j)) == null) {
//                        return "Error:";
//                    }
//                    if (activityService.UpdateActivityPredecessor(activities.get(j), activities.get(i)) == null) {
//                        return "Error";
//                    }
//                }
//
//                if (activityJsons.get(i).getActivity_id().equals(activityJsons.get(j).getActivity_successor())) {
//                    if (activityService.UpdateActivitySuccessor(activities.get(j), activities.get(i)) == null) {
//                        return "Error";
//                    }
//                    if (activityService.UpdateActivityPredecessor(activities.get(i), activities.get(j)) == null) {
//                        return "Error";
//                    }
//                }
//            }
//        }
//        return "Error:";
//    }

}
