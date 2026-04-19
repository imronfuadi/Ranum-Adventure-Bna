package com.ranum.adventure.bna.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ranum.adventure.bna.dto.PelangganDto;
import com.ranum.adventure.bna.dto.PenyewaanDto;
import com.ranum.adventure.bna.entities.Pengembalian;
import com.ranum.adventure.bna.entities.Penyewaan;
import com.ranum.adventure.bna.repository.PengembalianRepository;
import com.ranum.adventure.bna.repository.PenyewaanRepository;
import com.ranum.adventure.bna.service.PengembalianService;
import com.ranum.adventure.bna.service.impl.PengembalianServiceImpl;
import com.ranum.adventure.bna.service.impl.PenyewaanServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/pengembalian")
public class PengembalianController {

	@Autowired
	private PenyewaanRepository penyewaanRepository;

	@Autowired
	private PengembalianServiceImpl pengembalianServiceImpl;

	@Autowired
	private PengembalianRepository pengembalianRepository;

	@Autowired
	private PenyewaanServiceImpl penyewaanServiceImpl;

//	@GetMapping("/list")
//	public String findPengembalianList(Model model, @RequestParam(defaultValue = "0") String page) {
////	    int pageSize = 5; // Atur sesuai kebutuhan
////	    int currentPage = Integer.parseInt(page); // Konversi page ke int
////	    Page<Kategori> itemPage = kategoriServiceImpl.findPaginated(currentPage, pageSize);
////	    List<Kategori> kategoriList = itemPage.getContent();
////	    model.addAttribute("kategoriList", kategoriList);
////	    model.addAttribute("currentPage", currentPage);
////	    model.addAttribute("totalPages", itemPage.getTotalPages());
//	    return "/be-admin/pengembalian/pengembalian";
//	}

	@GetMapping(value = "/new")
	public String loadFormAdd(Model model) {
//		KategoriDto kategoriDto = new KategoriDto();
		model.addAttribute("sewaDto", new PenyewaanDto());
		return "be-admin/pengembalian/tambah-pengembalian";
	}

//	@GetMapping("/list")
//	public String listPenyewaanAktif(Model model) {
//		model.addAttribute("penyewaanList", penyewaanRepository.findAll());
//		return "be-admin/pengembalian/pengembalian";
//	}

	@GetMapping("/list")
	public String listPengembalian(Model model, @RequestParam(defaultValue = "0") String page) {
		int pageSize = 5; // Atur sesuai kebutuhan
		int currentPage = Integer.parseInt(page); // Konversi page ke int
		Page<Penyewaan> itemPage = penyewaanServiceImpl.findPaginated(currentPage, pageSize);
		List<Penyewaan> penyewaanList = itemPage.getContent();
		model.addAttribute("penyewaanList", penyewaanList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", itemPage.getTotalPages());
		return "/be-admin/pengembalian/pengembalian";
	}

	@GetMapping("/riwayat")
	public String riwayatPengembalian(Model model, @RequestParam(defaultValue = "0") String page) {
//		model.addAttribute("pengembalianList", pengembalianRepository.findAll());
		int pageSize = 5; // Atur sesuai kebutuhan
		int currentPage = Integer.parseInt(page); // Konversi page ke int
		Page<Pengembalian> itemPage = pengembalianServiceImpl.findPaginated(currentPage, pageSize);
		List<Pengembalian> pengembalianList = itemPage.getContent();
		model.addAttribute("pengembalianList", pengembalianList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", itemPage.getTotalPages());
		return "be-admin/pengembalian/riwayat-pengembalian";
	}

	@GetMapping("/detail/{id}")
	public String detailPengembalian(@PathVariable String id, Model model) {
		Penyewaan penyewaan = penyewaanRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Penyewaan not found"));
		model.addAttribute("penyewaan", penyewaan);
		return "be-admin/pengembalian/detail";
	}

	@PostMapping("/proses/{id}")
	public String prosesPengembalian(@PathVariable String id, @RequestParam(defaultValue = "1") int lamaPerjanjianHari,
			@RequestParam(defaultValue = "30000") BigDecimal dendaPerHari, RedirectAttributes redirect) {
		Penyewaan penyewaan = penyewaanRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Penyewaan not found"));

		pengembalianServiceImpl.prosesPengembalian(id, lamaPerjanjianHari, dendaPerHari);
		redirect.addFlashAttribute("success", "Pengembalian berhasil diproses!");
		return "redirect:/pengembalian/riwayat";
	}
	
	@GetMapping(value = "/form/{id}")
	public String loadFormUpdate(Model model, @PathVariable(name = "id") Long id) {
		Pengembalian pengembalian = pengembalianServiceImpl.findPengembalianById(id);
		model.addAttribute("pengembalian", pengembalian);
		return "be-admin/pengembalian/edit-pengembalian";
	}
	
	@PostMapping("/update/{id}")
	public String updateDendaPengembalian(@PathVariable Long id, @ModelAttribute Pengembalian pengembalian) {
	    pengembalianServiceImpl.saveDenda(id, pengembalian);
	    return "redirect:/pengembalian/riwayat";
	}

}
