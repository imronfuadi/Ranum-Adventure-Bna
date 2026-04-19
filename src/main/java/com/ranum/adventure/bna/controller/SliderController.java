package com.ranum.adventure.bna.controller;

import java.io.IOException;

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

import com.ranum.adventure.bna.entities.Slider;
import com.ranum.adventure.bna.service.impl.SliderServiceImpl;
import com.ranum.adventure.bna.util.FileUploadUtil;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/slider")
public class SliderController {

	@Autowired
	private final SliderServiceImpl sliderServiceImpl;
	
	@GetMapping(value = "/list")
	public String findSliderList(Model model) {
		model.addAttribute("sliders", sliderServiceImpl.findAllSlider());
		return "be-admin/slider/slider";
	}
	
	@GetMapping(value = "/new")
	public String loadFormAdd(Model model) {
		Slider slider = new Slider();
		model.addAttribute("addNewSlider", slider);
		return "be-admin/slider/addNewSlider";
	}
	
	@PostMapping(value = "/submit")
	public String saveSlider(@ModelAttribute(value = "slider") Slider slider, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        slider.setPictSlider(fileName);
        Slider saveSlider = sliderServiceImpl.saveSlider(slider);
        String uploadDir = "src/main/resources/static/assets/images/slider/";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		return "redirect:/slider/list";
	}
	
	@GetMapping(value = "/form/{id}")
	public String loadFormUpdate(Model model, @PathVariable(name = "id") Long id) {
		Slider slider= sliderServiceImpl.updateSlider(id)
				.orElseThrow(() -> new IllegalArgumentException("Gagal mendapatkan id: " + id));
		model.addAttribute("getEditSlider", slider);
		return "";
	}
	
	@PostMapping(value = "/update/{id}")
	private String updateDataSlider(Model model, @ModelAttribute(value = "slider") Slider slider, BindingResult result) {
		model.addAttribute("updateProduct", slider);
		sliderServiceImpl.saveSlider(slider);
		return "";
	}
	
	@GetMapping(value = "/delete/{id}")
	private String deleteSlider(@PathVariable(value = "id") Long id) {
		this.sliderServiceImpl.deleteSlider(id);
		return "redirect:/slider/list";
	}
}
