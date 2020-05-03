package org.example.flowkit.service;

import org.example.flowkit.entity.*;
import org.example.flowkit.repository.ActivityRepository;
import org.example.flowkit.service.implementation.ActivityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService implements ActivityServiceImpl {

    private ActivityRepository activityRepository;

    public ActivityService() {
    }

    @Autowired
    public void setActivityRepository(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public Activity createActivity(String name, String description, boolean auto, Double offsetX, Double offsetY,
                                   String shape, Associates other_associate, Individual individual, Roles role,
                                   boolean all_any, Workflow workflow) {
        Activity activity = new Activity();
        activity.setName(name);
        activity.setDescription(description);
        activity.setAuto(auto);
        activity.setOffsetX(offsetX);
        activity.setOffsetY(offsetY);
        activity.setShape(shape);
        activity.setOther_associate(other_associate);
        activity.setParticularIndividual(individual);
        activity.setAll_any_role(all_any);
        activity.setRoles(role);
        activity.setWorkflow(workflow);
        try {
            activityRepository.save(activity);
            return activity;
        } catch (DataAccessException error) {
            System.out.println("Error: [createActivity][ActivityService] " + error.getLocalizedMessage());
        }
        return null;
    }

    public Activity updateActivityPredecessor(Activity toBeUpdated, Activity predecessor) {
        toBeUpdated.setSource(predecessor);
        try {
            activityRepository.save(toBeUpdated);
            return toBeUpdated;
        } catch (DataAccessException error) {
            System.out.println("Error: [updateActivityPredecessor][ActivityService] " + error.getLocalizedMessage());
        }
        return null;
    }

    public Activity updateActivitySuccessor(Activity toBeUpdated, Activity successor) {
        toBeUpdated.setDestination(successor);
        try {
            activityRepository.save(toBeUpdated);
            return toBeUpdated;
        } catch (DataAccessException error) {
            System.out.println("Error: [updateActivitySuccessor][ActivityService] " + error.getLocalizedMessage());
        }
        return null;
    }

    public List<Activity> getAllActivitiesForWorkflow(Workflow workflow) {
        List<Activity> activities = activityRepository.findActivitiesByWorkflow(workflow);
        if (activities.isEmpty()) {
            return null;
        }
        return activities;
    }

    public Activity getActivityById(String activity_id){
        return activityRepository.findById(Long.valueOf(activity_id)).orElse(null);
    }

    public Activity getActivityByInstanceId(ActivityInstance activityInstance){
        return activityRepository.findActivitiesByInstance(activityInstance);
    }
}