package com.ranum.adventure.bna.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Testimonials {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "position")
	private String position;
	
	@Column(name = "testimonial")
	private String testimonial;
	
	@Column(name = "pict_testimonial")
	private String pictTestimonial;
	
//	@Transient
//	public String getPhotosTestimonialsPath() {
//        if (pictTestimonial == null || id == null) return null;
//         
//        return "/assets/images/testimonials/" + pictTestimonial;
//    }
}
