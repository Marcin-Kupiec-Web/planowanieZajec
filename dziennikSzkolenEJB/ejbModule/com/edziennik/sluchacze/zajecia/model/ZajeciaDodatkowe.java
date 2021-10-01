package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the zajecia_dodatkowe database table.
 * 
 */
@Entity
@Table(name="zajecia_dodatkowe")
@NamedQuery(name="ZajeciaDodatkowe.findAll", query="SELECT z FROM ZajeciaDodatkowe z")
public class ZajeciaDodatkowe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_zajecia_dodatkowe")
	private int idZajeciaDodatkowe;

	@Temporal(TemporalType.DATE)
	@Column(name="data_dodatkowe")
	private Date dataDodatkowe;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_wpisu")
	private Date dataWpisu;

	@Temporal(TemporalType.DATE)
	@Column(name="kiedy_usuniety_dodatkowy")
	private Date kiedyUsunietyDodatkowy;

	private int komp;

	@Column(name="kto_usunal_dodatkowy")
	private String ktoUsunalDodatkowy;

	@Column(name="kto_wpisal_dodatkowe")
	private String ktoWpisalDodatkowe;

	@Column(name="liczba_godzin_dodatkowe")
	private int liczbaGodzinDodatkowe;

	@Lob
	private String opis;

	private int pluton;

	private String szkolenie;

	@Column(name="usuniety_dodatkowy")
	private String usunietyDodatkowy;

	public ZajeciaDodatkowe() {
	}

	public int getIdZajeciaDodatkowe() {
		return this.idZajeciaDodatkowe;
	}

	public void setIdZajeciaDodatkowe(int idZajeciaDodatkowe) {
		this.idZajeciaDodatkowe = idZajeciaDodatkowe;
	}

	public Date getDataDodatkowe() {
		return this.dataDodatkowe;
	}

	public void setDataDodatkowe(Date dataDodatkowe) {
		this.dataDodatkowe = dataDodatkowe;
	}

	public Date getDataWpisu() {
		return this.dataWpisu;
	}

	public void setDataWpisu(Date dataWpisu) {
		this.dataWpisu = dataWpisu;
	}

	public Date getKiedyUsunietyDodatkowy() {
		return this.kiedyUsunietyDodatkowy;
	}

	public void setKiedyUsunietyDodatkowy(Date kiedyUsunietyDodatkowy) {
		this.kiedyUsunietyDodatkowy = kiedyUsunietyDodatkowy;
	}

	public int getKomp() {
		return this.komp;
	}

	public void setKomp(int komp) {
		this.komp = komp;
	}

	public String getKtoUsunalDodatkowy() {
		return this.ktoUsunalDodatkowy;
	}

	public void setKtoUsunalDodatkowy(String ktoUsunalDodatkowy) {
		this.ktoUsunalDodatkowy = ktoUsunalDodatkowy;
	}

	public String getKtoWpisalDodatkowe() {
		return this.ktoWpisalDodatkowe;
	}

	public void setKtoWpisalDodatkowe(String ktoWpisalDodatkowe) {
		this.ktoWpisalDodatkowe = ktoWpisalDodatkowe;
	}

	public int getLiczbaGodzinDodatkowe() {
		return this.liczbaGodzinDodatkowe;
	}

	public void setLiczbaGodzinDodatkowe(int liczbaGodzinDodatkowe) {
		this.liczbaGodzinDodatkowe = liczbaGodzinDodatkowe;
	}

	public String getOpis() {
		return this.opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public int getPluton() {
		return this.pluton;
	}

	public void setPluton(int pluton) {
		this.pluton = pluton;
	}

	public String getSzkolenie() {
		return this.szkolenie;
	}

	public void setSzkolenie(String szkolenie) {
		this.szkolenie = szkolenie;
	}

	public String getUsunietyDodatkowy() {
		return this.usunietyDodatkowy;
	}

	public void setUsunietyDodatkowy(String usunietyDodatkowy) {
		this.usunietyDodatkowy = usunietyDodatkowy;
	}

}