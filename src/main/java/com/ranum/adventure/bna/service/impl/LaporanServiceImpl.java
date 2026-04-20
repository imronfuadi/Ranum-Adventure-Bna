package com.ranum.adventure.bna.service.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ranum.adventure.bna.dto.PenyewaanDto;
import com.ranum.adventure.bna.entities.Penyewaan;
import com.ranum.adventure.bna.repository.PenyewaanRepository;
import com.ranum.adventure.bna.service.LaporanService;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class LaporanServiceImpl implements LaporanService{
	
	@Autowired
	private PenyewaanRepository penyewaanRepository;

	@Override
	public List<PenyewaanDto> findAllPenyewaan() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Penyewaan> findPaginated(int page, int size) {
		// TODO Auto-generated method stub
		return penyewaanRepository.findAll(PageRequest.of(page, size));
	}
	
	@Override
	public Page<Penyewaan> searchPaginated(String keyword, int page, int size) {
	    Pageable pageable = PageRequest.of(page, size);
	    return penyewaanRepository.searchByKeyword(keyword, pageable);
	}

	public Page<Penyewaan> findByDateRangePaginated(java.time.LocalDateTime start, java.time.LocalDateTime end, int page, int size) {
	    Pageable pageable = PageRequest.of(page, size);
	    return penyewaanRepository.findByTanggalPinjamBetween(start, end, pageable);
	}
	
	@Autowired
    private DataSource dataSource;

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JasperPrint downloadLaporan() throws Exception {
        InputStream fileReport = new ClassPathResource("reports/laporan.jasper").getInputStream();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fileReport);
        
        Connection connection = getConnection();
        try {
            return JasperFillManager.fillReport(jasperReport, null, connection);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}
