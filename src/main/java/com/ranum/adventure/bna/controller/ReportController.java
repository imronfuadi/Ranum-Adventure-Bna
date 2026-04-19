package com.ranum.adventure.bna.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ranum.adventure.bna.service.ReportService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
@RequestMapping("/reports")
public class ReportController {

//	@Autowired
//	private ReportService reportService;
//	
//	@Autowired
//	private HttpServletResponse response;
//	
//	@GetMapping("/testimonials")
//	public void getTestimonialReport() throws Exception{
//		response.setContentType("application/pdf");
//		response.setHeader("Content-Disposition", "attachment; filename:\"testimonial_list.pdf\"");
//		JasperPrint jasperPrint = reportService.generateJasperPrint();
//		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
//	}
	
	@Autowired
    private ReportService reportService;

    @GetMapping("/testimonials")
    public void getTestimonialReport(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"testimonial_list.pdf\"");
        
//        JasperPrint jasperPrint = reportService.generateJasperPrint();
//        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }
}
