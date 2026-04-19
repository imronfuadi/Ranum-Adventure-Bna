package com.ranum.adventure.bna.entities;


import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name ="berita")
public class Berita {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "judul", nullable = false)
	@NotBlank(message = "Judul Harus Diisi")
	private String judul;
	
	@Column(name = "deskripsi", columnDefinition = "TEXT", nullable = false)
	@NotBlank(message = "Deskripsi Harus Diisi")
	private String deskripsi;
	
	@Column(name = "pict_berita", nullable = false)
	private String pictBerita;
	
	@Column(name = "tanggal", columnDefinition = "date")
	@NotNull(message = "Tanggal tidak boleh kosong")
	private LocalDate tanggal;
	
	@Transient
	public String getPhotosBeritaPath() {
        if (pictBerita == null || id == null) return null;
         
        return "/assets/images/berita/" + pictBerita;
//        return "/user-photos/" + id + "/" + pict;
    }
}
