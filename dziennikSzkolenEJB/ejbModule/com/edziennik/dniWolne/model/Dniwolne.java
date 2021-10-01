package com.edziennik.dniWolne.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the dniwolne database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Dniwolne.findAll", query="SELECT d FROM Dniwolne d ORDER BY d.dzien"),
@NamedQuery(name="Dniwolne.findAllinYear", query="SELECT d FROM Dniwolne d WHERE d.rok LIKE :rok ORDER BY d.dzien"),
})
public class Dniwolne implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id_dniWolne;

	@Temporal(TemporalType.DATE)
	private Date dzien;

	private String komentarz;

	private Integer miesiac;
	
	private Integer rok;

	public Dniwolne() {
	}

	public int getId_dniWolne() {
		return this.id_dniWolne;
	}

	public void setId_dniWolne(int id_dniWolne) {
		this.id_dniWolne = id_dniWolne;
	}

	public Date getDzien() {
		return this.dzien;
	}

	public void setDzien(Date dzien) {
		this.dzien = dzien;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public Integer getMiesiac() {
		return this.miesiac;
	}

	public void setMiesiac(Integer miesiac) {
		this.miesiac = miesiac;
	}

	public Integer getRok() {
		return rok;
	}

	public void setRok(Integer rok) {
		this.rok = rok;
	}

}