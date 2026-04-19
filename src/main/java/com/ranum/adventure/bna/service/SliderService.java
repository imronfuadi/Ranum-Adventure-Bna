package com.ranum.adventure.bna.service;

import java.util.List;
import java.util.Optional;

import com.ranum.adventure.bna.entities.Slider;

public interface SliderService {

	
	public List<Slider> findAllSlider();
	
	public Slider saveSlider(Slider slider);
	
	public Optional<Slider> updateSlider(Long id);
	
	public void deleteSlider(Long id);
}
