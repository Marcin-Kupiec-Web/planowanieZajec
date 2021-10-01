package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the struktura_kursu database table.
 * 
 */
@Entity
@Table(name="struktura_kursu")
@NamedQuery(name="StrukturaKursu.findAll", query="SELECT s FROM StrukturaKursu s")
public class StrukturaKursu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_struktura_kursu")
	private int idStrukturaKursu;

	@Column(name="ile_godzin")
	private int ileGodzin;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="kiedy_wpisal")
	private Date kiedyWpisal;

	private String komentarz;

	@Column(name="kto_wpisal")
	private int ktoWpisal;

	private String kurs;
	
	private String kolor;

	private String jModol;

	private String jSzkol;

	private String kpn;
	
	private String nazwa;

	//bi-directional many-to-one association to Przedmiot
	@OneToMany(mappedBy="strukturaKursu", cascade=CascadeType.REMOVE)
	private List<Przedmiot> przedmiots;

	//bi-directional many-to-one association to Pluton
	@OneToMany(mappedBy="idKurs")
	private List<Pluton> plutons;
	
	public List<Pluton> getPlutons() {
		return plutons;
	}

	public void setPlutons(List<Pluton> plutons) {
		this.plutons = plutons;
	}

	public StrukturaKursu() {
	}

	public int getIdStrukturaKursu() {
		return this.idStrukturaKursu;
	}

	public void setIdStrukturaKursu(int idStrukturaKursu) {
		this.idStrukturaKursu = idStrukturaKursu;
	}

	public int getIleGodzin() {
		return this.ileGodzin;
	}

	public void setIleGodzin(int ileGodzin) {
		this.ileGodzin = ileGodzin;
	}

	public Date getKiedyWpisal() {
		return this.kiedyWpisal;
	}

	public void setKiedyWpisal(Date kiedyWpisal) {
		this.kiedyWpisal = kiedyWpisal;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public int getKtoWpisal() {
		return this.ktoWpisal;
	}

	public void setKtoWpisal(int ktoWpisal) {
		this.ktoWpisal = ktoWpisal;
	}

	public String getKurs() {
		return this.kurs;
	}

	public void setKurs(String kurs) {
		this.kurs = kurs;
	}


	public List<Przedmiot> getPrzedmiots() {
		return this.przedmiots;
	}

	public void setPrzedmiots(List<Przedmiot> przedmiots) {
		this.przedmiots = przedmiots;
	}

	public Przedmiot addPrzedmiot(Przedmiot przedmiot) {
		getPrzedmiots().add(przedmiot);
		przedmiot.setStrukturaKursu(this);

		return przedmiot;
	}

	public Przedmiot removePrzedmiot(Przedmiot przedmiot) {
		getPrzedmiots().remove(przedmiot);
		przedmiot.setStrukturaKursu(null);

		return przedmiot;
	}

	public String getjModol() {
		return jModol;
	}

	public void setjModol(String jModol) {
		this.jModol = jModol;
	}

	public String getjSzkol() {
		return jSzkol;
	}

	public void setjSzkol(String jSzkol) {
		this.jSzkol = jSzkol;
	}

	public String getKpn() {
		return kpn;
	}

	public void setKpn(String kpn) {
		this.kpn = kpn;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public String getKolor() {
		return kolor;
	}

	public void setKolor(String kolor) {
		this.kolor = kolor;
	}

}