package com.ranum.adventure.bna.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.ranum.adventure.bna.dto.KategoriDto;
import com.ranum.adventure.bna.entities.Kategori;

public interface KategoriService {

	public List<KategoriDto> findAllKategori();

	public Kategori saveKategori(KategoriDto dto);

	public KategoriDto findKategoriById(Long id);
	
	public KategoriDto updateKategori(Long id, KategoriDto kategoriDto);

	public void deleteKategori(Long id);
	
	public Page<Kategori> findPaginated(int page, int size);
}
