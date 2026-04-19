package com.ranum.adventure.bna.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ranum.adventure.bna.dto.TestimonialsDto;
import com.ranum.adventure.bna.entities.Testimonials;
import com.ranum.adventure.bna.repository.TestimonialsRepository;
import com.ranum.adventure.bna.service.TestimonialsService;

@Service
public class TestimonialsServiceImpl implements TestimonialsService{
	
	@Autowired
	private TestimonialsRepository testimonialsRepository;

	@Override
	public List<TestimonialsDto> getAllTestimonial() {
		List<Testimonials> testi = testimonialsRepository.findAll();
		return testi.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
	}

	@Override
	public TestimonialsDto getTestimonialById(Long testimonialId) {
		Optional<Testimonials> testiOptional = testimonialsRepository.findById(testimonialId);
		return testiOptional.map(this::convertToDto).orElse(null);
	}

	@Override
	public void createTestimonial(TestimonialsDto dto) {
		Testimonials testi = convertToEntity(dto);
		testimonialsRepository.save(testi);
		
	}

	@Override
	public void updateTestimonial(Long testimonialId, TestimonialsDto dto) {
		Testimonials testi = convertToEntity(dto);
		testi.setId(testimonialId);
		testimonialsRepository.save(testi);
	}

	@Override
	public void deleteTestimonial(Long testimonialId) {
		testimonialsRepository.deleteById(testimonialId);
	}
	
	private Testimonials convertToEntity(TestimonialsDto dto) {
		Testimonials testi = new Testimonials();
		testi.setId(dto.getId());
		testi.setName(dto.getName());
		testi.setPosition(dto.getPosition());
		testi.setTestimonial(dto.getTestimonial());
		testi.setPictTestimonial(dto.getPictTestimonial());
		return testi;
	}
	
	private TestimonialsDto convertToDto(Testimonials testi) {
		TestimonialsDto dto = new TestimonialsDto();
		dto.setId(testi.getId());
		dto.setName(testi.getName());
		dto.setPosition(testi.getPosition());
		dto.setTestimonial(testi.getTestimonial());
		dto.setPictTestimonial(testi.getPictTestimonial());
		return dto;
	}

}
