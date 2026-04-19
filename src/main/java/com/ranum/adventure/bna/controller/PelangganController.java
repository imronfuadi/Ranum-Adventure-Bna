package com.ranum.adventure.bna.controller;

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
import com.ranum.adventure.bna.entities.Pelanggan;
import com.ranum.adventure.bna.service.impl.PelangganServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/pelanggan")
public class PelangganController {
	
	@Autowired
	private PelangganServiceImpl pelangganServiceImpl;

	@GetMapping("/list")
	public String findPelangganList(Model model, @RequestParam(defaultValue = "0") String page) {
		int pageSize = 5; // Atur sesuai kebutuhan
		int currentPage = Integer.parseInt(page); // Konversi page ke int
		Page<Pelanggan> itemPage = pelangganServiceImpl.findPaginated(currentPage, pageSize);
		List<Pelanggan> pelangganList = itemPage.getContent();
		model.addAttribute("pelangganList", pelangganList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", itemPage.getTotalPages());
		return "/be-admin/pelanggan/pelanggan";
	}

	@GetMapping(value = "/new")
	public String loadFormAdd(Model model) {
//		BarangDto BarangDto = new BarangDto();
//		List<UserDto> dto = userServiceImpl.findAllUser();
		model.addAttribute("userDto", new PelangganDto());
		return "be-admin/pelanggan/tambah-pelanggan";
	}

	@PostMapping(value = "/save")
	public String savePelanggan(@ModelAttribute(value = "PelangganDto") PelangganDto dto,
			RedirectAttributes redirectAttributes, MultipartFile foto) {
		Pelanggan savePelanggan = pelangganServiceImpl.savePelanggan(dto, foto);
		return "redirect:/pelanggan/list";
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

	@GetMapping(value = "/form/{pelangganId}")
	public String loadFormUpdate(Model model, @PathVariable(name = "pelangganId") Long pelangganId) {
		PelangganDto pelangganDto = pelangganServiceImpl.findPelangganById(pelangganId);
		model.addAttribute("pelangganDto", pelangganDto);
		return "be-admin/pelanggan/edit-pelanggan";
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
	
	@PostMapping(value = "/update/{pelangganId}")
	private String updateDataPelanggan(@PathVariable Long pelangganId, Model model,
			@ModelAttribute(value = "pelangganDto") PelangganDto pelangganDto, BindingResult result,
			MultipartFile foto,
	        RedirectAttributes redirectAttributes) {
	    try {
	        // Cek apakah file baru diunggah atau tidak
	        if (foto != null && !foto.isEmpty()) {
	            // Jika ada file baru, lakukan update termasuk file foto
	            pelangganServiceImpl.updatePelanggan(pelangganId, pelangganDto, foto);
	        } else {
	            // Jika tidak ada file baru, lakukan update tanpa mengubah foto
	            pelangganDto.setFoto(pelangganDto.getFoto()); // Tetap gunakan foto lama
	            pelangganServiceImpl.updatePelanggan(pelangganId, pelangganDto, null);
	        }
	        return "redirect:/pelanggan/list";
	    } catch (EntityNotFoundException ex) { 
	        redirectAttributes.addFlashAttribute("error", ex.getMessage());
	        return "redirect:/pelanggan/new";
	    } catch (Exception ex) {
	        redirectAttributes.addFlashAttribute("error", "Unexpected error: " + ex.getMessage());
	        return "redirect:/pelanggan/new";
	    }
	}


	@GetMapping(value = "/delete/{pelangganId}")
	private String deletePelanggan(@PathVariable(value = "pelangganId") Long pelangganId) {
		this.pelangganServiceImpl.deletePelanggan(pelangganId);
		return "redirect:/pelanggan/list";
	}
}
