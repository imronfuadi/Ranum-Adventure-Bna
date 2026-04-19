package com.ranum.adventure.bna.controller;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ranum.adventure.bna.entities.Barang;
import com.ranum.adventure.bna.entities.Keranjang;
import com.ranum.adventure.bna.entities.Pelanggan;
import com.ranum.adventure.bna.entities.Penyewaan;
import com.ranum.adventure.bna.entities.PenyewaanDetail;
import com.ranum.adventure.bna.generator.IdGenerator;
import com.ranum.adventure.bna.repository.BarangRepository;
import com.ranum.adventure.bna.repository.KeranjangRepository;
import com.ranum.adventure.bna.repository.PelangganRepository;
import com.ranum.adventure.bna.repository.PenyewaanDetailRepository;
import com.ranum.adventure.bna.repository.PenyewaanRepository;
import com.ranum.adventure.bna.service.impl.KeranjangServiceImpl;

@Controller
public class KeranjangController {

	@Autowired
	private BarangRepository barangRepository;

	@Autowired
	private KeranjangRepository keranjangRepository;

	@Autowired
	private KeranjangServiceImpl keranjangServiceImpl;

	@Autowired
	private PelangganRepository pelangganRepository;

	@Autowired
	private PenyewaanRepository penyewaanRepository;

	@Autowired
	private PenyewaanDetailRepository penyewaanDetailRepository;

	@GetMapping(value = "/barang")
	public String tampilkanBarang(Model model) {
		model.addAttribute("daftarBarang", barangRepository.findAll());
		return "barang"; // barang.html
	}

	@PostMapping(value = "/keranjang/tambah/{id}")
	public String tambahKeKeranjang(@PathVariable("id") Long barangId) {
		Optional<Barang> barangOpt = barangRepository.findById(barangId);

//        if (barangOpt.isEmpty()) {
//            return "redirect:/penyewaan/new"; // barang tidak ditemukan
//        }

		if (barangOpt.isEmpty()) {
			return "redirect:/penyewaan/new?error=barang_not_found";
		}

		Barang barang = barangOpt.get();

		// Cari apakah barang ini sudah ada di keranjang
		Optional<Keranjang> existingKeranjang = keranjangRepository.findByBarang(barang);

		if (existingKeranjang.isPresent()) {
			// Jika barang sudah ada, update jumlah
			Keranjang keranjang = existingKeranjang.get();

			// Cek apakah jumlah di keranjang sudah melebihi stok
			if (keranjang.getJumlah() >= barang.getJumlah()) {
				// Sudah mencapai batas stok
				return "redirect:/penyewaan/new?error=stok_kurang";
			}
			keranjang.setJumlah(keranjang.getJumlah() + 1);
			keranjangRepository.save(keranjang);
			return "redirect:/penyewaan/new?success=barang_ditambahkan";
		} else {
			// Jika barang baru, pastikan stok minimal 1
			if (barang.getJumlah() <= 0) {
				return "redirect:/penyewaan/new?error=stok_habis";
			}

			// Jika belum ada, buat item baru di keranjang
			Keranjang keranjang = new Keranjang();
			keranjang.setBarang(barang);
			keranjang.setJumlah(1);
			keranjangRepository.save(keranjang);
			return "redirect:/penyewaan/new?success=barang_ditambahkan";
		}

//        return "redirect:/penyewaan/new";
//        Optional<Barang> barangOpt = barangRepository.findById(barangId);
//        if (barangOpt.isPresent()) {
//            Barang barang = barangOpt.get();
//            Keranjang keranjang = new Keranjang();
//            keranjang.setBarang(barang);
//            keranjang.setJumlah(1);
//            keranjangRepository.save(keranjang);
//        }
//        return "redirect:/penyewaan/keranjang";
	}

