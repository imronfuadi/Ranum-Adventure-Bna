package com.ranum.adventure.bna.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "tb_kategori")
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kategoriId;

    @Column(name = "nama_kategori")
    private String namaKategori;

    @OneToMany(mappedBy = "kategori", cascade = CascadeType.ALL)
    private List<Barang> barangList;

    // Getters dan Setters
}

