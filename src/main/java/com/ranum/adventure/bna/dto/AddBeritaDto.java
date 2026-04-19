package com.ranum.adventure.bna.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddBeritaDto {

	@NotBlank(message = "Judul Harus Diisi")
	private String judul;
	
	@NotBlank(message = "Deskripsi Harus Diisi")
	private String deskripsi;
	
	@NotBlank(message = "Foto Harus Diisi")
	private String pictBerita;
	
	@NotBlank(message = "Tanggal Harus Diisi")
	private LocalDate tanggal;
}
