package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the ogloszenia database table.
 * 
 */
@Entity
@NamedQuery(name="Ogloszenia.findAll", query="SELECT o FROM Ogloszenia o")
public class Ogloszenia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_ogloszenia")
	private int idOgloszenia;

	@Temporal(TemporalType.DATE)
	@Column(name="kiedy_ogloszenia")
	private Date kiedyOgloszenia;

	@Column(name="kto_wpisal_ogloszenia")
	private int ktoWpisalOgloszenia;

	@Lob
	private String ogloszenie;

	public Ogloszenia() {
	}

	public int getIdOgloszenia() {
		return this.idOgloszenia;
	}

	public void setIdOgloszenia(int idOgloszenia) {
		this.idOgloszenia = idOgloszenia;
	}

	public Date getKiedyOgloszenia() {
		return this.kiedyOgloszenia;
	}

	public void setKiedyOgloszenia(Date kiedyOgloszenia) {
		this.kiedyOgloszenia = kiedyOgloszenia;
	}

	public int getKtoWpisalOgloszenia() {
		return this.ktoWpisalOgloszenia;
	}

	public void setKtoWpisalOgloszenia(int ktoWpisalOgloszenia) {
		this.ktoWpisalOgloszenia = ktoWpisalOgloszenia;
	}

	public String getOgloszenie() {
		return this.ogloszenie;
	}

	public void setOgloszenie(String ogloszenie) {
		this.ogloszenie = ogloszenie;
	}

}