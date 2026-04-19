package com.ranum.adventure.bna.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ranum.adventure.bna.entities.Kontak;
import com.ranum.adventure.bna.service.impl.KontakServiceImpl;

@Controller
@RequestMapping("/kontak")
public class KontakController {

	@Autowired
	private KontakServiceImpl kontakServiceImpl;

	@GetMapping("/list")
	public String index(Model model) {
		List<Kontak> kontak = kontakServiceImpl.findAllDataKontak();
		model.addAttribute("kontaks", kontak);
		return "/be-admin/kontak/kontak";
	}
	
	@GetMapping("/new")
	public String form(Model model) {
		Kontak kontak = new Kontak();
		model.addAttribute("addNewKontak", kontak);
		return "/be-admin/kontak/addNewKontak";
	}
	
	@PostMapping("/submit")
	public String saveKontak(Kontak kontak) {
		Kontak saveKontak = kontakServiceImpl.saveKontak(kontak);
		return "redirect:/";
	}
}
