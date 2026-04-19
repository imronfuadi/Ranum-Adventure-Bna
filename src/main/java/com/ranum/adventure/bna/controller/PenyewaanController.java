package com.ranum.adventure.bna.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

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
import com.ranum.adventure.bna.service.ReportService;
import com.ranum.adventure.bna.service.impl.BarangServiceImpl;
import com.ranum.adventure.bna.service.impl.PenyewaanServiceImpl;
import com.ranum.adventure.bna.util.FileUploadUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
@AllArgsConstructor
@RequestMapping("/penyewaan")
public class PenyewaanController {

	@Autowired
	private PenyewaanServiceImpl penyewaanServiceImpl;

	@Autowired
	private PenyewaanDetailRepository penyewaanDetailRepository;

	@Autowired
	private BarangServiceImpl barangServiceImpl;

	@Autowired
	private PenyewaanRepository penyewaanRepository;

	@Autowired
	private PelangganRepository pelangganRepository;

	@Autowired
	private KeranjangRepository keranjangRepository;

	@Autowired
	private BarangRepository barangRepository;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private ReportService reportService;

	@GetMapping("/list")
	public String findPenyewaanList(Model model, @RequestParam(defaultValue = "0") String page) {
		int pageSize = 5; // Atur sesuai kebutuhan
		int currentPage = Integer.parseInt(page); // Konversi page ke int
		Page<Penyewaan> itemPage = penyewaanServiceImpl.findPaginated(currentPage, pageSize);
		List<Penyewaan> penyewaanList = itemPage.getContent();
		model.addAttribute("penyewaanList", penyewaanList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", itemPage.getTotalPages());
		return "/be-admin/penyewaan/penyewaan";
	}

//	@GetMapping("/new")
//	public String findBarangList(Model model, @RequestParam(defaultValue = "0") String page) {
//		int pageSize = 5; // Atur sesuai kebutuhan
//		int currentPage = Integer.parseInt(page); // Konversi page ke int
//		Page<Barang> itemPage = barangServiceImpl.findPaginated(currentPage, pageSize);
//		List<Barang> barangList = itemPage.getContent();
//		model.addAttribute("barangList", barangList);
//		model.addAttribute("currentPage", currentPage);
//		model.addAttribute("totalPages", itemPage.getTotalPages());
//		return "/be-admin/penyewaan/tambah-penyewaan";
//	}

	@GetMapping("/new")
	public String findBarangList(@RequestParam(required = false) String error, Model model) {
//		int pageSize = 5; // Atur sesuai kebutuhan
//		int currentPage = Integer.parseInt(page); // Konversi page ke int
		List<Barang> barangList = barangServiceImpl.findAll();
//		List<Barang> barangList = itemPage.getContent();
		model.addAttribute("barangList", barangList);
//		model.addAttribute("currentPage", currentPage);
//		model.addAttribute("totalPages", itemPage.getTotalPages());
		System.out.println("Error param: " + error); // Debug
		return "/be-admin/penyewaan/tambah-penyewaan-test";
	}

	@GetMapping("/detail/{id}")
	public String detailPenyewaan(@PathVariable String id, Model model) {
		Penyewaan penyewaan = penyewaanRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Penyewaan tidak ditemukan"));

		model.addAttribute("penyewaan", penyewaan);
		model.addAttribute("detailList", penyewaan.getDetailList());
		return "be-admin/penyewaan/penyewaan-detail"; // file Thymeleaf
	}

//	@GetMapping("/pembayaran/{id}")
//    public String tampilPembayaran(@PathVariable String id, Model model) {
//        Penyewaan penyewaan = penyewaanServiceImpl.findById(id)
//                .orElseThrow(() -> new RuntimeException("Penyewaan tidak ditemukan"));
//
//        model.addAttribute("penyewaan", penyewaan);
//        return "be-admin/penyewaan/pembayaran"; // path ke pembayaran.html
//    }

	@GetMapping("/pembayaran/temp")
	public String tampilPembayaranSementara(Model model) {
		// Ambil dari flashAttribute
		if (!model.containsAttribute("penyewaId")) {
			return "redirect:/penyewaan/keranjang"; // fallback jika akses langsung
		}

		Long penyewaId = (Long) model.getAttribute("penyewaId");
		BigDecimal totalHarga = (BigDecimal) model.getAttribute("totalHarga");
		Integer lamaSewa = (Integer) model.getAttribute("lamaSewa");

		Pelanggan pelanggan = pelangganRepository.findById(penyewaId)
				.orElseThrow(() -> new RuntimeException("Pelanggan tidak ditemukan"));

		BigDecimal grandTotal = totalHarga.multiply(BigDecimal.valueOf(lamaSewa));

		model.addAttribute("pelanggan", pelanggan);
		model.addAttribute("totalHarga", totalHarga);
		model.addAttribute("lamaSewa", lamaSewa);
		model.addAttribute("grandTotal", grandTotal);
		model.addAttribute("keranjang", keranjangRepository.findAll());

		return "be-admin/penyewaan/pembayaran"; // konfirmasi pembayaran
	}

//
//    @PostMapping("/bayar")
//    public String prosesBayar(
//            @RequestParam String penyewaanId,
//            @RequestParam String metodePembayaran,
//            RedirectAttributes redirectAttributes) {
//
//        Penyewaan penyewaan = penyewaanServiceImpl.findById(penyewaanId)
//                .orElseThrow(() -> new RuntimeException("Penyewaan tidak ditemukan"));
//
//        BigDecimal totalHarga = penyewaan.getTotalHarga();
////        BigDecimal kembalian = jumlahBayar.subtract(totalHarga);
//
////        if (kembalian.compareTo(BigDecimal.ZERO) < 0) {
////            redirectAttributes.addFlashAttribute("error", "Jumlah uang kurang dari total harga!");
////            return "redirect:/penyewaan/pembayaran/" + penyewaanId;
////        }
//
//        // Simpan status penyewaan dan pembayaran
//        penyewaanServiceImpl.prosesPembayaran(penyewaan, metodePembayaran);
//
////        redirectAttributes.addFlashAttribute("success", "Pembayaran berhasil. Kembalian: Rp. " +
////                kembalian.setScale(0, RoundingMode.HALF_UP));
//        redirectAttributes.addFlashAttribute("success", "Pembayaran berhasil");
//        return "redirect:/penyewaan/list"; // redirect ke list penyewaan
//    }

//	@PostMapping("/bayar")
//	public String prosesBayar(
//	        @RequestParam Long penyewaId,
//	        @RequestParam BigDecimal totalHarga,
//	        @RequestParam Integer lamaSewa,
//	        @RequestParam String metodePembayaran,
//	        RedirectAttributes redirect) {
//
//	    Pelanggan pelanggan = pelangganRepository.findById(penyewaId)
//	            .orElseThrow(() -> new RuntimeException("Pelanggan tidak ditemukan"));
//
//	    BigDecimal grandTotal = totalHarga.multiply(BigDecimal.valueOf(lamaSewa));
//
//	    // Buat dan simpan penyewaan
//	    Penyewaan penyewaan = new Penyewaan();
//	    String penyId = IdGenerator.generatePenyewaanId();
//	    penyewaan.setPenyewaanId(penyId);
//	    penyewaan.setPelanggan(pelanggan);
//	    penyewaan.setTanggalPinjam(LocalDateTime.now());
//	    penyewaan.setTotalHarga(grandTotal);
//	    penyewaan.setLamaSewa(lamaSewa);
//	    penyewaan.setTanggalKembali(penyewaan.getTanggalPinjam().plusDays(lamaSewa));
//	    penyewaan.setStatusPembayaran("LUNAS");
//	    penyewaan.setStatusPenyewaan("DISEWA");
//	    penyewaan.setMetodeBayar(metodePembayaran);
//
//	    penyewaanRepository.save(penyewaan);
//
//	    // Simpan detail dari keranjang
//	    List<Keranjang> daftarKeranjang = keranjangRepository.findAll();
//	    for (Keranjang k : daftarKeranjang) {
//	        PenyewaanDetail detail = new PenyewaanDetail();
//	        detail.setPenyewaan(penyewaan);
//	        detail.setBarang(k.getBarang());
//	        detail.setJumlah(k.getJumlah());
//	        detail.setHargaSatuan(k.getBarang().getHarga());
//	        detail.setHargaSewa(k.getBarang().getHarga().multiply(BigDecimal.valueOf(k.getJumlah())));
//	        penyewaanDetailRepository.save(detail);
//
//	        // Kurangi stok
//	        Barang barang = k.getBarang();
//	        barang.setJumlah(barang.getJumlah() - k.getJumlah());
//	        barangRepository.save(barang);
//	    }
//
//	    keranjangRepository.deleteAll();
//
//	    redirect.addFlashAttribute("success", "Pembayaran berhasil");
//	    return "redirect:/penyewaan/list";
//	}

	@PostMapping("/bayar")
	public String prosesBayar(@RequestParam Long penyewaId, @RequestParam BigDecimal totalHarga,
			@RequestParam Integer lamaSewa, @RequestParam String metodePembayaran,
			@RequestParam(value = "file", required = false) MultipartFile file, RedirectAttributes redirectAttributes) {

		Pelanggan pelanggan = pelangganRepository.findById(penyewaId)
				.orElseThrow(() -> new RuntimeException("Pelanggan tidak ditemukan"));

		BigDecimal grandTotal = totalHarga.multiply(BigDecimal.valueOf(lamaSewa));

		// Buat penyewaan
		Penyewaan penyewaan = new Penyewaan();
		penyewaan.setPenyewaanId(IdGenerator.generatePenyewaanId());
		penyewaan.setPelanggan(pelanggan);
		penyewaan.setTanggalPinjam(LocalDateTime.now());
		penyewaan.setTotalHarga(grandTotal);
		penyewaan.setLamaSewa(lamaSewa);
		penyewaan.setTanggalKembali(LocalDateTime.now().plusDays(lamaSewa));
		penyewaan.setStatusPembayaran("LUNAS");
		penyewaan.setStatusPenyewaan("DISEWA");
		penyewaan.setMetodeBayar(metodePembayaran);

		// ✅ Jika metode transfer dan file tidak kosong, simpan file
//		if ("Transfer".equalsIgnoreCase(metodePembayaran) && file != null && !file.isEmpty()) {
//			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//			String contentType = file.getContentType();
//
//			if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
//				throw new IllegalArgumentException("Hanya file JPEG dan PNG yang diperbolehkan.");
//			}
//			String uploadDir = "src/main/resources/static/assets/images/bukti-transfer/";
//			try {
//				FileUploadUtil.saveFile(uploadDir, fileName, file);
//			} catch (IOException e) {
//				throw new RuntimeException("Gagal menyimpan file: " + e.getMessage(), e);
//			}
//			penyewaan.setBuktiTransfer(fileName);
//
//		} else {
//			redirectAttributes.addFlashAttribute("error", "Gagal menyimpan file bukti transfer!");
//			return "redirect:/penyewaan/pembayaran/temp";
//		}
		if ("Transfer".equalsIgnoreCase(metodePembayaran)) {
		    // Jika transfer, pastikan ada file dan validasinya
		    if (file != null && !file.isEmpty()) {
		        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		        String contentType = file.getContentType();

		        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
		            throw new IllegalArgumentException("Hanya file JPEG dan PNG yang diperbolehkan.");
		        }

		        String uploadDir = "src/main/resources/static/assets/images/bukti-transfer/";
		        try {
		            FileUploadUtil.saveFile(uploadDir, fileName, file);
		        } catch (IOException e) {
		            throw new RuntimeException("Gagal menyimpan file: " + e.getMessage(), e);
		        }
		        penyewaan.setBuktiTransfer(fileName);
		    } else {
		        redirectAttributes.addFlashAttribute("error", "File bukti transfer wajib diupload untuk metode transfer.");
		        return "redirect:/penyewaan/pembayaran/temp";
		    }
		}


		// Simpan penyewaan
		penyewaanRepository.save(penyewaan);

		// Simpan detail penyewaan dari keranjang
		List<Keranjang> daftarKeranjang = keranjangRepository.findAll();
		for (Keranjang k : daftarKeranjang) {
			PenyewaanDetail detail = new PenyewaanDetail();
			detail.setPenyewaan(penyewaan);
			detail.setBarang(k.getBarang());
			detail.setJumlah(k.getJumlah());
			detail.setHargaSatuan(k.getBarang().getHarga());
			detail.setHargaSewa(k.getBarang().getHarga().multiply(BigDecimal.valueOf(k.getJumlah())));
			penyewaanDetailRepository.save(detail);

			// Kurangi stok
			Barang barang = k.getBarang();
			barang.setJumlah(barang.getJumlah() - k.getJumlah());
			barangRepository.save(barang);
		}

		keranjangRepository.deleteAll();

		redirectAttributes.addFlashAttribute("success", "Pembayaran berhasil!");
		return "redirect:/penyewaan/list";
	}

