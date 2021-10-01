package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the zrodlo_danych database table.
 * 
 */
@Entity
@Table(name="zrodlo_danych")
@NamedQuery(name="ZrodloDanych.findAll", query="SELECT z FROM ZrodloDanych z")
public class ZrodloDanych implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_zrodlo_danych")
	private int idZrodloDanych;

	@Column(name="data_wpisu")
	private Timestamp dataWpisu;

	@Column(name="id_osoba")
	private int idOsoba;

	@Lob
	private String komentarz;

	@Lob
	private String opis;

	private BigInteger wpisujacy;

	public ZrodloDanych() {
	}

	public int getIdZrodloDanych() {
		return this.idZrodloDanych;
	}

	public void setIdZrodloDanych(int idZrodloDanych) {
		this.idZrodloDanych = idZrodloDanych;
	}

	public Timestamp getDataWpisu() {
		return this.dataWpisu;
	}

	public void setDataWpisu(Timestamp dataWpisu) {
		this.dataWpisu = dataWpisu;
	}

	public int getIdOsoba() {
		return this.idOsoba;
	}

	public void setIdOsoba(int idOsoba) {
		this.idOsoba = idOsoba;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public String getOpis() {
		return this.opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public BigInteger getWpisujacy() {
		return this.wpisujacy;
	}

	public void setWpisujacy(BigInteger wpisujacy) {
		this.wpisujacy = wpisujacy;
	}

}