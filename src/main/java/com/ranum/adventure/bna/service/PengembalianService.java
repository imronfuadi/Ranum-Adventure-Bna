package com.ranum.adventure.bna.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;

import com.ranum.adventure.bna.entities.Pengembalian;

public interface PengembalianService {

    public Pengembalian prosesPengembalian(String penyewaanId, int lamaPerjanjianHari, BigDecimal dendaPerHari);
    
    public Page<Pengembalian> findPaginated(int page, int size) ;
}
