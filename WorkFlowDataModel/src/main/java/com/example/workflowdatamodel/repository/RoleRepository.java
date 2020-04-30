package com.example.workflowdatamodel.repository;

import com.example.workflowdatamodel.entity.Roles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Roles, Long> {
}
