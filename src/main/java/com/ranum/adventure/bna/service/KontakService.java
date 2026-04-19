package com.ranum.adventure.bna.service;

import java.util.List;

import com.ranum.adventure.bna.entities.Kontak;


public interface KontakService {

	public List<Kontak> findAllDataKontak();
	
	public Kontak saveKontak(Kontak kontak);
	
	public void deleteKontak(Long id);
}
