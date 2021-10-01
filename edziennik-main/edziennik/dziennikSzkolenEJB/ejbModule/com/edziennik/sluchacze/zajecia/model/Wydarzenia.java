package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the wydarzenia database table.
 * 
 */
@Entity
@NamedQuery(name="Wydarzenia.findAll", query="SELECT w FROM Wydarzenia w")
public class Wydarzenia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_wydarzenia")
	private int idWydarzenia;

	@Column(name="czas_wydarzenia")
	private Timestamp czasWydarzenia;

	@Column(name="kiedy_usuna")
	private Timestamp kiedyUsuna;

	@Lob
	private String komentarz;

	@Column(name="kto_usuna")
	private int ktoUsuna;

	private String usunieta;

	@Column(name="wpisal_wydarzenie")
	private String wpisalWydarzenie;

	//bi-directional many-to-one association to Pluton
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="id_pluton")
	private Pluton pluton;

	public Wydarzenia() {
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

	public Timestamp getKiedyUsuna() {
		return this.kiedyUsuna;
	}

	public void setKiedyUsuna(Timestamp kiedyUsuna) {
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

	public Pluton getPluton() {
		return this.pluton;
	}

	public void setPluton(Pluton pluton) {
		this.pluton = pluton;
	}

}