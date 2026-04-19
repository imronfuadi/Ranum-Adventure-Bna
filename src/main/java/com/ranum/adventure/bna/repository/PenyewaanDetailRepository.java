package com.ranum.adventure.bna.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ranum.adventure.bna.entities.Penyewaan;
import com.ranum.adventure.bna.entities.PenyewaanDetail;

public interface PenyewaanDetailRepository extends JpaRepository<PenyewaanDetail, Long>{

	List<PenyewaanDetail> findByPenyewaan(Penyewaan penyewaan);
}
