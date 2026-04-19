package com.ranum.adventure.bna.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/superadmin")
public class SuperAdminController {

	@GetMapping("/dashboard")
	public String getDashboard() {
		return "/be-superadmin/dashboard/dashboard-ranum-adventure";
	}
}
