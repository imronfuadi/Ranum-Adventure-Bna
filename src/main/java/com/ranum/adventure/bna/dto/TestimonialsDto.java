package com.ranum.adventure.bna.dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialsDto {

	private Long id;

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z\\s]+{1,50}$", message = "username must be of 1 to 50 length with no special characters")
	private String name;

	@NotBlank
	private String position;

	@NotBlank
	private String testimonial;
	
	
	private String pictTestimonial;
	
	@Transient
	public String getPhotosTestimonialsPath() {
        if (pictTestimonial == null || id == null) return null;
        return "/assets/images/testimonials/" + pictTestimonial;
    }
}
