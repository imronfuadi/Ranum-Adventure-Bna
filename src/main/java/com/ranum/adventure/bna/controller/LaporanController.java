package com.ranum.adventure.bna.controller;

import java.util.List;

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

import com.ranum.adventure.bna.dto.KategoriDto;
import com.ranum.adventure.bna.entities.Kategori;
import com.ranum.adventure.bna.entities.Penyewaan;
import com.ranum.adventure.bna.service.impl.KategoriServiceImpl;
import com.ranum.adventure.bna.service.impl.LaporanServiceImpl;
import com.ranum.adventure.bna.service.impl.PenyewaanServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
@AllArgsConstructor
@RequestMapping("/laporan")
public class LaporanController {

	private final PenyewaanServiceImpl penyewaanServiceImpl;

	private final LaporanServiceImpl laporanServiceImpl;

//	@GetMapping(value = "/list")
//	public String findkategoriList(Model model) {
//		List<KategoriDto> kategoriList = kategoriServiceImpl.findAllKategori();
//		model.addAttribute("kategoriList", kategoriList);
//		return "be-admin/kategori/kategori";
//	}

	@GetMapping("/list")
	public String findLaporanList(Model model, @RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(defaultValue = "0") String page) {
//		int pageSize = 10; // Atur sesuai kebutuhan
//		int currentPage = Integer.parseInt(page); // Konversi page ke int
//		Page<Penyewaan> itemPage = laporanServiceImpl.findPaginated(currentPage, pageSize);
//		List<Penyewaan> laporanList = itemPage.getContent();
		int pageSize = 10;
	    int currentPage = Integer.parseInt(page);
	    Page<Penyewaan> itemPage;

	    if (keyword != null && !keyword.trim().isEmpty()) {
	        // Jika ada keyword, pakai pencarian
	        itemPage = laporanServiceImpl.searchPaginated(keyword, currentPage, pageSize);
	    } else {
	        // Jika tidak ada, tampilkan semua
	        itemPage = laporanServiceImpl.findPaginated(currentPage, pageSize);
	    }
	    
	    List<Penyewaan> laporanList = itemPage.getContent();
	    
		model.addAttribute("laporanList", laporanList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", itemPage.getTotalPages());
		model.addAttribute("keyword", keyword);
		return "/be-admin/laporan/laporan";
	}
	
	@GetMapping("/download")
    public void getTestimonialReport(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"laporan-penyewaan.pdf\"");
        
        JasperPrint jasperPrint = laporanServiceImpl.downloadLaporan();
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

//	@GetMapping(value = "/new")
//	public String loadFormAdd(Model model) {
////		KategoriDto kategoriDto = new KategoriDto();
//		model.addAttribute("kategoriDto", new KategoriDto());
//		return "be-admin/kategori/tambah-kategori";
//	}
//	
//	@PostMapping(value = "/save")
//	public String saveKategori(@ModelAttribute(value = "kategoriDto") KategoriDto dto){
//        Kategori savekategori = kategoriServiceImpl.saveKategori(dto);
//		return "redirect:/kategori/list";
//	}
//	
//	@GetMapping(value = "/form/{kategoriId}")
//	public String loadFormUpdate(Model model, @PathVariable(name = "kategoriId") Long kategoriId) {
//		KategoriDto kategoriDto = kategoriServiceImpl.findKategoriById(kategoriId);
//		model.addAttribute("kategoriDto", kategoriDto);
//		return "be-admin/kategori/edit-kategori";
//	}
//	
//	@PostMapping(value = "/update/{kategoriId}")
//	private String updateDatakategori(@PathVariable Long kategoriId,Model model, @ModelAttribute(value = "kategoriDto") KategoriDto kategoriDto, BindingResult result) {
//		kategoriServiceImpl.updateKategori(kategoriId, kategoriDto);
//		return "redirect:/kategori/list";
//	}
//	
//	@GetMapping(value = "/delete/{kategoriId}")
//	private String deletekategori(@PathVariable(value = "kategoriId") Long kategoriId) {
//		this.kategoriServiceImpl.deleteKategori(kategoriId);
//		return "redirect:/kategori/list";
//	}
}
