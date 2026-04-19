package com.ranum.adventure.bna.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ranum.adventure.bna.entities.Penyewaan;
import com.ranum.adventure.bna.generator.IdGenerator;
import com.ranum.adventure.bna.repository.PenyewaanRepository;
import com.ranum.adventure.bna.service.PenyewaanService;

@Service
public class PenyewaanServiceImpl implements PenyewaanService{

	@Autowired
	private PenyewaanRepository penyewaanRepository;
	
//	@Override
//	public Page<Penyewaan> findPaginated(int page, int size) {
//		// TODO Auto-generated method stub
//		return penyewaanRepository.findAll(PageRequest.of(page, size));
//	}
	
	@Override
	public Page<Penyewaan> findPaginated(int page, int size) {
		// TODO Auto-generated method stub
		return penyewaanRepository.findAllStatusDisewa(PageRequest.of(page, size));
	}

	@Override
	public Optional<Penyewaan> findById(String id) {
		return penyewaanRepository.findById(id);
	}

	@Override
	public Penyewaan prosesPembayaran(Penyewaan penyewaan, String metodeBayar) {
//		String penyewaanId = IdGenerator.generatePenyewaanId();
//		penyewaan.setPenyewaanId(penyewaanId);
		penyewaan.setStatusPembayaran("LUNAS");
        penyewaan.setMetodeBayar(metodeBayar); // pastikan Anda punya field ini di Penyewaan
        return penyewaanRepository.save(penyewaan);
	}
	
	public List<Integer> countPenyewaanPerBulan() {
	    List<Object[]> raw = penyewaanRepository.countPenyewaanPerBulanRaw();
	    Map<String, Integer> map = new HashMap<>();

	    for (Object[] row : raw) {
	        String bulan = (String) row[0];
	        Integer jumlah = ((Number) row[1]).intValue();
	        map.put(bulan, jumlah);
	    }

	    // Urutkan sesuai bulan
	    List<String> labels = List.of("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des");
	    List<Integer> result = new ArrayList<>();
	    for (String bulan : labels) {
	        result.add(map.getOrDefault(bulan, 0));
	    }
	    return result;
	}


}
