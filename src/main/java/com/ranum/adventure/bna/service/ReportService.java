package com.ranum.adventure.bna.service;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.ranum.adventure.bna.entities.Penyewaan;
import com.ranum.adventure.bna.repository.PenyewaanRepository;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class ReportService {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PenyewaanRepository penyewaanRepository;

	public String getUserNameById(String id) {
		Optional<Penyewaan> penyewaanOptional = penyewaanRepository.findById(id);

		if (penyewaanOptional.isPresent()) {
			Penyewaan penyewaan = penyewaanOptional.get();
			return penyewaan.getPelanggan().getNama();
		} else {
			// Handle case where Biodata is not found (return a default value or throw an
			// exception)
			return "Unknown User"; // Or handle this case appropriately
		}
	}

	private Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public JasperPrint generateJasperPrint(String id) throws Exception {
		// Path to your Jasper file
		InputStream fileReport = new ClassPathResource("reports/Invoice2.jasper").getInputStream();

		// Load the compiled JasperReport
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fileReport);

		// Define the parameters map
		Map<String, Object> params = new HashMap<>();
		params.put("Parameter2", id); // Pass the ID as a parameter to the report

		try (Connection connection = getConnection()) {
			if (connection == null) {
				throw new SQLException("Failed to establish a database connection.");
			}
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, connection);
			return jasperPrint;
		}

//        Connection connection = getConnection();
//        try {
//            return JasperFillManager.fillReport(jasperReport, params, connection);
//        } finally {
//            if (connection != null) {
//                connection.close();
//            }
//        }
	}

//	@Autowired
//	private DataSource dataSource;
//	
//	private Connection getConnection() {
//	
//		try {
//			return dataSource.getConnection();
//		} catch (SQLException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return null;
//		}
//	}
//	
//	public JasperPrint generateJasperPrint() throws Exception{
//		//buka file
//		InputStream fileReport = new ClassPathResource("reports/TestimonialReport.jasper").getInputStream();
//		//load
//		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fileReport);
//		//print
//		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, getConnection());
//		return jasperPrint;
//	}

//	@Autowired
//    private DataSource dataSource;
//
//    private Connection getConnection() {
//        try {
//            return dataSource.getConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public JasperPrint generateJasperPrint() throws Exception {
//        InputStream fileReport = new ClassPathResource("reports/Invoice2.jasper").getInputStream();
//        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fileReport);
//        
//        Connection connection = getConnection();
//        try {
//            return JasperFillManager.fillReport(jasperReport, null, connection);
//        } finally {
//            if (connection != null) {
//                connection.close();
//            }
//        }
//    }
}
