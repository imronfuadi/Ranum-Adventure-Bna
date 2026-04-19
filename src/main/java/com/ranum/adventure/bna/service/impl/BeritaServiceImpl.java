package com.ranum.adventure.bna.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ranum.adventure.bna.entities.Berita;
import com.ranum.adventure.bna.repository.BeritaRepository;
import com.ranum.adventure.bna.service.BeritaService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BeritaServiceImpl implements BeritaService{

	@Autowired
	private BeritaRepository beritaRepository;
	
	@Override
	public List<Berita> findAllBerita() {
		// TODO Auto-generated method stub
		return beritaRepository.findAll();
	}

	@Override
	public Berita saveBerita(Berita berita) {
		// TODO Auto-generated method stub
		return beritaRepository.save(berita);
	}

	@Override
	public Optional<Berita> findBeritaById(Long id) {
		// TODO Auto-generated method stub
		return beritaRepository.findById(id);
	}

	@Override
	public void deleteBerita(Long id) {
		// TODO Auto-generated method stub
		beritaRepository.deleteById(id);
	}

	@Override
	public List<Berita> getBeritaTerbaru() {
		// TODO Auto-generated method stub
		return beritaRepository.findTop5ByOrderByTanggalDesc();
	}

	@Override
	public List<Berita> get3Berita() {
		// TODO Auto-generated method stub
		return beritaRepository.findTop3ByOrderByIdDesc();
	}

}
