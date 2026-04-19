package com.ranum.adventure.bna.security;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ranum.adventure.bna.entities.Berita;


@Component
public class BeritaValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Berita.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "judul", "judul.required", "Judul harus diisi");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tanggal", "tanggal.required", "Tanggal tidak boleh kosong");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deskripsi", "deskripsi.required", "Deskripsi harus diisi");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pictBerita", "pictBerita.required", "Foto tidak boleh kosong");
    }
}