	@GetMapping("/nota/{id}")
	public void getNota(HttpServletResponse response, @PathVariable("id") String id) throws Exception {
		// Generate the report
		JasperPrint jasperPrint = reportService.generateJasperPrint(id);

		// Retrieve additional data from the report or database
		String userName = reportService.getUserNameById(id); // Assume this method exists

		// Create a dynamic file name based on the ID and user name
		String fileName = "Nota_Ranum_Adventure_" + userName + "_" + id + ".pdf";

		// Set the content type and disposition header for the response
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		// Export the report to the output stream
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}

	@GetMapping("/delete/{id}")
	public String deletePenyewaan(@PathVariable String id, Model model) {
//        Penyewaan penyewaan = penyewaanServiceImpl.findById(id)
//                .orElseThrow(() -> new RuntimeException("Penyewaan tidak ditemukan"));

//        model.addAttribute("penyewaan", penyewaan);
		this.penyewaanRepository.deleteById(id);
		return "redirect:/penyewaan/list"; // path ke pembayaran.html
	}

	@GetMapping("/batal/{id}")
	public String penyewaanBatal(@PathVariable String id, Model model, RedirectAttributes redirect) {
		Penyewaan penyewaan = penyewaanServiceImpl.findById(id)
				.orElseThrow(() -> new RuntimeException("Penyewaan tidak ditemukan"));
		penyewaan.setStatusPenyewaan("DIBATALKAN");
		// update stok
		penyewaan.getDetailList().forEach(detail -> {
			Barang barang = detail.getBarang();
			barang.setJumlah(barang.getJumlah() + detail.getJumlah());
			barangRepository.save(barang);
		});
		penyewaanRepository.save(penyewaan);
//		redirect.addFlashAttribute("dibatalkan", "Pengembalian berhasil diproses!");
		return "redirect:/penyewaan/list?success=dibatalkan";
	}

//    @GetMapping("/nota/{id}")
//    public void generateNotaPDF(@PathVariable Long id, HttpServletResponse response) throws Exception {
//        Penyewaan penyewaan = penyewaanRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Penyewaan tidak ditemukan"));
//
//        // ambil detail
////        var penyewaanDetail = penyewaanDetailRepository.findByPenyewaan(penyewaan);
//        List<PenyewaanDetail> penyewaanDetail = penyewaanDetailRepository.findByPenyewaan(penyewaan);
//
//        // isi model untuk Thymeleaf
//        Context context = new Context();
//        context.setVariable("penyewaan", penyewaan);
//        context.setVariable("penyewaanDetail", penyewaanDetail);
//
//     // Ini benar — memakai variable instance templateEngine
//        String html = templateEngine.process("be-admin/penyewaan/nota", context); 
//
//
//        // Set up response
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "inline; filename=Nota_Penyewaan_" + id + ".pdf");
//
//        OutputStream out = response.getOutputStream();
//
//        // Convert HTML ke PDF
////        ITextRenderer renderer = new ITextRenderer();
////        renderer.setDocumentFromString(html);
////        renderer.layout();
////        renderer.createPDF(out);
//
//        out.close();
//    }

//	@GetMapping(value = "/detail/{id}")
//	public String detailPenyewaan(Model model, @PathVariable(name = "id") Long id) {
//		//BarangDto barangDto = barangServiceImpl.findBarangById(barangId);
////		List<PenyewaanDetail> dto = kategoriServiceImpl.findAllKategori();
////		model.addAttribute("kategoriList", dto);
////		model.addAttribute("barangDto", barangDto);
//		return "be-admin/barang/edit-barang";
//	}

//	@GetMapping(value = "/keranjang")
//	public String loadFormAdd(Model model) {
////		KategoriDto kategoriDto = new KategoriDto();
////		model.addAttribute("sewaDto", new PenyewaanDto());
//		return "be-admin/penyewaan/keranjang";
//	}
}
