package com.ranum.adventure.bna.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ranum.adventure.bna.dto.PelangganDto;
import com.ranum.adventure.bna.entities.Barang;
import com.ranum.adventure.bna.entities.Pengembalian;
import com.ranum.adventure.bna.entities.Penyewaan;
import com.ranum.adventure.bna.repository.BarangRepository;
import com.ranum.adventure.bna.repository.PengembalianRepository;
import com.ranum.adventure.bna.repository.PenyewaanRepository;
import com.ranum.adventure.bna.service.PengembalianService;

@Service
public class PengembalianServiceImpl implements PengembalianService {

	@Autowired
	private PenyewaanRepository penyewaanRepository;

	@Autowired
	private PengembalianRepository pengembalianRepository;

	@Autowired
	private BarangRepository barangRepository;

	@Override
	public Pengembalian prosesPengembalian(String penyewaanId, int lamaPerjanjianHari, BigDecimal dendaPerHari) {
		Penyewaan penyewaan = penyewaanRepository.findById(penyewaanId)
				.orElseThrow(() -> new RuntimeException("Penyewaan tidak ditemukan"));

		Pengembalian pengembalian = new Pengembalian();
		pengembalian.setPenyewaan(penyewaan);
		pengembalian.setTanggalPengembalian(LocalDateTime.now());

		// hitung lama sewa
		long lamaSewaHari = Duration.between(penyewaan.getTanggalPinjam().toInstant(ZoneOffset.UTC),
				pengembalian.getTanggalPengembalian().toInstant(ZoneOffset.UTC)).toDays();

//		// Hitung harga sewa per hari
//		BigDecimal hargaSewaPerHari = penyewaan.getTotalHarga().divide(BigDecimal.valueOf(lamaPerjanjianHari),
//				RoundingMode.CEILING);

		// hitung denda
		if (lamaSewaHari > lamaPerjanjianHari) {
			long telatHari = lamaSewaHari - lamaPerjanjianHari;
			pengembalian.setDenda(dendaPerHari.multiply(BigDecimal.valueOf(telatHari)));
//			pengembalian.setDenda(hargaSewaPerHari.multiply(BigDecimal.valueOf(telatHari)));
			pengembalian.setStatusPengembalian("TERLAMBAT");
			penyewaan.setStatusPenyewaan("TERLAMBAT"); // update status penyewaan
		} else {
			pengembalian.setDenda(BigDecimal.ZERO);
			pengembalian.setStatusPengembalian("SELESAI");
			penyewaan.setStatusPenyewaan("SELESAI"); // update status penyewaan
		}

		// update stok
		penyewaan.getDetailList().forEach(detail -> {
			Barang barang = detail.getBarang();
			barang.setJumlah(barang.getJumlah() + detail.getJumlah());
			barangRepository.save(barang);
		});

		// Simpan pengembalian dan update penyewaan
		pengembalianRepository.save(pengembalian);
		penyewaanRepository.save(penyewaan);

		return pengembalian;
	}

	public Page<Pengembalian> findPaginated(int page, int size) {
		// TODO Auto-generated method stub
		return pengembalianRepository.findAll(PageRequest.of(page, size));
	}

	public Pengembalian findPengembalianById(Long id) {

		return pengembalianRepository.findById(id).orElse(null);

	}
	
	public Pengembalian saveDenda(Long id, Pengembalian updatedPengembalian) {
	    Pengembalian existing = pengembalianRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Data pengembalian tidak ditemukan dengan ID: " + id));

	    existing.setDenda(updatedPengembalian.getDenda());
	    existing.setKeterangan(updatedPengembalian.getKeterangan());

	    return pengembalianRepository.save(existing); // Simpan perubahan
	}

	

}
