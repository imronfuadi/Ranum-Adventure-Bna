package com.ranum.adventure.bna.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.ranum.adventure.bna.dto.BarangDto;
import com.ranum.adventure.bna.entities.Barang;

public interface BarangService {

	public List<BarangDto> findAllBarang();
	
	public List<Barang> findAll();

	public Barang saveBarang(BarangDto dto, MultipartFile foto);

	public BarangDto findBarangById(Long id);
	
	public BarangDto updateBarang(Long id, BarangDto barangDto, MultipartFile file) throws IOException;

	public void deleteBarang(Long id);
	
	public Page<Barang> findPaginated(int page, int size);
}
