package com.ranum.adventure.bna.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import com.ranum.adventure.bna.dto.BarangDto;
import com.ranum.adventure.bna.dto.KategoriDto;
import com.ranum.adventure.bna.entities.Barang;
import com.ranum.adventure.bna.service.impl.BarangServiceImpl;
import com.ranum.adventure.bna.service.impl.KategoriServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/barang")
public class BarangController {

	@Autowired
	private BarangServiceImpl barangServiceImpl;

	@Autowired
	private KategoriServiceImpl kategoriServiceImpl;

	@GetMapping("/list")
	public String findBarangList(Model model, @RequestParam(defaultValue = "0") String page) {
		int pageSize = 5; // Atur sesuai kebutuhan
		int currentPage = Integer.parseInt(page); // Konversi page ke int
		Page<Barang> itemPage = barangServiceImpl.findPaginated(currentPage, pageSize);
		List<Barang> barangList = itemPage.getContent();
		model.addAttribute("barangList", barangList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", itemPage.getTotalPages());
		return "/be-admin/barang/barang";
	}

	@GetMapping(value = "/new")
	public String loadFormAdd(Model model) {
//		BarangDto BarangDto = new BarangDto();
		List<KategoriDto> dto = kategoriServiceImpl.findAllKategori();
		model.addAttribute("kategoriList", dto);
		model.addAttribute("barangDto", new BarangDto());
		return "be-admin/barang/tambah-barang";
	}

	@PostMapping(value = "/save")
	public String saveBarang(@ModelAttribute(value = "BarangDto") BarangDto dto,
			RedirectAttributes redirectAttributes, MultipartFile foto) {
		Barang saveBarang = barangServiceImpl.saveBarang(dto, foto);
		return "redirect:/barang/list";
//		
//		try {
//			if (dto.getFoto() != null && !dto.getFoto().isEmpty()) {
//				barangServiceImpl.updateBiodataWithPhoto(biodata, bcrDto.getFoto());
//			} else {
//				barangServiceImpl.saveBarang(dto);
//			}
//			return "redirect:/barang/list";
//		} catch (IOException ex) {
//			redirectAttributes.addFlashAttribute("error", "Failed to save photo: " + ex.getMessage());
//			return "redirect:/biodata/form";
//		} catch (EntityNotFoundException ex) { // Perbaikan: Menangani EntityNotFoundException
//			redirectAttributes.addFlashAttribute("error", ex.getMessage());
//			return "redirect:/biodata/form";
//		} catch (Exception ex) { // Perbaikan: Menangani semua pengecualian lainnya
//			redirectAttributes.addFlashAttribute("error", "Unexpected error: " + ex.getMessage());
//			return "redirect:/biodata/form";
//		}
	}

	@GetMapping(value = "/form/{barangId}")
	public String loadFormUpdate(Model model, @PathVariable(name = "barangId") Long barangId) {
		BarangDto barangDto = barangServiceImpl.findBarangById(barangId);
		List<KategoriDto> dto = kategoriServiceImpl.findAllKategori();
		model.addAttribute("kategoriList", dto);
		model.addAttribute("barangDto", barangDto);
		return "be-admin/barang/edit-barang";
	}

//	@PostMapping(value = "/update/{barangId}")
//	private String updateDataBarang(@PathVariable Long barangId, Model model,
//			@ModelAttribute(value = "barangDto") BarangDto barangDto, BindingResult result, MultipartFile file,
//			RedirectAttributes redirectAttributes) {
//		try {
//			if (barangDto.getFoto() != null && !barangDto.getFoto().isEmpty()) {
//				barangServiceImpl.updateBarang(barangId, barangDto, file);
//			} else {
//				barangServiceImpl.saveBarang(barangDto, file);
//			}
//			return "redirect:/barang/list";
//		} catch (EntityNotFoundException ex) { // Perbaikan: Menangani EntityNotFoundException
//			redirectAttributes.addFlashAttribute("error", ex.getMessage());
//			return "redirect:/barang/new";
//		} catch (Exception ex) { // Perbaikan: Menangani semua pengecualian lainnya
//			redirectAttributes.addFlashAttribute("error", "Unexpected error: " + ex.getMessage());
//			return "redirect:/barang/new";
//		}
////		barangServiceImpl.updateBarang(barangId, barangDto, file);
////		return "redirect:/barang/list";
//	}
	
	@PostMapping(value = "/update/{barangId}")
	private String updateDataBarang(@PathVariable Long barangId, Model model,
	        @ModelAttribute(value = "barangDto") BarangDto barangDto, BindingResult result, MultipartFile foto,
	        RedirectAttributes redirectAttributes) {
	    try {
	        // Cek apakah file baru diunggah atau tidak
	        if (foto != null && !foto.isEmpty()) {
	            // Jika ada file baru, lakukan update termasuk file foto
	            barangServiceImpl.updateBarang(barangId, barangDto, foto);
	        } else {
	            // Jika tidak ada file baru, lakukan update tanpa mengubah foto
	            barangDto.setFoto(barangDto.getFoto()); // Tetap gunakan foto lama
	            barangServiceImpl.updateBarang(barangId, barangDto, null);
	        }
	        return "redirect:/barang/list";
	    } catch (EntityNotFoundException ex) { 
	        redirectAttributes.addFlashAttribute("error", ex.getMessage());
	        return "redirect:/barang/new";
	    } catch (Exception ex) {
	        redirectAttributes.addFlashAttribute("error", "Unexpected error: " + ex.getMessage());
	        return "redirect:/barang/new";
	    }
	}


//	@GetMapping(value = "/delete/{barangId}")
//	private String deleteBarang(@PathVariable(value = "barangId") Long barangId) {
//		this.barangServiceImpl.deleteBarang(barangId);
//		return "redirect:/barang/list";
//	}
	
	@GetMapping("/delete/{barangId}")
	public String deleteBarang(@PathVariable Long barangId, RedirectAttributes redirect) {
	    try {
	        barangServiceImpl.deleteBarang(barangId); // atau barangRepository.deleteById(id);
	        redirect.addFlashAttribute("success", "Barang berhasil dihapus.");
	    } catch (DataIntegrityViolationException ex) {
	        redirect.addFlashAttribute("error", "Tidak dapat menghapus barang karena masih digunakan dalam penyewaan.");
	    } catch (Exception ex) {
	        redirect.addFlashAttribute("error", "Terjadi kesalahan internal saat menghapus barang.");
	    }
	    return "redirect:/barang/list";
	}
}
