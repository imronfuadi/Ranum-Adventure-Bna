package com.ranum.adventure.bna.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ranum.adventure.bna.entities.Kontak;
import com.ranum.adventure.bna.repository.KontakRepository;
import com.ranum.adventure.bna.service.KontakService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class KontakServiceImpl implements KontakService{
	
	@Autowired
	private KontakRepository kontakRepository;

	@Override
	public List<Kontak> findAllDataKontak() {
		// TODO Auto-generated method stub
		return kontakRepository.findAll();
	}

	@Override
	public Kontak saveKontak(Kontak kontak) {
		// TODO Auto-generated method stub
		return kontakRepository.save(kontak);
	}

	@Override
	public void deleteKontak(Long id) {
		// TODO Auto-generated method stub
		kontakRepository.deleteById(id);
	}

}
