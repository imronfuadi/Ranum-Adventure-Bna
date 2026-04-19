package com.ranum.adventure.bna.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ranum.adventure.bna.dto.PelangganDto;
import com.ranum.adventure.bna.entities.Pelanggan;
import com.ranum.adventure.bna.repository.PelangganRepository;
import com.ranum.adventure.bna.service.PelangganService;
import com.ranum.adventure.bna.util.FileUploadUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PelangganServiceImpl implements PelangganService{
	
	@Autowired
	private PelangganRepository pelangganRepository;

	@Override
	public Pelanggan findPelangganByEmail(String email) {
		return pelangganRepository.findByEmail(email);
	}
	
	@Override
	public List<PelangganDto> findAllUser() {
		List<Pelanggan> k = pelangganRepository.findAll();
		return k.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public Pelanggan savePelanggan(PelangganDto dto, MultipartFile foto) {
		String fileName = StringUtils.cleanPath(foto.getOriginalFilename());
		String contentType = foto.getContentType();

//		if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
//			throw new IllegalArgumentException("Hanya file JPEG dan PNG yang diperbolehkan.");
//		}

		String uploadDir = "src/main/resources/static/assets/images/pelanggan/foto/";
		try {
			FileUploadUtil.saveFile(uploadDir, fileName, foto);
		} catch (IOException e) {
			// Ubah IOException menjadi RuntimeException
			throw new RuntimeException("Gagal menyimpan file: " + e.getMessage(), e);
		}
		Pelanggan user= convertToEntity(dto);
		user.setFoto(fileName);
		return pelangganRepository.save(user);
	}

	@Override
	public PelangganDto findPelangganById(Long id) {
		// TODO Auto-generated method stub
		return pelangganRepository.findById(id).map(this::convertToDto).orElse(null);
	}

//	@Override
//	public UserDto updateUser(Long id, UserDto userDto, MultipartFile file) throws IOException {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	@Override
	public PelangganDto updatePelanggan(Long id, PelangganDto pelangganDto, MultipartFile foto) throws IOException {
	    if (foto != null && !foto.isEmpty()) {
	        String fileName = StringUtils.cleanPath(foto.getOriginalFilename());
	        String contentType = foto.getContentType();

	        // Validasi jenis file
	        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
	            throw new IllegalArgumentException("Only JPEG and PNG files are allowed.");
	        }

	        // Simpan file ke direktori yang sesuai
	        String uploadDir = "src/main/resources/static/assets/images/pelanggan/foto/";
	        FileUploadUtil.saveFile(uploadDir, fileName, foto);

	        // Set nama file ke dalam entitas barang
	        pelangganDto.setNamaFoto(fileName);
	    }

	    return pelangganRepository.findById(id).map(existingBarang -> {
	        existingBarang.setNama(pelangganDto.getNama());
	        existingBarang.setAlamat(pelangganDto.getAlamat());
	        existingBarang.setNik(pelangganDto.getNik());
	        existingBarang.setEmail(pelangganDto.getEmail());
	        existingBarang.setNomorHp(pelangganDto.getNomorHp());
	        existingBarang.setPassword(pelangganDto.getPassword());
	        
	        // Hanya set foto jika file baru diunggah
	        if (foto != null && !foto.isEmpty()) {
	            existingBarang.setFoto(pelangganDto.getNamaFoto());
	        }
//	        existingBarang.setKategori(barangDto.getKategori());
	        Pelanggan updatedPelanggan= pelangganRepository.save(existingBarang);
	        return convertToDto(updatedPelanggan);
	    }).orElseThrow(() -> new EntityNotFoundException("Barang not found with id " + id));
	}

	@Override
	public void deletePelanggan(Long id) {
		this.pelangganRepository.deleteById(id);
	}

	@Override
	public Page<Pelanggan> findPaginated(int page, int size) {
		// TODO Auto-generated method stub
		return pelangganRepository.findAll(PageRequest.of(page, size));
	}
	
	private PelangganDto convertToDto(Pelanggan pelanggan) {
		PelangganDto dto = new PelangganDto();
		dto.setPelangganId(pelanggan.getPelangganId());
		dto.setNama(pelanggan.getNama());
		dto.setNik(pelanggan.getNik());
		dto.setAlamat(pelanggan.getAlamat());
		dto.setEmail(pelanggan.getEmail());
		dto.setNomorHp(pelanggan.getNomorHp());
		dto.setPassword(pelanggan.getPassword());
		dto.setNamaFoto(pelanggan.getFoto());
		return dto;
	}
	
	private Pelanggan convertToEntity(PelangganDto dto) {
		Pelanggan pelanggan = new Pelanggan();
		pelanggan.setPelangganId(dto.getPelangganId());
		pelanggan.setNama(dto.getNama());
		pelanggan.setNik(dto.getNik());
		pelanggan.setAlamat(dto.getAlamat());
		pelanggan.setEmail(dto.getEmail());
		pelanggan.setNomorHp(dto.getNomorHp());
		pelanggan.setPassword(dto.getPassword());
		pelanggan.setFoto(dto.getNamaFoto());
		return pelanggan;
	}

	@Override
	public Long getUserIdByUsername(String username) {
		Pelanggan pelanggan = pelangganRepository.findByEmail(username);
		if (pelanggan == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return pelanggan.getPelangganId();
	}

}
