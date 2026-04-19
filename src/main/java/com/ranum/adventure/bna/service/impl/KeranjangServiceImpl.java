package com.ranum.adventure.bna.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.ranum.adventure.bna.dto.KategoriDto;
import com.ranum.adventure.bna.entities.Kategori;
import com.ranum.adventure.bna.repository.KategoriRepository;
import com.ranum.adventure.bna.repository.KeranjangRepository;
import com.ranum.adventure.bna.service.KategoriService;
import com.ranum.adventure.bna.service.KeranjangService;


@Service
public class KeranjangServiceImpl implements KeranjangService {

	@Autowired
	private KeranjangRepository keranjangRepository;

	@Override
	public void deleteKeranjang(Long id) {
		// TODO Auto-generated method stub
		this.keranjangRepository.deleteById(id);
	}
	
	//	@Autowired
//	private KategoriRepository kategoriRepository;
//
//	@Override
//	public List<KategoriDto> findAllKategori() {
//		List<Kategori> k = kategoriRepository.findAll();
//		return k.stream().map(this::convertToDto).collect(Collectors.toList());
//	}
//
//	@Override
//	public Kategori saveKategori(KategoriDto dto) {
//		Kategori kategori = convertToEntity(dto);
//		return kategoriRepository.save(kategori);
//	}
//
//	@Override
//	public KategoriDto findKategoriById(Long id) {
////		Optional<Kategori>kategori = kategoriRepository.findById(id);
////		Optional<KategoriDto> dto = convertToDto(kategori);
//		return kategoriRepository.findById(id).map(this::convertToDto).orElse(null);
//	}
//
//	@Override
//	public KategoriDto updateKategori(Long id, KategoriDto kategoriDto) {
//		// TODO Auto-generated method stub
//		return kategoriRepository.findById(id).map(existingKategori -> {
//			existingKategori.setNamaKategori(kategoriDto.getNamaKategori());
//			Kategori updatedKategori = kategoriRepository.save(existingKategori);
//			return convertToDto(updatedKategori);
//		}).orElse(null);
//	}

	

//	@Override
//	public Page<Kategori> findPaginated(int page, int size) {
//		// TODO Auto-generated method stub
//		return kategoriRepository.findAll(PageRequest.of(page, size));
//	}

//	private KategoriDto convertToDto(Kategori kategori) {
//		KategoriDto dto = new KategoriDto();
//		dto.setKategoriId(kategori.getKategoriId());
//		dto.setNamaKategori(kategori.getNamaKategori());
//		return dto;
//	}
//
//	private Kategori convertToEntity(KategoriDto dto) {
//		Kategori kategori = new Kategori();
//		kategori.setKategoriId(dto.getKategoriId());
//		kategori.setNamaKategori(dto.getNamaKategori());
//		return kategori;
//	}

}
