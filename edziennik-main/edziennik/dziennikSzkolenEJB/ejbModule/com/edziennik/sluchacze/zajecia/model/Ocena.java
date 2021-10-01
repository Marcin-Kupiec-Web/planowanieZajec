package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.userManager.model.User;


/**
 * The persistent class for the ocena database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Ocena.findAll", query="SELECT o FROM Ocena o"),
@NamedQuery(name="Ocena.findAllinPluton", query="SELECT o FROM Ocena o WHERE o.ocenyNglowek.pluton.idPluton LIKE :idPluton ORDER BY o.dataOcena DESC")
})
public class Ocena implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_ocena")
	private int idOcena;

	@Temporal(TemporalType.DATE)
	@Column(name="data_ocena")
	private Date dataOcena;


	@Column(name="data_remove")
	private Timestamp dataRemove;

	@Column(name="data_wpisu")
	private Timestamp dataWpisu;

	@ManyToOne
	@JoinColumn(name="id_uzytkownik")
	private User idUzytkownik;

	private String komentarz;

	@Column(name="ocena_wartosc")
	private String ocenaWartosc;

	private boolean usunieta;
	
	private String imieNazwiskoUserUsunal;
	private Integer imieNazwiskoUserUsunalId;
	
	private String kolor;

	//bi-directional many-to-one association to OcenyNglowek
	@ManyToOne
	@JoinColumn(name="id_oceny_naglowek")
	private OcenyNaglowek ocenyNglowek;

	//bi-directional many-to-one association to Sluchacze
	@ManyToOne
	@JoinColumn(name="id_s")
	private Sluchacze sluchacze;


	public Ocena() {
	}

	public int getIdOcena() {
		return this.idOcena;
	}

	public void setIdOcena(int idOcena) {
		this.idOcena = idOcena;
	}

	public Date getDataOcena() {
		return this.dataOcena;
	}

	public void setDataOcena(Date dataOcena) {
		this.dataOcena = dataOcena;
	}

	public Timestamp getDataRemove() {
		return this.dataRemove;
	}

	public void setDataRemove(Timestamp dataRemove) {
		this.dataRemove = dataRemove;
	}

	public Timestamp getDataWpisu() {
		return this.dataWpisu;
	}

	public void setDataWpisu(Timestamp dataWpisu) {
		this.dataWpisu = dataWpisu;
	}

	public User getIdUzytkownik() {
		return this.idUzytkownik;
	}

	public void setIdUzytkownik(User idUzytkownik) {
		this.idUzytkownik = idUzytkownik;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public String getOcenaWartosc() {
		return this.ocenaWartosc;
	}

	public void setOcenaWartosc(String ocenaWartosc) {
		this.ocenaWartosc = ocenaWartosc;
	}

	public boolean getUsunieta() {
		return this.usunieta;
	}

	public void setUsunieta(boolean usunieta) {
		this.usunieta = usunieta;
	}

	public OcenyNaglowek getOcenyNglowek() {
		return this.ocenyNglowek;
	}

	public void setOcenyNglowek(OcenyNaglowek ocenyNglowek) {
		this.ocenyNglowek = ocenyNglowek;
	}

	public Sluchacze getSluchacze() {
		return this.sluchacze;
	}

	public void setSluchacze(Sluchacze sluchacze) {
		this.sluchacze = sluchacze;
	}

	public String getKolor() {
		return kolor;
	}

	public void setKolor(String kolor) {
		this.kolor = kolor;
	}

	public String getImieNazwiskoUserUsunal() {
		return imieNazwiskoUserUsunal;
	}

	public void setImieNazwiskoUserUsunal(String imieNazwiskoUserUsunal) {
		this.imieNazwiskoUserUsunal = imieNazwiskoUserUsunal;
	}

	public Integer getImieNazwiskoUserUsunalId() {
		return imieNazwiskoUserUsunalId;
	}

	public void setImieNazwiskoUserUsunalId(Integer imieNazwiskoUserUsunalId) {
		this.imieNazwiskoUserUsunalId = imieNazwiskoUserUsunalId;
	}



}