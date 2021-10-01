package com.edziennik.logi.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the logi database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Logi.findAll", query="SELECT l FROM Logi l"),
@NamedQuery(name="Logi.findAllLogoWylog", query="SELECT l FROM Logi l WHERE l.dzialanie LIKE 'logowanie' OR l.dzialanie LIKE 'wylogowanie'"),
@NamedQuery(name="Logi.findLogiFinder", query="SELECT l FROM Logi l WHERE CONVERT(l.idUser,char) LIKE :idUser AND l.dzialanie LIKE :dzialanie AND l.ip LIKE :ip AND l.modol LIKE :modol AND l.kto LIKE :kto AND l.nazwaObiektu LIKE :nazwaObiektu AND l.sesja LIKE :sesja AND l.opis LIKE :opis")
})
public class Logi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_logi")
	private int idLogi;

	private Timestamp data;

	private String dzialanie;

	@Column(name="id_obiektu")
	private int idObiektu;

	@Column(name="id_osoba")
	private int idOsoba;

	@Column(name="id_user")
	private int idUser;

	private String ip;

	private String kto;
	
	private String modol;
	
	private String sesja;
	
	@Column(name="nazwa_obiektu")
	private String nazwaObiektu;

	@Lob
	private String opis;

	public Logi() {
	}

	public int getIdLogi() {
		return this.idLogi;
	}

	public void setIdLogi(int idLogi) {
		this.idLogi = idLogi;
	}

	public Timestamp getData() {
		return this.data;
	}

	public void setData(Timestamp data) {
		this.data = data;
	}

	public String getDzialanie() {
		return this.dzialanie;
	}

	public void setDzialanie(String dzialanie) {
		this.dzialanie = dzialanie;
	}

	public int getIdObiektu() {
		return this.idObiektu;
	}

	public void setIdObiektu(int idObiektu) {
		this.idObiektu = idObiektu;
	}

	public int getIdOsoba() {
		return this.idOsoba;
	}

	public void setIdOsoba(int idOsoba) {
		this.idOsoba = idOsoba;
	}

	public int getIdUser() {
		return this.idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getKto() {
		return this.kto;
	}

	public void setKto(String kto) {
		this.kto = kto;
	}

	public String getNazwaObiektu() {
		return this.nazwaObiektu;
	}

	public void setNazwaObiektu(String nazwaObiektu) {
		this.nazwaObiektu = nazwaObiektu;
	}

	public String getOpis() {
		return this.opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getModol() {
		return modol;
	}

	public void setModol(String modol) {
		this.modol = modol;
	}

	public String getSesja() {
		return sesja;
	}

	public void setSesja(String sesja) {
		this.sesja = sesja;
	}

}