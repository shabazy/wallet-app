package com.tech.api.repository;

import com.tech.api.entity.Role;
import com.tech.api.entity.RoleName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long> {

    Optional<Role> findByName(RoleName roleName);

}
