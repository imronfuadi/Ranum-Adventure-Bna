package com.ranum.adventure.bna.dto;

import org.springframework.web.multipart.MultipartFile;

import com.ranum.adventure.bna.entities.Penyewaan;

import lombok.Data;

@Data
public class PelangganDto {

	private Long pelangganId;

	private String nama;

	private String alamat;

	private String nik;

	private MultipartFile foto;
	
	private String namaFoto;

	private String nomorHp;

	private String email;

	private String password;

	private Penyewaan penyeaaan;
	
	private Long penyewaanId;

}
