package com.ranum.adventure.bna.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.ranum.adventure.bna.entities.Penyewaan;

public interface PenyewaanService {

	public Page<Penyewaan> findPaginated(int page, int size);
	
	public Optional<Penyewaan> findById(String id);
	
	public Penyewaan prosesPembayaran(Penyewaan penyewaan, String metodePembayaran);
}
