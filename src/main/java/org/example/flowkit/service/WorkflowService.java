package org.example.flowkit.service;

import org.example.flowkit.entity.Activity;
import org.example.flowkit.entity.Associates;
import org.example.flowkit.entity.Workflow;
import org.example.flowkit.repository.ActivityRepository;
import org.example.flowkit.repository.WorkFlowRepository;
import org.example.flowkit.service.implementation.WorkflowServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkflowService implements WorkflowServiceImpl {

    private WorkFlowRepository workFlowRepository;

    private ActivityService activityService;

    private ActivityRepository activityRepository;

    public WorkflowService() {
    }

    @Autowired
    public void SetWorkflowService(WorkFlowRepository workFlowRepository, ActivityService activityService,
                           ActivityRepository activityRepository) {
        this.workFlowRepository = workFlowRepository;
        this.activityService = activityService;
        this.activityRepository = activityRepository;
    }

    public Workflow createWorkflow(String title, String description, Integer deadline_days, Associates creator){
        Workflow workflow = new Workflow();
        workflow.setTitle(title);
        workflow.setDescription(description);
        workflow.setDeadlineDays(deadline_days);
        workflow.setCreator(creator);
        try {
            workFlowRepository.save(workflow);
            return workflow;
        } catch (DataAccessException error) {
            System.out.println("Error: [createWorkflow][WorkflowService] " + error.getLocalizedMessage());
        }
        return null;
    }

    public List<Workflow> getAllWorkflows() {
        List<Workflow> workflows = new ArrayList<>();
        workFlowRepository.findAll().forEach(workflows::add);
        return workflows;
    }

    public Workflow getWorkflowById(Long workflow_id){
        Optional<Workflow> workflow = workFlowRepository.findById(workflow_id);
        return workflow.orElse(null);
    }

    public Workflow connectActivities(List<Activity> activities, Long workflow_id) {
        Workflow workflow = getWorkflowById(workflow_id);
        if(workflow==null){
            System.out.println("Error: [connectActivities][WorkflowService]: No workflow found");
            return null;
        }
        for (int i = 0; i < activities.size() - 1; i++) {
            Activity current = activities.get(i);
            Activity next = activities.get(i + 1);
            current = activityService.updateActivitySuccessor(current, next);
            activities.set(i, current);
        }
        for (int i = activities.size()-1; i > 0; i--) {
            Activity current = activities.get(i);
            Activity previous = activities.get(i - 1);
            current = activityService.updateActivityPredecessor(current, previous);
            activities.set(i, current);
        }
        try{
            for (Activity activity : activities) {
                activityRepository.save(activity);
            }
            return workflow;
        }catch (DataAccessException error){
            System.out.println("Error: [connectActivities][WorkflowService] " + error.getLocalizedMessage());
        }
        return null;
    }

    public String deleteWorkflow(Workflow workflow){
        if(!workflow.getWorkflowInstances().isEmpty()){
            return "Warning:";
        }
        try{
            workFlowRepository.delete(workflow);
            return "Success:";
        }catch (DataAccessException error){
            System.out.println("Error: [deleteWorkflow][WorkflowService] " + error.getLocalizedMessage());
        }
        return "Error:";
    }
}
