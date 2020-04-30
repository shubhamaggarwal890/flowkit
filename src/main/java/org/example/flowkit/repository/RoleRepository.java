package org.example.flowkit.repository;

import org.example.flowkit.entity.Roles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Roles, Long> {
}
