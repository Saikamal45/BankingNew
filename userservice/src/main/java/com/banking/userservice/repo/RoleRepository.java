package com.banking.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.userservice.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,String>{

}
