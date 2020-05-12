package org.example.flowkit.service;

import org.example.flowkit.entity.*;
import org.example.flowkit.repository.AssociateRepository;
import org.example.flowkit.service.implementation.AssociateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class AssociateService implements AssociateServiceImpl {

    private AssociateRepository associateRepository;

    public AssociateService() {
    }

    private static byte[] getSaltPassword() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getSecurePassword(String password, byte[] salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(salt);
            byte[] password_bytes = messageDigest.digest(password.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte password_byte : password_bytes) {
                stringBuilder.append(Integer.toString((password_byte & 0xff) + 0x100, 16).substring(1));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Autowired
    public void setAssociateRepository(AssociateRepository associateRepository) {
        this.associateRepository = associateRepository;
    }

    public Associates getAssociateByEmailID(String emailId) {
        return associateRepository.findByEmailId(emailId);
    }

    public Associates addAssociate(String first_name, String last_name, String email, String password,
                                   Associates manager, Roles role) {
        Associates employee = new Associates();
        employee.setFirstName(first_name);
        employee.setLastName(last_name);
        employee.setEmailId(email);
        byte[] salt = getSaltPassword();
        if (salt == null) {
            System.out.println("Error: [addAssociate][AssociateService]: Couldn't generate salt");
            return null;
        }
        employee.setSalt(salt);
        String secure_password = getSecurePassword(password, salt);
        if (secure_password == null) {
            System.out.println("Error: [addAssociate][AssociateService]: Couldn't generate password");
            return null;
        }
        employee.setPassword(secure_password);
        if (manager != null) {
            employee.setManager(manager);
        }
        employee.setRole(role);
        try {
            associateRepository.save(employee);
            return employee;
        } catch (DataAccessException error) {
            System.out.println("Error: [addAssociate][AssociateService] " + error.getLocalizedMessage());
        }
        return null;
    }

    public Associates checkSignIn(String email, String password) {
        Associates associate = getAssociateByEmailID(email);
        if (associate == null) {
            return null;
        }
        byte[] salt = associate.getSalt();
        String securePassword = getSecurePassword(password, salt);
        if (securePassword == null) {
            System.out.println("Error: [checkSignIn][AssociateService]: Couldn't generate password");
            return null;
        }
        if (securePassword.equals(associate.getPassword())) {
            return associate;
        }
        return null;
    }

    public Associates getAssociateById(Long id) {
        Optional<Associates> associate = associateRepository.findById(id);
        return associate.orElse(null);
    }

    public Associates updateManager(Long associate_id, String manager_email) {
        Associates associate = getAssociateById(associate_id);
        Associates manager = getAssociateByEmailID(manager_email);
        if (associate == null) {
            return null;
        }
        if (manager == null) {
            return null;
        }
        associate.setManager(manager);
        try {
            associateRepository.save(associate);
            return associate;
        } catch (DataAccessException error) {
            System.out.println("Error: [updateManager][AssociateService] " + error.getLocalizedMessage());
        }
        return null;
    }

    public Associates findOtherAssociateForActivity(Activity activity) {
        return associateRepository.findOtherAssociateForActivity(activity);
    }

    public Associates findAssociateBasedIndividual(Individual individual) {
        return associateRepository.findAssociateForIndividual(individual);
    }

    public List<Associates> findAssociateBasedRole(Roles role) {
        return associateRepository.findAssociateForRole(role);
    }

    public Associates findAssociateManager(Associates associates) {
        return associateRepository.findAssociateManager(associates);
    }

    public void updateRoleOfAssociate(Associates associates, Roles role) {
        try {
            associates.setRole(role);
            associateRepository.save(associates);
        } catch (DataAccessException error) {
            System.out.println("Error: [updateRoleOfAssociate][AssociateService] " + error.getLocalizedMessage());
        }
    }

    public Associates findAssociateBasedIndividualVertical(Associates associates, String vertical) {
        if (vertical.equals("Manager")) {
            return associateRepository.findAssociateManager(associates);
        }
        return null;
    }

    public Associates findAssociateByWorkflowInstance(WorkflowInstance workflowInstance) {
        return associateRepository.findAssociateByWorkflowInstances(workflowInstance);
    }

    public Associates findAssociateByActivityAssociate(ActivityAssociates activityAssociate) {
        return associateRepository.findAssociateByActivityAssociates(activityAssociate);
    }

}
