package com.ranum.adventure.bna.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_barang")
public class Barang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long barangId;

    @Column(name = "nama_barang")
    private String namaBarang;

    @Column(name = "deskripsi")
    private String deskripsi;

    @Column(name = "foto")
    private String foto;

    @Column(name = "jumlah")
    private Long jumlah;

    @Column(name = "harga")
    private BigDecimal harga;

    @ManyToOne
    @JoinColumn(name = "kategori_id")
    private Kategori kategori;
    
    @Transient
	public String getPhotosBarangPath() {
        if (foto == null || barangId == null) return null;
         
        return "/assets/images/barang/foto/" + foto;
//        return "/user-photos/" + id + "/" + pict;
    }

    // Getters dan Setters
}
