package com.ranum.adventure.bna.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ranum.adventure.bna.entities.Barang;
import com.ranum.adventure.bna.entities.Keranjang;

public interface KeranjangRepository extends JpaRepository<Keranjang, Long>{

	Optional<Keranjang> findByBarang(Barang barang);
}
