package com.ranum.adventure.bna.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ranum.adventure.bna.dto.BarangDto;
import com.ranum.adventure.bna.entities.Barang;
import com.ranum.adventure.bna.entities.Kategori;
import com.ranum.adventure.bna.repository.BarangRepository;
import com.ranum.adventure.bna.repository.KategoriRepository;
import com.ranum.adventure.bna.service.BarangService;
import com.ranum.adventure.bna.util.FileUploadUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BarangServiceImpl implements BarangService {

	@Autowired
	private BarangRepository barangRepository;

	@Autowired
	private KategoriRepository kategoriRepository;

	@Override
	public List<BarangDto> findAllBarang() {
		List<Barang> k = barangRepository.findAll();
		return k.stream().map(this::convertToDto).collect(Collectors.toList());
	}
	
	@Override
	public List<Barang> findAll() {
		List<Barang> k = barangRepository.findAll();
		return k;
	}

	@Override
	public Barang saveBarang(BarangDto dto, MultipartFile foto) {
//		if (foto == null || foto.isEmpty()) {
//			throw new IllegalArgumentException("Foto tidak boleh kosong.");
//		}

		String fileName = StringUtils.cleanPath(foto.getOriginalFilename());
		String contentType = foto.getContentType();

		if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
			throw new IllegalArgumentException("Hanya file JPEG dan PNG yang diperbolehkan.");
		}

		String uploadDir = "src/main/resources/static/assets/images/barang/foto/";
		try {
			FileUploadUtil.saveFile(uploadDir, fileName, foto);
		} catch (IOException e) {
			// Ubah IOException menjadi RuntimeException
			throw new RuntimeException("Gagal menyimpan file: " + e.getMessage(), e);
		}
		Barang barang = convertToEntity(dto);
		barang.setFoto(fileName);
		return barangRepository.save(barang);
	}

	@Override
	public BarangDto findBarangById(Long id) {
		// TODO Auto-generated method stub
		return barangRepository.findById(id).map(this::convertToDto).orElse(null);
	}

//	@Override
//	public BarangDto updateBarang(Long id, BarangDto barangDto, MultipartFile foto) {
////	    if (foto == null || foto.isEmpty()) {
////	        throw new IllegalArgumentException("Foto tidak boleh kosong.");
////	    }
//
//		String fileName = StringUtils.cleanPath(foto.getOriginalFilename());
//		String contentType = foto.getContentType();
//
//		if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
//			throw new IllegalArgumentException("Hanya file JPEG dan PNG yang diperbolehkan.");
//		}
//
//		String uploadDir = "src/main/resources/static/assets/images/barang/foto/";
//		try {
//			FileUploadUtil.saveFile(uploadDir, fileName, foto);
//		} catch (IOException e) {
//			// Ubah IOException menjadi RuntimeException
//			throw new RuntimeException("Gagal menyimpan file: " + e.getMessage(), e);
//		}
//
//		return barangRepository.findById(id).map(existingBarang -> {
//			existingBarang.setNamaBarang(barangDto.getNamaBarang());
//			existingBarang.setHarga(barangDto.getHarga());
//			existingBarang.setDeskripsi(barangDto.getDeskripsi());
//			existingBarang.setJumlah(barangDto.getJumlah());
//			existingBarang.setFoto(fileName);
//			existingBarang.setKategori(barangDto.getKategori());
//			Barang updatedBarang = barangRepository.save(existingBarang);
//			return convertToDto(updatedBarang);
//		}).orElseThrow(() -> new IllegalArgumentException("Barang dengan ID " + id + " tidak ditemukan."));
//	}
	@Override
	public BarangDto updateBarang(Long id, BarangDto barangDto, MultipartFile foto) throws IOException {
    	System.out.println("barangdto = " + barangDto);
		if (foto != null && !foto.isEmpty()) {
	        String fileName = StringUtils.cleanPath(foto.getOriginalFilename());
	        System.out.println("filename = " + fileName);
	        String contentType = foto.getContentType();

	        // Validasi jenis file
	        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
	            throw new IllegalArgumentException("Only JPEG and PNG files are allowed.");
	        }

	        // Simpan file ke direktori yang sesuai
	        String uploadDir = "src/main/resources/static/assets/images/barang/foto/";
	        FileUploadUtil.saveFile(uploadDir, fileName, foto);

	        // Set nama file ke dalam entitas barang
	        barangDto.setNamaFoto(fileName);
	    }

	    return barangRepository.findById(id).map(existingBarang -> {
	        existingBarang.setNamaBarang(barangDto.getNamaBarang());
	        existingBarang.setHarga(barangDto.getHarga());
	        existingBarang.setDeskripsi(barangDto.getDeskripsi());
	        existingBarang.setJumlah(barangDto.getJumlah());
	        
	        // Hanya set foto jika file baru diunggah
	        if (foto != null && !foto.isEmpty()) {
	            existingBarang.setFoto(barangDto.getNamaFoto());
	        }
	        if (existingBarang.getKategori() != null) {
				Kategori kategori = kategoriRepository.findById(barangDto.getKategoriId())
						.orElseThrow(() -> new IllegalArgumentException("Invalid tingkat pendidikan code"));
				existingBarang.setKategori(kategori);
			} else {
				existingBarang.setKategori(null); // Set to null if empty
			}
//	        existingBarang.setKategori(barangDto.getKategori());
	        Barang updatedBarang = barangRepository.save(existingBarang);
	        return convertToDto(updatedBarang);
	    }).orElseThrow(() -> new EntityNotFoundException("Barang not found with id " + id));
	}


	@Override
	public void deleteBarang(Long id) {
		this.barangRepository.deleteById(id);
	}

	@Override
	public Page<Barang> findPaginated(int page, int size) {
		// TODO Auto-generated method stub
		return barangRepository.findAll(PageRequest.of(page, size));
	}

	private BarangDto convertToDto(Barang barang) {
		BarangDto dto = new BarangDto();
		dto.setBarangId(barang.getBarangId());
		dto.setNamaBarang(barang.getNamaBarang());
		dto.setHarga(barang.getHarga());
		dto.setDeskripsi(barang.getDeskripsi());
		dto.setJumlah(barang.getJumlah());
		dto.setNamaFoto(barang.getFoto());
//		dto.setKategori(barang.getKategori());
		if (barang.getKategori() != null) {
			dto.setKategoriId(barang.getKategori().getKategoriId());
			dto.setNamaKategori(barang.getKategori().getNamaKategori());
		}
		return dto;
	}

	private Barang convertToEntity(BarangDto dto) {
		Barang barang = new Barang();
		barang.setBarangId(dto.getBarangId());
		barang.setNamaBarang(dto.getNamaBarang());
		barang.setHarga(dto.getHarga());
		barang.setDeskripsi(dto.getDeskripsi());
		barang.setJumlah(dto.getJumlah());
		barang.setFoto(dto.getNamaFoto());
//		barang.setKategori(dto.getKategori());
		// Validasi dan set TingkatPendidikan jika ada
		if (dto.getKategoriId() != null) {
			Kategori kategori = kategoriRepository.findById(dto.getKategoriId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid tingkat pendidikan code"));
			barang.setKategori(kategori);
		} else {
			barang.setKategori(null); // Set to null if empty
		}
		return barang;
	}

}
