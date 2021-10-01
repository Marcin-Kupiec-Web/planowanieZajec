package com.edziennik.terminarz.model;

import java.io.Serializable;
import javax.persistence.*;

import com.edziennik.sluchacze.zajecia.model.Pluton;

import java.util.Calendar;
import java.util.Date;


/**
 * The persistent class for the terminarz database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Terminarz.findAll", query="SELECT t FROM Terminarz t "),
	@NamedQuery(name="Terminarz.findAllowed", query="SELECT t FROM Terminarz t WHERE t.ktoWpisal LIKE :ktoWpisal OR t.doWiadomosci LIKE 'wszyscy' OR t.doWiadomosci LIKE :zaklad OR CONVERT(t.plutont.idPluton, CHAR(16)) LIKE :idPluton")
})
public class Terminarz implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_terminarz")
	private int idTerminarz;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_wpisu")
	private Calendar dataWpisu;

	private String doWiadomosci;

	private String kolor;
	
	private boolean calyDzien;
	
	@Column(name="kto_wpisal")
	private int ktoWpisal;

	private String stampWpis;

	@Temporal(TemporalType.TIMESTAMP)
	private Date terminOd;

	@Temporal(TemporalType.TIMESTAMP)
	private Date terminDo;
	
	@ManyToOne
	@JoinColumn(name="id_pluton")
	private Pluton plutont;
	
	public Date getTerminDo() {
		return terminDo;
	}

	public void setTerminDo(Date terminDo) {
		this.terminDo = terminDo;
	}

	@Lob
	private String tresc;

	public Terminarz() {
	}

	public int getIdTerminarz() {
		return this.idTerminarz;
	}

	public void setIdTerminarz(int idTerminarz) {
		this.idTerminarz = idTerminarz;
	}

	public Calendar getDataWpisu() {
		return this.dataWpisu;
	}

	public void setDataWpisu(Calendar calendar) {
		this.dataWpisu = calendar;
	}

	public String getDoWiadomosci() {
		return this.doWiadomosci;
	}

	public void setDoWiadomosci(String doWiadomosci) {
		this.doWiadomosci = doWiadomosci;
	}

	public String getKolor() {
		return this.kolor;
	}

	public void setKolor(String kolor) {
		this.kolor = kolor;
	}

	public int getKtoWpisal() {
		return this.ktoWpisal;
	}

	public void setKtoWpisal(int ktoWpisal) {
		this.ktoWpisal = ktoWpisal;
	}

	public String getStampWpis() {
		return this.stampWpis;
	}

	public void setStampWpis(String stampWpis) {
		this.stampWpis = stampWpis;
	}

	public Date getTerminOd() {
		return this.terminOd;
	}

	public void setTerminOd(Date terminOd) {
		this.terminOd = terminOd;
	}

	public String getTresc() {
		return this.tresc;
	}

	public void setTresc(String tresc) {
		this.tresc = tresc;
	}

	public boolean isCalyDzien() {
		return calyDzien;
	}

	public void setCalyDzien(boolean calyDzien) {
		this.calyDzien = calyDzien;
	}

	public Pluton getPlutont() {
		return plutont;
	}

	public void setPlutont(Pluton plutont) {
		this.plutont = plutont;
	}


}