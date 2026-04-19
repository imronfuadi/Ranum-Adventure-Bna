package com.ranum.adventure.bna.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ranum.adventure.bna.entities.Pelanggan;


public interface PelangganRepository extends JpaRepository<Pelanggan, Long> {

	Pelanggan findByEmail(String email);
	
//	ChangePasswordUserDto findByEmailDto(String email);
	
	Pelanggan findByPassword(String password);
	
//	Long getUserById(Long userId);
}
