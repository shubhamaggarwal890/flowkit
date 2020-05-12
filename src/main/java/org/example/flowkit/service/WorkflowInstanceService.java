package org.example.flowkit.service;

import org.example.flowkit.entity.ActivityInstance;
import org.example.flowkit.entity.Associates;
import org.example.flowkit.entity.Workflow;
import org.example.flowkit.entity.WorkflowInstance;
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
    private ActivityInstanceRepository activityInstanceRepository;
    private AssociateService associateService;

    public WorkflowInstanceService() {
    }

    @Autowired
    public void setWorkflowInstanceRepository(WorkflowInstanceRepository workflowInstanceRepository) {
        this.workflowInstanceRepository = workflowInstanceRepository;
    }

    @Autowired
    public void setActivityInstanceRepository(ActivityInstanceRepository activityInstanceRepository) {
        this.activityInstanceRepository = activityInstanceRepository;
    }

    @Autowired
    public void setAssociateService(AssociateService associateService) {
        this.associateService = associateService;
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


    public List<WorkflowInstance> getAllWorkflowInstanceRelatedAssociate(Associates associate) {
        List<ActivityInstance> activityInstances = activityInstanceRepository.getActivityInstanceByActivityAssociate(
                associate, "PENDING");
        if (activityInstances == null || activityInstances.isEmpty()) {
            return null;
        }
        List<WorkflowInstance> workflowInstances = new ArrayList<>();
        for (ActivityInstance activityInstance : activityInstances) {
            if (activityInstance.getSource().getStatus().equals("ACCEPT")) {
                WorkflowInstance workflowInstance = workflowInstanceRepository.getWorkflowInstanceByActivityInstances(activityInstance);
                if (workflowInstance == null) {
                    return null;
                } else {
                    workflowInstances.add(workflowInstance);
                    Associates customer = associateService.findAssociateByWorkflowInstance(workflowInstance);
                    if (customer == null) {
                        System.out.println("Error: [getAllWorkflowInstanceRelatedAssociate][WorkflowInstanceService] " +
                                "no customer for workflow instance found");
                        return null;
                    }
                }
            }
        }
        return workflowInstances;
    }

    public WorkflowInstance getWorkflowInstanceByActivityInstance(ActivityInstance activityInstance){
        return workflowInstanceRepository.getWorkflowInstanceByActivityInstances(activityInstance);
    }
}
