package com.ranum.adventure.bna.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_pengembalian")
public class Pengembalian {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "penyewaan_id")
    private Penyewaan penyewaan; // refer ke data penyewaan awal

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime tanggalPengembalian;

    @Column(name = "denda")
    private BigDecimal denda; // jika telat, bisa diisi

    @Column(name = "status_pengembalian")
    private String statusPengembalian; // "SELESAI", "TERLAMBAT", dll.

    @Column(name = "keterangan")
    private String keterangan;
    // Getters dan Setters
}
