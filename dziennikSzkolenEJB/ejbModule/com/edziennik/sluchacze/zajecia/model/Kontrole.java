package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the kontrole database table.
 * 
 */
@Entity
@NamedQuery(name="Kontrole.findAll", query="SELECT k FROM Kontrole k")
public class Kontrole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_kontrola")
	private int idKontrola;

	@Temporal(TemporalType.DATE)
	@Column(name="czas_kontrola")
	private Date czasKontrola;

	@Temporal(TemporalType.DATE)
	@Column(name="data_usuniecia")
	private Date dataUsuniecia;

	@Column(name="id_pluton")
	private BigInteger idPluton;

	@Lob
	private String komentarz;

	private String kontrolowany;

	@Column(name="kto_usunal")
	private String ktoUsunal;

	private String usuniety;

	@Column(name="wpisal_kontrola")
	private String wpisalKontrola;

	public Kontrole() {
	}

	public int getIdKontrola() {
		return this.idKontrola;
	}

	public void setIdKontrola(int idKontrola) {
		this.idKontrola = idKontrola;
	}

	public Date getCzasKontrola() {
		return this.czasKontrola;
	}

	public void setCzasKontrola(Date czasKontrola) {
		this.czasKontrola = czasKontrola;
	}

	public Date getDataUsuniecia() {
		return this.dataUsuniecia;
	}

	public void setDataUsuniecia(Date dataUsuniecia) {
		this.dataUsuniecia = dataUsuniecia;
	}

	public BigInteger getIdPluton() {
		return this.idPluton;
	}

	public void setIdPluton(BigInteger idPluton) {
		this.idPluton = idPluton;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public String getKontrolowany() {
		return this.kontrolowany;
	}

	public void setKontrolowany(String kontrolowany) {
		this.kontrolowany = kontrolowany;
	}

	public String getKtoUsunal() {
		return this.ktoUsunal;
	}

	public void setKtoUsunal(String ktoUsunal) {
		this.ktoUsunal = ktoUsunal;
	}

	public String getUsuniety() {
		return this.usuniety;
	}

	public void setUsuniety(String usuniety) {
		this.usuniety = usuniety;
	}

	public String getWpisalKontrola() {
		return this.wpisalKontrola;
	}

	public void setWpisalKontrola(String wpisalKontrola) {
		this.wpisalKontrola = wpisalKontrola;
	}

}