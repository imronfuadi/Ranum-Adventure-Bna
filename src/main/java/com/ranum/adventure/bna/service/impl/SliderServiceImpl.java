package com.ranum.adventure.bna.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ranum.adventure.bna.entities.Slider;
import com.ranum.adventure.bna.repository.SliderRepository;
import com.ranum.adventure.bna.service.SliderService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SliderServiceImpl implements SliderService{

	private final SliderRepository sliderRepository;

	@Override
	public List<Slider> findAllSlider() {
		// TODO Auto-generated method stub
		return sliderRepository.findAll();
	}

	@Override
	public Slider saveSlider(Slider slider) {
		// TODO Auto-generated method stub
		return sliderRepository.save(slider);
	}

	@Override
	public Optional<Slider> updateSlider(Long id) {
		// TODO Auto-generated method stub
		return sliderRepository.findById(id);
	}

	@Override
	public void deleteSlider(Long id) {
		// TODO Auto-generated method stub
		sliderRepository.deleteById(id);
	}
}
