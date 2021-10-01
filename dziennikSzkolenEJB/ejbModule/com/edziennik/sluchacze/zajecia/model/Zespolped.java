package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the zespolped database table.
 * 
 */
@Entity
@NamedQuery(name="Zespolped.findAll", query="SELECT z FROM Zespolped z")
public class Zespolped implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id_zespolPed;

	private String funkcja;

	@Column(name="id_pluton")
	private int idPluton;

	private String imie;

	private String komorkaOrg;

	private String nazwisko;

	private String nieetatowyDowodca;

	private String nieetatowyPomocnik;

	private String stopien;

	private int telefon;

	public Zespolped() {
	}

	public int getId_zespolPed() {
		return this.id_zespolPed;
	}

	public void setId_zespolPed(int id_zespolPed) {
		this.id_zespolPed = id_zespolPed;
	}

	public String getFunkcja() {
		return this.funkcja;
	}

	public void setFunkcja(String funkcja) {
		this.funkcja = funkcja;
	}

	public int getIdPluton() {
		return this.idPluton;
	}

	public void setIdPluton(int idPluton) {
		this.idPluton = idPluton;
	}

	public String getImie() {
		return this.imie;
	}

	public void setImie(String imie) {
		this.imie = imie;
	}

	public String getKomorkaOrg() {
		return this.komorkaOrg;
	}

	public void setKomorkaOrg(String komorkaOrg) {
		this.komorkaOrg = komorkaOrg;
	}

	public String getNazwisko() {
		return this.nazwisko;
	}

	public void setNazwisko(String nazwisko) {
		this.nazwisko = nazwisko;
	}

	public String getNieetatowyDowodca() {
		return this.nieetatowyDowodca;
	}

	public void setNieetatowyDowodca(String nieetatowyDowodca) {
		this.nieetatowyDowodca = nieetatowyDowodca;
	}

	public String getNieetatowyPomocnik() {
		return this.nieetatowyPomocnik;
	}

	public void setNieetatowyPomocnik(String nieetatowyPomocnik) {
		this.nieetatowyPomocnik = nieetatowyPomocnik;
	}

	public String getStopien() {
		return this.stopien;
	}

	public void setStopien(String stopien) {
		this.stopien = stopien;
	}

	public int getTelefon() {
		return this.telefon;
	}

	public void setTelefon(int telefon) {
		this.telefon = telefon;
	}

}