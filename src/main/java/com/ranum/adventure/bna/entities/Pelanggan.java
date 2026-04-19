package com.ranum.adventure.bna.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_pelanggan")
public class Pelanggan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pelangganId;

    @Column(name = "nama")
    private String nama;

    @Column(name = "alamat")
    private String alamat;

    @Column(name = "nik")
    private String nik;

    @Column(name = "foto")
    private String foto;

    @Column(name = "nomor_hp")
    private String nomorHp;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "pelanggan", cascade = CascadeType.ALL)
    private List<Penyewaan> sewaList;
    
    @Transient
	public String getPhotosPelangganPath() {
        if (foto == null || pelangganId == null) return null;
         
        return "/assets/images/pelanggan/foto/" + foto;
//        return "/user-photos/" + id + "/" + pict;
    }
    
//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinTable(name = "users_roles", joinColumns = {
//			@JoinColumn(name = "USER_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
//					@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
//	private List<Role> roles = new ArrayList();

    // Getters dan Setters
}
