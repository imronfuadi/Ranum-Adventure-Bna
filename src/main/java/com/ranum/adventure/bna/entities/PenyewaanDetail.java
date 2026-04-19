package com.ranum.adventure.bna.entities;

import java.math.BigDecimal;

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
@Table(name = "tb_penyewaan_detail")
public class PenyewaanDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "penyewaan_id")
	private Penyewaan penyewaan;

	@ManyToOne
	@JoinColumn(name = "barang_id")
	private Barang barang;

	private Integer jumlah;
	
	private BigDecimal hargaSewa;
	
	private BigDecimal hargaSatuan;
	
	@Transient
    public BigDecimal getSubtotal() {
        return hargaSatuan.multiply(BigDecimal.valueOf(jumlah));
    }
}
