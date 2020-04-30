package com.example.workflowdatamodel.service;

import com.example.workflowdatamodel.entity.Associates;
import com.example.workflowdatamodel.entity.Individual;
import com.example.workflowdatamodel.repository.IndividualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IndividualService {
    private IndividualRepository individualRepository;

    @Autowired
    public void setIndividualRepository(IndividualRepository individualRepository) {
        this.individualRepository = individualRepository;
    }

    public Individual addIndividual(String vertical_name, Associates employee) {
        Individual individual = new Individual();
        individual.setVertical(vertical_name);
        individual.setAssociates(employee);
        try {
            individualRepository.save(individual);
            return individual;

        } catch (DataAccessException error) {
            System.out.println("Error: [addIndividual][IndividualService] " + error.getLocalizedMessage());
        }
        return null;
    }

    public Individual getByIndividualId(Long individual_id) {
        return individualRepository.findById(individual_id).orElse(null);
    }

    public List<Individual> getAllIndividuals(){
        List<Individual> individuals = new ArrayList<>();

        for (Individual individual: individualRepository.findAll()){
            individual.setAssociates(null);
            individual.setActivities(null);
            individuals.add(individual);
        }
        if(individuals.isEmpty()){
            return null;
        }
        return individuals;
    }

}
