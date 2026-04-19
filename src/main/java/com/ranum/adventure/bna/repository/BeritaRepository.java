package com.ranum.adventure.bna.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ranum.adventure.bna.entities.Berita;


public interface BeritaRepository extends JpaRepository<Berita, Long>{

	@Query("SELECT b FROM Berita b ORDER BY b.tanggal DESC LIMIT 5")
	public List<Berita> findTop5ByOrderByTanggalDesc();
	
	public List<Berita> findTop3ByOrderByIdDesc();
}
