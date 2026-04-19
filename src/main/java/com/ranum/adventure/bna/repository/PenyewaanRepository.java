package com.ranum.adventure.bna.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ranum.adventure.bna.entities.Penyewaan;

public interface PenyewaanRepository extends JpaRepository<Penyewaan, String> {

	@Query("""
			  SELECT p FROM Penyewaan p
			  WHERE p.id NOT IN (SELECT k.penyewaan.id FROM Pengembalian k)
			""")
	List<Penyewaan> findAllBelumDikembalikan();

//	@Query("SELECT p FROM Penyewaan p WHERE status_penyewaan LIKE '%DISEWA%' ")
//	@Query("SELECT p FROM Penyewaan p WHERE p.statusPenyewaan = 'DISEWA'")
//	@Query("SELECT p FROM Penyewaan p WHERE p.statusPenyewaan LIKE '%DISEWA%' OR '%DIBATALKAN%'")
//	@Query("SELECT p FROM Penyewaan p WHERE p.statusPenyewaan IN ('DISEWA', 'DIBATALKAN')")
	@Query("SELECT p FROM Penyewaan p WHERE p.statusPenyewaan IN ('DISEWA')")
	Page<Penyewaan> findAllStatusDisewa(Pageable pageable);

	@Query("SELECT p FROM Penyewaan p WHERE " + "LOWER(p.penyewaanId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(p.pelanggan.nama) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	Page<Penyewaan> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM tb_penyewaan WHERE EXTRACT(MONTH FROM tanggal_pinjam) = :month", nativeQuery = true)
	int countPenyewaanByMonth(@Param("month") int month);

	// atau langsung:
	@Query(value = """
			    SELECT to_char(tanggal_pinjam, 'Mon') AS bulan,
			           COUNT(*) as jumlah
			    FROM tb_penyewaan
			    GROUP BY bulan, EXTRACT(MONTH FROM tanggal_pinjam)
			    ORDER BY EXTRACT(MONTH FROM tanggal_pinjam)
			""", nativeQuery = true)
	List<Object[]> countPenyewaanPerBulanRaw();

//	@Query("SELECT COALESCE(SUM(p.totalHarga), 0) FROM Penyewaan p")
//	BigDecimal getTotalPenghasilan();

	@Query("SELECT COALESCE(SUM(p.totalHarga), 0) FROM Penyewaan p WHERE p.statusPenyewaan <> 'DIBATALKAN'")
	BigDecimal getTotalPenghasilan();

//	@Query("SELECT COALESCE(SUM(p.totalHarga), 0) FROM Penyewaan p WHERE MONTH(p.tanggalPinjam) = :month")
//	BigDecimal getPenghasilanBulanIni(@Param("month") int month);

	@Query(value = "SELECT COALESCE(SUM(total_harga), 0) " + "FROM tb_penyewaan "
			+ "WHERE EXTRACT(MONTH FROM tanggal_pinjam) = :month "
			+ "AND status_penyewaan <> 'DIBATALKAN'", nativeQuery = true)
	BigDecimal getPenghasilanBulanIni(@Param("month") int month);

	@Query("SELECT COUNT(p) FROM Penyewaan p WHERE MONTH(p.tanggalPinjam) = :month")
	long countByMonth(@Param("month") int month);

}
