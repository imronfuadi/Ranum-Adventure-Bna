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

import com.ranum.adventure.bna.dto.TestimonialsDto;
import com.ranum.adventure.bna.service.TestimonialsService;
import com.ranum.adventure.bna.util.FileUploadUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Controller
@RequestMapping("/testimonials")
public class TestimonialsController {

	@Autowired
	private TestimonialsService testimonialService;

	@GetMapping("/list")
	public String getAllTestimonials(Model model) {
		List<TestimonialsDto> dto = testimonialService.getAllTestimonial();
		model.addAttribute("testimonials", dto);
		return "/be-admin/testimonials/testimonials";
	}

	@GetMapping("/new")
	public String createTestimonialForm(Model model) {
		model.addAttribute("testiDto", new TestimonialsDto());
		return "/be-admin/testimonials/add-testimonials";
	}

	@PostMapping("/new")
	public String createTestimonial(@Valid @ModelAttribute("testiDto") TestimonialsDto dto, BindingResult result,
			@RequestParam("image") @NotNull MultipartFile multipartFile) {
		if (result.hasErrors()) {
			return "/be-admin/testimonials/add-testimonials";
		} else {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			dto.setPictTestimonial(fileName);
			testimonialService.createTestimonial(dto);
			String uploadDir = "src/main/resources/static/assets/images/testimonials/";
			try {
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "redirect:/testimonials/list";
		}

	}

	@GetMapping("/edit/{testimonialId}")
	public String updateTestimonialForm(@PathVariable Long testimonialId, Model model) {
		TestimonialsDto dto = testimonialService.getTestimonialById(testimonialId);
		model.addAttribute("testiDto", dto);
		return "/be-admin/testimonials/edit-testimonials";
	}

	@PostMapping("/edit/{testimonialId}")
	public String updateTestimonial(@PathVariable Long testimonialId, @ModelAttribute("testiDto") TestimonialsDto dto,
			BindingResult result, @RequestParam("image") @NotNull MultipartFile multipartFile) {
		if (result.hasErrors()) {
			return "/be-admin/testimonials/add-testimonials";
		} else {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			dto.setPictTestimonial(fileName);
			testimonialService.updateTestimonial(testimonialId, dto);
			String uploadDir = "src/main/resources/static/assets/images/testimonials/";
			try {
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "redirect:/testimonials/list";
		}
		
		
	}

	@GetMapping("/delete/{testimonialId}")
	public String deleteTestimonial(@PathVariable Long testimonialId) {
		testimonialService.deleteTestimonial(testimonialId);
		return "redirect:/testimonials/list";
	}
}
