package com.ranum.adventure.bna.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ranum.adventure.bna.entities.User;
import com.ranum.adventure.bna.repository.PenyewaanRepository;
import com.ranum.adventure.bna.repository.UserRepository;
import com.ranum.adventure.bna.service.impl.PenyewaanServiceImpl;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

//	@Autowired
//	private UMKMServiceImpl umkmServiceImpl;

	@Autowired
	private PenyewaanRepository penyewaanRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PenyewaanServiceImpl penyewaanServiceImpl;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/dashboard")
	public String GetDashboardIndex(Model model) {

		// Chart 1: Jumlah UMKM berdasarkan kategori
//		List<Object[]> umkmData = umkmServiceImpl.countUMKMByCategory();
//		String[] labels = new String[umkmData.size()];
//		int[] data = new int[umkmData.size()];
//
//		for (int i = 0; i < umkmData.size(); i++) {
//			Object[] row = umkmData.get(i);
//			labels[i] = (String) row[0];
//			data[i] = ((Number) row[1]).intValue();
//		}

//		model.addAttribute("labels", labels);
//		model.addAttribute("data", data);

		// Chart 2: Jumlah UMKM berdasarkan kecamatan di Kota CIREBON
//		String kota = "CIREBON";
//		Map<String, Integer> umkmDataKec = umkmServiceImpl.countUmkmByKecamatanInKotaCirebon(kota);
//		String[] labelsByKecamatan = umkmDataKec.keySet().toArray(new String[0]);
//		int[] dataByKecamatan = umkmDataKec.values().stream().mapToInt(Integer::intValue).toArray();
//		model.addAttribute("labelsByKecamatan", labelsByKecamatan);
//		model.addAttribute("dataByKecamatan", dataByKecamatan);

//		String kota = "CIREBON";
//		List<Object[]> umkmDataKec = umkmServiceImpl.countKecamatanByKota(kota);
//		String[] labelsByKecamatan = new String[umkmDataKec.size()];
//		int[] dataByKecamatan = new int[umkmDataKec.size()];
//
//		for (int i = 0; i < umkmDataKec.size(); i++) {
//			Object[] row = umkmDataKec.get(i);
//			labelsByKecamatan[i] = (String) row[0];
//			dataByKecamatan[i] = ((Number) row[1]).intValue();
//		}
//		
//		model.addAttribute("labelsByKecamatan", labelsByKecamatan);
//		model.addAttribute("dataByKecamatan", dataByKecamatan);
		// Ambil data penyewaan per bulan (contoh dummy data)
		List<String> labels = List.of("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov",
				"Des");
		List<Integer> data = penyewaanServiceImpl.countPenyewaanPerBulan();

		BigDecimal totalPenghasilan = penyewaanRepository.getTotalPenghasilan();
		int bulanIni = LocalDate.now().getMonthValue();
		BigDecimal penghasilanBulanIni = penyewaanRepository.getPenghasilanBulanIni(bulanIni);

		long totalPenyewaan = penyewaanRepository.count();
		long penyewaanBulanIni = penyewaanRepository.countByMonth(LocalDate.now().getMonthValue());

		model.addAttribute("totalPenghasilan", totalPenghasilan);
		model.addAttribute("penghasilanBulanIni", penghasilanBulanIni);
		model.addAttribute("totalPenyewaan", totalPenyewaan);
		model.addAttribute("penyewaanBulanIni", penyewaanBulanIni);

		model.addAttribute("labels", labels);
		model.addAttribute("data", data);

		return "/be-admin/dashboard/dashboard";
	}

	@GetMapping("/edit-password")
	public String formEditPassword() {
		return "/be-admin/edit-password";
	}

	@PostMapping("/update-password")
	public String editPassword(@RequestParam String oldPassword, @RequestParam String newPassword,
			@RequestParam String confirmPassword, RedirectAttributes redirectAttributes, Principal principal) {

		User user = userRepository.findByEmail(principal.getName());

		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			redirectAttributes.addFlashAttribute("error", "Password lama salah!");
			return "redirect:/admin/edit-password";
		}

		if (!newPassword.equals(confirmPassword)) {
			redirectAttributes.addFlashAttribute("error", "Konfirmasi password tidak cocok!");
			return "redirect:/admin/edit-password";
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);

		redirectAttributes.addFlashAttribute("success", "Password berhasil diubah!");
		return "redirect:/admin/edit-password";
	}

//	@GetMapping("/umkm/chart")
//	public String showChart(Model model) {
//		String kota = "CIREBON";
//		Map<String, Integer> umkmData = umkmServiceImpl.countUmkmByKecamatanInKotaCirebon(kota);
//		String[] labelsKec = umkmData.keySet().toArray(new String[0]);
//		int[] dataKec = umkmData.values().stream().mapToInt(Integer::intValue).toArray();
//		model.addAttribute("labels", labelsKec);
//		model.addAttribute("data", dataKec);
//		return "chart";
//	}
}
