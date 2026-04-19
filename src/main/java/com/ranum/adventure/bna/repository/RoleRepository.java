package com.ranum.adventure.bna.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ranum.adventure.bna.entities.Role;


public interface RoleRepository extends JpaRepository<Role, Long>{

	Role findByName(String name);
}
