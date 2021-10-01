package com.edziennik.logi.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the logi_daneosobowe database table.
 * 
 */
@Entity
@Table(name="logi_daneosobowe")
@NamedQueries({
@NamedQuery(name="LogiDaneosobowe.findAll", query="SELECT l FROM LogiDaneosobowe l"),
@NamedQuery(name="LogiDaneosobowe.findLogiFinder",  query="SELECT l FROM LogiDaneosobowe l WHERE CONVERT(l.idOsoba,char) LIKE :idOsoba AND l.dzialanie LIKE :dzialanie AND l.ip LIKE :ip AND l.kto LIKE :kto AND l.sesja LIKE :sesja AND l.opis LIKE :opis")
})
public class LogiDaneosobowe implements Serializable {
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
	
	private String sesja;
	
	@Column(name="nazwa_obiektu")
	private String nazwaObiektu;

	@Lob
	private String opis;

	public LogiDaneosobowe() {
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

	public String getSesja() {
		return sesja;
	}

	public void setSesja(String sesja) {
		this.sesja = sesja;
	}

}