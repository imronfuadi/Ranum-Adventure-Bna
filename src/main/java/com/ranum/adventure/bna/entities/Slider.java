package com.ranum.adventure.bna.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name ="slider")
public class Slider {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "pict_slider", nullable = false)
	private String pictSlider;
	
	@Transient
	public String getPhotosImagePath() {
        if (pictSlider == null || id == null) return null;
         
        return "/assets/images/slider/" + pictSlider;
//        return "/user-photos/" + id + "/" + pict;
    }
}
