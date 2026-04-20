package com.ranum.adventure.bna.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_penyewaan")
public class Penyewaan {

    @Id
    @Size(max = 15)
    private String penyewaanId;

    @ManyToOne
    @JoinColumn(name = "barang_id")
    private Barang barang;

    @ManyToOne
    @JoinColumn(name = "pelanggan_id")
    private Pelanggan pelanggan;

    private LocalDateTime tanggalPinjam;

    private LocalDateTime tanggalKembali;

    @OneToOne(mappedBy = "penyewaan", cascade = CascadeType.ALL)
    private Pengembalian pengembalian;
    
    @OneToMany(mappedBy = "penyewaan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PenyewaanDetail> detailList = new ArrayList<>();
    
    @Column(name = "total_harga")
    private BigDecimal totalHarga;
    
    @Column(name = "lama_sewa")
    private Integer lamaSewa;
    
    @Column(name = "metode_pembayaran")
    private String metodeBayar; // misal "CASH"
    
    @Column(name = "status_pembayaran")
    private String statusPembayaran;     // "PAID"

    @Column(name = "status_penyewaan")
    private String statusPenyewaan;     // "ONGOING/SELESAI/TERLAMBAT"
    
    @Column(name = "bukti_transfer")
    private String buktiTransfer;
    
    @Transient
	public String getStatusRealtime() {
        if ("DISEWA".equalsIgnoreCase(statusPenyewaan)) {
            if (tanggalKembali != null && LocalDateTime.now().isAfter(tanggalKembali)) {
                return "TERLAMBAT";
            }
        }
        return statusPenyewaan;
    }

    @Transient
	public String getBuktiTransferPath() {
        if (buktiTransfer == null || penyewaanId == null) return null;
         
        return "/assets/images/bukti-transfer/" + buktiTransfer;
    }

    // Getters dan Setters
}