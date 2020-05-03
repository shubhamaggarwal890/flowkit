package org.example.flowkit.service;

import org.example.flowkit.entity.*;
import org.example.flowkit.repository.ActivityInstanceRepository;
import org.example.flowkit.repository.WorkflowInstanceRepository;
import org.example.flowkit.service.implementation.WorkflowInstanceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class WorkflowInstanceService implements WorkflowInstanceServiceImpl {

    private WorkflowInstanceRepository workflowInstanceRepository;
    private ActivityInstanceService activityInstanceService;
    private ActivityInstanceRepository activityInstanceRepository;

    public WorkflowInstanceService() {
    }

    @Autowired
    public void SetWorkflowInstanceService(WorkflowInstanceRepository workflowInstanceRepository,
                                           ActivityInstanceService activityInstanceService,
                                           ActivityInstanceRepository activityInstanceRepository) {
        this.workflowInstanceRepository = workflowInstanceRepository;
        this.activityInstanceService = activityInstanceService;
        this.activityInstanceRepository = activityInstanceRepository;
    }

    public WorkflowInstance createWorkflowInstance(String title, String description, Associates initiator,
                                                   Associates customer, Workflow workflow) {
        WorkflowInstance workflowInstance = new WorkflowInstance();
        workflowInstance.setTitle(title);
        workflowInstance.setDescription(description);
        workflowInstance.setInitiator(initiator);
        workflowInstance.setWorkflowFor(customer);
        workflowInstance.setInstance_date(new Date());
        workflowInstance.setWorkflow(workflow);
        try {
            workflowInstanceRepository.save(workflowInstance);
            return workflowInstance;

        } catch (DataAccessException error) {
            System.out.println("ERROR: [createNewWorkflowInstance][WorkflowInstanceService] " +
                    error.getLocalizedMessage());
        }
        return null;
    }


    public WorkflowInstance getWorkflowInstanceById(Long workflow_id) {
        Optional<WorkflowInstance> workflowInstance = workflowInstanceRepository.findById(workflow_id);
        return workflowInstance.orElse(null);
    }

    public List<WorkflowInstance> getAllWorkflowInstanceForCustomer(Associates customer) {

        List<WorkflowInstance> workflowInstances = workflowInstanceRepository.findByCustomerId(customer);
        if (workflowInstances.isEmpty()) {
            return null;
        }
        return workflowInstances;
    }

    public List<WorkflowInstance> getAllWorkflowInstanceRelatedAssociate(Associates associate){
        List<WorkflowInstance> workflowInstances = new ArrayList<>();
        List<ActivityAssociates> activityAssociates = associate.getActivityAssociates();
        for (ActivityAssociates activityAssociates1 : activityAssociates) {
            ActivityInstance activityInstance = activityAssociates1.getActivity_instance_associate();
            if (activityInstance.getStatus().equals("PENDING") &&
                    activityInstance.getPredecessor().getStatus().equals("ACCEPT")) {
                WorkflowInstance workflowInstance = activityInstance.getWorkflowInstance();
                if (!workflowInstances.contains(workflowInstance)) {
                    workflowInstances.add(workflowInstance);
                }
            }
        }
        if (workflowInstances.isEmpty()) {
            return null;
        }
        return workflowInstances;
    }
}
