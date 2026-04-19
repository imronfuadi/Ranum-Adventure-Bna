package com.ranum.adventure.bna.controller;

//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ranum.adventure.bna.dto.ChangePasswordUserDto;
import com.ranum.adventure.bna.entities.User;
import com.ranum.adventure.bna.service.impl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserDashboardController {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/home")
	public String GetDashboardIndex() {
		return "/be-user/dashboard/dashboard";
	}

	@GetMapping("/editPassword")
	public String editData(Model model, HttpSession session) {
		// Ambil ID pengguna dari sesi
//	    Long userId = (Long) session.getAttribute("userId");

		// Gunakan ID pengguna untuk mengambil data yang sesuai dari database
//	    Long user = userService.getUserById(userId);

		// Kirim data ke halaman edit
//	    model.addAttribute("user", user);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		User currentUser = userServiceImpl.findByEmail(currentUserName);
		model.addAttribute("changeUser", currentUser);

		return "/be-user/user/change-password";
	}

	// change password tanpa dto
	@PostMapping("/changePassword")
	public String changePassword(@Valid @ModelAttribute("changeUser") User user, BindingResult result,
	        @RequestParam("oldPassword") String oldPassword, Model model) {

	    User existingUser = userServiceImpl.findByEmail(user.getEmail());
	    if (existingUser != null) {
	        if (passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
	            if (result.hasErrors()) {
	                // Jika terdapat kesalahan validasi, kembali ke halaman form dengan model yang sama
	                return "/be-user/user/change-password";
	            }
	            userServiceImpl.changePassword(existingUser, user.getPassword()); // Mengencode kata sandi baru di service
	            model.addAttribute("successMessage", "Password changed successfully!");
	        } else {
	            model.addAttribute("errorMessage", "Old password is incorrect!");
	        }
	    } else {
	        model.addAttribute("errorMessage", "User not found!");
	    }

	    return "/be-user/user/change-password";
	}
	
	@GetMapping("/edit-password")
	public String formEditPassword() {
	    return "/be-admin/edit-password";
	}

	// change password dengan dto
//	@PostMapping("/changePassword")
//	public String changePassword(
//	        @Valid @ModelAttribute("changePasswordUserDto") ChangePasswordUserDto changePasswordUserDto,
//	        BindingResult result, @RequestParam("oldPassword") String oldPassword, Model model) {
//	    User existingUser = userServiceImpl.findByEmail(changePasswordUserDto.getEmail());
//	    System.out.println(existingUser.getEmail());
//	    if (existingUser != null) {
//	        if (passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
//	            if (result.hasErrors()) {
//	                return "/be-user/user/change-password";
//	            }
////	            userServiceImpl.changePassword(changePasswordUserDto);
//	            model.addAttribute("successMessage", "Password changed successfully!");
//	        } else {
//	            model.addAttribute("errorMessage", "Old password is incorrect!");
//	        }
//	    } else {
//	        model.addAttribute("errorMessage", "User not found!");
//	    }
//
//	    return "/be-user/user/change-password";
//	}


}
