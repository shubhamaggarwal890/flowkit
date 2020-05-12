package org.example.flowkit.controller;

import org.example.flowkit.entity.Associates;
import org.example.flowkit.entity.Roles;
import org.example.flowkit.jsonobject.AssociateRequest;
import org.example.flowkit.jsonobject.AssociateResponse;
import org.example.flowkit.jsonobject.RoleRequest;
import org.example.flowkit.service.AssociateService;
import org.example.flowkit.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AssociateController {

    private AssociateService associateService;
    private RolesService rolesService;

    @Autowired
    public void setAssociateService(AssociateService associateService) {
        this.associateService = associateService;
    }

    @Autowired
    public void setRolesService(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @PostMapping("/add_associate")
    public String addAssociate(@RequestBody Associates associates) {
        Associates employee = associateService.getAssociateByEmailID(associates.getEmailId());
        if (employee != null) {
            return "Employee:";
        }
        Roles roles = rolesService.getRoleById(associates.getRole().getId());
        Associates manager = null;
        if (associates.getManager() != null) {
            manager = associateService.getAssociateByEmailID(associates.getManager().getEmailId());
            if (manager == null) {
                return "Manager:";
            }
        }
        Associates associates1 = associateService.addAssociate(associates.getFirstName(), associates.getLastName(),
                associates.getEmailId(), associates.getPassword(), manager, roles);

        if (associates1 == null) {
            return "Error:";
        }
        return "Success:";
    }

    @PostMapping("/verify_associate")
    public AssociateResponse signIn(@RequestBody Associates associates) {
        Associates associates1 = associateService.checkSignIn(associates.getEmailId(), associates.getPassword());
        if (associates1 == null) {
            return null;
        }
        AssociateResponse associateResponse = new AssociateResponse();
        associateResponse.setFirstName(associates1.getFirstName());
        associateResponse.setId(associates1.getId());
        RoleRequest roleRequest = new RoleRequest();
        Roles roles = associates1.getRole();
        roleRequest.setName(roles.getName());
        associateResponse.setRole(roleRequest);
        return associateResponse;
    }

    @PostMapping("/verify_email")
    public String verifyEmail(@RequestBody Associates associates) {
        if (associateService.getAssociateByEmailID(associates.getEmailId()) == null) {
            return "Error:";
        }
        return "Success:";
    }

    @PostMapping("/associate_manager")
    public AssociateRequest getAssociateManager(@RequestBody Associates associates) {
        AssociateRequest associateRequest = new AssociateRequest();
        Associates associates1 = associateService.getAssociateById(associates.getId());
        if (associates1 == null) {
            return null;
        }
        Associates manager = associateService.findAssociateManager(associates1);
        if (manager == null) {
            return null;
        }
        associateRequest.setName(manager.getFirstName() + " " + manager.getLastName());
        associateRequest.setEmail(manager.getEmailId());
        associateRequest.setId(manager.getId());
        return associateRequest;
    }

}