	@GetMapping(value = "/penyewaan/keranjang")
	public String lihatKeranjang(Model model) {
//        model.addAttribute("daftarKeranjang", keranjangRepository.findAll());
		List<Keranjang> daftarKeranjang = keranjangRepository.findAll(); // atau findByUser, dsb
		List<Pelanggan> listPelanggan = pelangganRepository.findAll();
		// Hitung total harga
		BigDecimal totalHarga = daftarKeranjang.stream()
				.map(k -> k.getBarang().getHarga().multiply(BigDecimal.valueOf(k.getJumlah())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
		String totalHargaFormat = nf.format(totalHarga); // contoh "135.000"
		model.addAttribute("totalHargaFormat", totalHargaFormat);

		List<Map<String, Object>> keranjangData = new ArrayList<>();

		for (Keranjang k : daftarKeranjang) {
			Map<String, Object> data = new HashMap<>();
			data.put("id", k.getId());
			data.put("barang", k.getBarang());
			data.put("jumlah", k.getJumlah());
			data.put("hargaFormat", nf.format(k.getBarang().getHarga()));
			data.put("subtotalFormat", nf.format(k.getBarang().getHarga().multiply(BigDecimal.valueOf(k.getJumlah()))));
			keranjangData.add(data);
		}
		model.addAttribute("keranjangData", keranjangData);

		model.addAttribute("listPelanggan", listPelanggan);
		model.addAttribute("daftarKeranjang", daftarKeranjang);
		model.addAttribute("totalHarga", totalHarga);
		return "/be-admin/penyewaan/keranjang"; // keranjang.html
	}

	@PostMapping(value = "/keranjang/update/{id}")
	public String updateJumlah(@PathVariable("id") Long id, @RequestParam("jumlah") int jumlah) {
		Keranjang keranjang = keranjangRepository.findById(id).orElse(null);
		if (keranjang != null && jumlah > 0) {
			keranjang.setJumlah(jumlah);
			keranjangRepository.save(keranjang);
		}
		return "redirect:/keranjang";
	}

	@GetMapping(value = "/keranjang/delete/{id}")
	public String hapusItem(@PathVariable(value = "id") Long id) {
		this.keranjangServiceImpl.deleteKeranjang(id);
		return "redirect:/penyewaan/keranjang";
	}

//    @PostMapping("/keranjang/checkout")
//    public String checkout(
//            @RequestParam("penyewaId") Long penyewaId,
//            @RequestParam("totalHarga") BigDecimal totalHarga,
//            @RequestParam("lamaSewa") Integer lamaSewa, Model model) {
//
//        // 1. Ambil penyewa
//        Pelanggan pelanggan = pelangganRepository.findById(penyewaId)
//                             .orElseThrow(() -> new RuntimeException("Penyewa not found"));
//        
//        // total harga dikali lamaSewa
//        BigDecimal grandTotal = totalHarga.multiply(BigDecimal.valueOf(lamaSewa));
//
//        // 2. Buat entitas Penyewaan
//        Penyewaan penyewaan = new Penyewaan();
//        penyewaan.setPelanggan(pelanggan); // relasi @ManyToOne ke Penyewa
//        penyewaan.setTanggalPinjam(LocalDateTime.now());
//        penyewaan.setTotalHarga(grandTotal);
//        penyewaan.setStatusPembayaran("PENDING_PAYMENT");
//        penyewaan.setStatusPenyewaan("DISEWA");
//        penyewaan.setLamaSewa(lamaSewa);
//
//        // 3. Simpan penyewaan
//        penyewaanRepository.save(penyewaan);
//
//        // 4. Simpan detail penyewaan
//        List<Keranjang> daftarKeranjang = keranjangRepository.findAll();
//        for (Keranjang k : daftarKeranjang) {
//            PenyewaanDetail detail = new PenyewaanDetail();
//            detail.setPenyewaan(penyewaan);
//            detail.setBarang(k.getBarang());
//            detail.setJumlah(k.getJumlah());
//            detail.setHargaSatuan(k.getBarang().getHarga());
//            detail.setHargaSewa(
//            	    k.getBarang().getHarga().multiply(BigDecimal.valueOf(k.getJumlah()))
//			);
//            penyewaanDetailRepository.save(detail);
//
//            // Kurangi stok barang
//            Barang barang = k.getBarang();
//            barang.setJumlah(barang.getJumlah() - k.getJumlah());
//            barangRepository.save(barang);
//        }
//
//        // 5. Kosongkan keranjang
//        keranjangRepository.deleteAll();
//
//        return "redirect:/penyewaan/list";
//    }

	//now
//	@PostMapping("/keranjang/checkout")
//	public String checkout(@RequestParam("penyewaId") Long penyewaId, @RequestParam("totalHarga") BigDecimal totalHarga,
//			@RequestParam("lamaSewa") Integer lamaSewa, Model model) {
//
//		// 1. Ambil penyewa
//		Pelanggan pelanggan = pelangganRepository.findById(penyewaId)
//				.orElseThrow(() -> new RuntimeException("Penyewa not found"));
//
//		// total harga dikali lamaSewa
//		BigDecimal grandTotal = totalHarga.multiply(BigDecimal.valueOf(lamaSewa));
//
//		// 2. Buat entitas Penyewaan
//		Penyewaan penyewaan = new Penyewaan();
//		String penyewaanId = IdGenerator.generatePenyewaanId();
//		penyewaan.setPenyewaanId(penyewaanId);
//		penyewaan.setPelanggan(pelanggan); // relasi @ManyToOne ke Penyewa
//		penyewaan.setTanggalPinjam(LocalDateTime.now());
//		penyewaan.setTotalHarga(grandTotal);
//		penyewaan.setStatusPembayaran("PENDING_PAYMENT");
//		penyewaan.setStatusPenyewaan("DISEWA");
//		penyewaan.setLamaSewa(lamaSewa);
//
//		// 🟢 Tambahkan ini di sini:
//		penyewaan.setTanggalKembali(penyewaan.getTanggalPinjam().plusDays(lamaSewa)); //tambah hari
//
//		// 3. Simpan penyewaan
//		penyewaanRepository.save(penyewaan);
//
//		// 4. Simpan detail penyewaan
//		List<Keranjang> daftarKeranjang = keranjangRepository.findAll();
//		for (Keranjang k : daftarKeranjang) {
//			PenyewaanDetail detail = new PenyewaanDetail();
//			detail.setPenyewaan(penyewaan);
//			detail.setBarang(k.getBarang());
//			detail.setJumlah(k.getJumlah());
//			detail.setHargaSatuan(k.getBarang().getHarga());
//			detail.setHargaSewa(k.getBarang().getHarga().multiply(BigDecimal.valueOf(k.getJumlah())));
//			penyewaanDetailRepository.save(detail);
//
//			// Kurangi stok barang
//			Barang barang = k.getBarang();
//			barang.setJumlah(barang.getJumlah() - k.getJumlah());
//			barangRepository.save(barang);
//		}
//
//		// 5. Kosongkan keranjang
//		keranjangRepository.deleteAll();
//
//		return "redirect:/penyewaan/pembayaran/" + penyewaan.getPenyewaanId();
//
//	}
	
	@PostMapping("/keranjang/checkout")
	public String checkout(
	        @RequestParam("penyewaId") Long penyewaId,
	        @RequestParam("totalHarga") BigDecimal totalHarga,
	        @RequestParam("lamaSewa") Integer lamaSewa,
	        RedirectAttributes redirect) {

	    // 🔸 Simpan data sementara di flashAttributes
	    redirect.addFlashAttribute("penyewaId", penyewaId);
	    redirect.addFlashAttribute("totalHarga", totalHarga);
	    redirect.addFlashAttribute("lamaSewa", lamaSewa);

	    return "redirect:/penyewaan/pembayaran/temp";
	}


}
