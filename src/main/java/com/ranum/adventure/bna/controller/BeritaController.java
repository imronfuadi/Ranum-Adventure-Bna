package com.ranum.adventure.bna.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ranum.adventure.bna.entities.Berita;
import com.ranum.adventure.bna.service.impl.BeritaServiceImpl;
import com.ranum.adventure.bna.util.FileUploadUtil;

import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/berita")
@AllArgsConstructor
public class BeritaController {

	@Autowired
	private BeritaServiceImpl beritaServiceImpl;

	@Autowired
	private Validator validator;

	@GetMapping("/list")
	public String showBerita(Model model) {
		List<Berita> berita = beritaServiceImpl.findAllBerita();
		model.addAttribute("berita", berita);
		return "be-admin/berita/berita";
	}

	@GetMapping("/new")
	public String loadFormBerita(Model model) {
		Berita berita = new Berita();
		model.addAttribute("addNewBerita", berita);
		return "/be-admin/berita/addNewBerita";
	}

	@PostMapping(value = "/submit")
	public String saveBerita(@Valid @ModelAttribute(value = "addNewBerita") Berita berita, BindingResult result,
			@RequestParam("image") @NotNull MultipartFile multipartFile) {
		if (result.hasErrors()) {
			return "/be-admin/berita/addNewBerita"; // Return the form page with errors
		} else {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			berita.setPictBerita(fileName);
			Berita saveBerita = beritaServiceImpl.saveBerita(berita);
			String uploadDir = "src/main/resources/static/assets/images/berita/";
			try {
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "redirect:/berita/list";
		}
	}

	@GetMapping(value = "/form/{id}")
	public String loadFormUpdate(Model model, @PathVariable(name = "id") Long id) {
		Berita berita = beritaServiceImpl.findBeritaById(id)
				.orElseThrow(() -> new IllegalArgumentException("Gagal mendapatkan id: " + id));
		model.addAttribute("getEditBerita", berita);
		return "/be-admin/berita/edit-berita";
	}

	@PostMapping(value = "/update/{id}")
	public String updateDataBerita(Model model, @ModelAttribute(value = "berita") Berita berita,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		berita.setPictBerita(fileName);
		Berita saveBerita = beritaServiceImpl.saveBerita(berita);
		String uploadDir = "src/main/resources/static/assets/images/berita/";
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		return "redirect:/berita/list";
	}

	@GetMapping(value = "/delete/{id}")
	public String deleteSlider(@PathVariable(value = "id") Long id) {
		this.beritaServiceImpl.deleteBerita(id);
		return "redirect:/berita/list";
	}

	@GetMapping("/detail/{id}")
	public String beritaDetail(Model model, @PathVariable(name = "id") Long id) {
		Berita berita = beritaServiceImpl.findBeritaById(id)
				.orElseThrow(() -> new IllegalArgumentException("Gagal mendapatkan id: " + id));
		List<Berita> beritaTerbaru = beritaServiceImpl.getBeritaTerbaru();
		model.addAttribute("getBeritaDetail", berita);
		model.addAttribute("beritaTerbaru", beritaTerbaru);
		return "/fe/berita-detail";
	}
}
