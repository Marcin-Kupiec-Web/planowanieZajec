package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the adnotacje_urzedowe database table.
 * 
 */
@Entity
@Table(name="adnotacje_urzedowe")
@NamedQuery(name="AdnotacjeUrzedowe.findAll", query="SELECT a FROM AdnotacjeUrzedowe a")
public class AdnotacjeUrzedowe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_wydarzenia")
	private int idWydarzenia;

	@Column(name="czas_wydarzenia")
	private Timestamp czasWydarzenia;

	@Column(name="id_pluton")
	private BigInteger idPluton;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="kiedy_usuna")
	private Date kiedyUsuna;

	@Lob
	private String komentarz;

	@Column(name="kto_usuna")
	private int ktoUsuna;

	private String usunieta;

	@Column(name="wpisal_wydarzenie")
	private String wpisalWydarzenie;

	public AdnotacjeUrzedowe() {
	}

	public int getIdWydarzenia() {
		return this.idWydarzenia;
	}

	public void setIdWydarzenia(int idWydarzenia) {
		this.idWydarzenia = idWydarzenia;
	}

	public Timestamp getCzasWydarzenia() {
		return this.czasWydarzenia;
	}

	public void setCzasWydarzenia(Timestamp czasWydarzenia) {
		this.czasWydarzenia = czasWydarzenia;
	}

	public BigInteger getIdPluton() {
		return this.idPluton;
	}

	public void setIdPluton(BigInteger idPluton) {
		this.idPluton = idPluton;
	}

	public Date getKiedyUsuna() {
		return this.kiedyUsuna;
	}

	public void setKiedyUsuna(Date kiedyUsuna) {
		this.kiedyUsuna = kiedyUsuna;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public int getKtoUsuna() {
		return this.ktoUsuna;
	}

	public void setKtoUsuna(int ktoUsuna) {
		this.ktoUsuna = ktoUsuna;
	}

	public String getUsunieta() {
		return this.usunieta;
	}

	public void setUsunieta(String usunieta) {
		this.usunieta = usunieta;
	}

	public String getWpisalWydarzenie() {
		return this.wpisalWydarzenie;
	}

	public void setWpisalWydarzenie(String wpisalWydarzenie) {
		this.wpisalWydarzenie = wpisalWydarzenie;
	}

}