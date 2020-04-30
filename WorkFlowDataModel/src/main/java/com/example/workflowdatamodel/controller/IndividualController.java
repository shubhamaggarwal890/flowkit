package com.example.workflowdatamodel.controller;

import com.example.workflowdatamodel.entity.Associates;
import com.example.workflowdatamodel.entity.Individual;
import com.example.workflowdatamodel.service.AssociateService;
import com.example.workflowdatamodel.service.IndividualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class IndividualController {
    private IndividualService individualService;
    private AssociateService associateService;

    @Autowired
    public void setIndividualService(IndividualService individualService) {
        this.individualService = individualService;
    }

    @Autowired
    public void setAssociateService(AssociateService associateService) {
        this.associateService = associateService;
    }

    @PostMapping("/add_individual")
    public String addRole(@RequestBody Individual individual) {
        Individual individual1;
        if (individual.getAssociates() == null) {
            individual1 = individualService.addIndividual(individual.getVertical(), null);
        } else {
            Associates associate = associateService.getAssociateByEmailID(individual.getAssociates().getEmailId());
            individual1 = individualService.addIndividual(individual.getVertical(), associate);
        }

        if (individual1 == null) {
            return "Error:";
        }
        return "Success:";
    }

    @GetMapping("/get_individuals")
    public List<Individual> getAllIndividual() {
        return individualService.getAllIndividuals();
    }
}
