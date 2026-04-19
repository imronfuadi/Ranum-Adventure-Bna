package com.ranum.adventure.bna.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.ranum.adventure.bna.dto.KategoriDto;
import com.ranum.adventure.bna.dto.PenyewaanDto;
import com.ranum.adventure.bna.entities.Kategori;
import com.ranum.adventure.bna.entities.Penyewaan;

public interface LaporanService {

	public List<PenyewaanDto> findAllPenyewaan();

//	public Kategori saveKategori(KategoriDto dto);
//
//	public KategoriDto findKategoriById(Long id);
//	
//	public KategoriDto updateKategori(Long id, KategoriDto kategoriDto);
//
//	public void deleteKategori(Long id);
	
	public Page<Penyewaan> findPaginated(int page, int size);
	
	Page<Penyewaan> searchPaginated(String keyword, int page, int size);

}
