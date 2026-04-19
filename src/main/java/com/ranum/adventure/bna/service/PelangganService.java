package com.ranum.adventure.bna.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.ranum.adventure.bna.dto.PelangganDto;
import com.ranum.adventure.bna.entities.Pelanggan;


public interface PelangganService {

	public List<PelangganDto> findAllUser();

	public Pelanggan savePelanggan(PelangganDto dto, MultipartFile foto);

	public PelangganDto findPelangganById(Long id);
	
	public PelangganDto updatePelanggan(Long id, PelangganDto pelangganDto, MultipartFile file) throws IOException;

	public void deletePelanggan(Long id);
	
	public Page<Pelanggan> findPaginated(int page, int size);
	
	public Long getUserIdByUsername(String username);
	
	Pelanggan findPelangganByEmail(String email);
}
