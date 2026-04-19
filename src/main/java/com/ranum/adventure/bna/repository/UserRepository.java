package com.ranum.adventure.bna.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ranum.adventure.bna.entities.User;


public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);
	
//	ChangePasswordUserDto findByEmailDto(String email);
	
	User findByPassword(String password);
	
	Long getUserById(Long userId);
}
