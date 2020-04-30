package org.example.flowkit.controller;

import org.example.flowkit.entity.Roles;
import org.example.flowkit.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class RolesController {
    private RolesService rolesService;

    @Autowired
    public void setRolesService(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @PostMapping("/add_role")
    public String addRole(@RequestBody Roles role) {
        Roles roles = rolesService.addRole(role.getName());
        if (roles == null) {
            return "Error:";
        }
        return "Success:";
    }

    @GetMapping("/get_roles")
    public List<Roles> getAllRoles(){
        return rolesService.getAllRoles();
    }
}
