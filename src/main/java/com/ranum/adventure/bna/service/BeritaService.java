package com.ranum.adventure.bna.service;

import java.util.List;
import java.util.Optional;

import com.ranum.adventure.bna.entities.Berita;


public interface BeritaService {

	public List<Berita> findAllBerita();
	
	public Berita saveBerita(Berita berita);
	
	public Optional<Berita> findBeritaById(Long id);
	
	public void deleteBerita(Long id);
	
	public List<Berita> getBeritaTerbaru();
	
	public List<Berita> get3Berita();
}
