package com.ranum.adventure.bna.dto;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import com.ranum.adventure.bna.entities.Kategori;

import lombok.Data;

@Data
public class BarangDto {

	private Long barangId;

	private String namaBarang;

	private String deskripsi;

	private String namaFoto;

	private MultipartFile foto;

	private Long jumlah;

	private BigDecimal harga;

	private Kategori kategori;

	private Long kategoriId;

	private String namaKategori;

}
