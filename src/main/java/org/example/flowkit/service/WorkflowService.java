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
    public void setWorkFlowRepository(WorkFlowRepository workFlowRepository) {
        this.workFlowRepository = workFlowRepository;
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setActivityRepository(ActivityRepository activityRepository) {
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
