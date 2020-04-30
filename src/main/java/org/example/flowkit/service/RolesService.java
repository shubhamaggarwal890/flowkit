package org.example.flowkit.service;

import org.example.flowkit.entity.Roles;
import org.example.flowkit.repository.RoleRepository;
import org.example.flowkit.service.implementation.RolesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RolesService implements RolesServiceImpl {
    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Roles addRole(String role_name){
        Roles role = new Roles();
        role.setName(role_name);
        try {
            roleRepository.save(role);
            return role;
        }catch (DataAccessException error){
            System.out.println("Error: [addRole][RolesService]: "+error.getLocalizedMessage());
        }
        return null;
    }

    public Roles getRoleById(Long role_id){
        Optional<Roles> role = roleRepository.findById(role_id);
        return role.orElse(null);
    }

    public List<Roles> getAllRoles(){
        List<Roles> roles = new ArrayList<>();
        for (Roles role: roleRepository.findAll()) {
            role.setAssociates(null);
            roles.add(role);
        }
        if(roles.isEmpty()){
            return null;
        }
        return roles;
    }
}
