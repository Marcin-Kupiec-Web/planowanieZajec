package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the przedmiot database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Przedmiot.findAll", query="SELECT p FROM Przedmiot p"),
@NamedQuery(name="Przedmiot.findPoJMJSStrukt", query="SELECT p FROM Przedmiot p WHERE p.jm LIKE :jm AND (p.js LIKE :js OR p.js IS NULL) AND p.strukturaKursu.idStrukturaKursu LIKE :idStrukturaKursu")
})
public class Przedmiot implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_przedmiot")
	private int idPrzedmiot;


	@Column(name="ile_godzin")
	private int ileGodzin;

	private String jm;

	private String js;

	private String komentarz;

	//bi-directional many-to-one association to Ocena
		@OneToMany(mappedBy="przedmiot", fetch=FetchType.LAZY)
		private List<OcenyNaglowek> ocenyNagloweks;

	//bi-directional many-to-one association to Przedmiot
	@ManyToOne
	@JoinColumn(name="id_struktura_kursu")
	private StrukturaKursu strukturaKursu;
	
	//bi-directional many-to-one association to OcenaPoprawa
		@OneToMany(mappedBy="przedmiot",cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
		private List<Przedmiottemat> Przedmiottemats;
		


	public Przedmiot() {
	}

	public int getIdPrzedmiot() {
		return this.idPrzedmiot;
	}

	public void setIdPrzedmiot(int idPrzedmiot) {
		this.idPrzedmiot = idPrzedmiot;
	}


	public int getIleGodzin() {
		return this.ileGodzin;
	}

	public void setIleGodzin(int ileGodzin) {
		this.ileGodzin = ileGodzin;
	}

	public String getJm() {
		return this.jm;
	}

	public void setJm(String jm) {
		this.jm = jm;
	}

	public String getJs() {
		return this.js;
	}

	public void setJs(String js) {
		this.js = js;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public StrukturaKursu getStrukturaKursu() {
		return strukturaKursu;
	}

	public void setStrukturaKursu(StrukturaKursu strukturaKursu) {
		this.strukturaKursu = strukturaKursu;
	}
	public List<Przedmiottemat> getPrzedmiottemats() {
		return Przedmiottemats;
	}

	public void setPrzedmiottemats(List<Przedmiottemat> przedmiottemats) {
		Przedmiottemats = przedmiottemats;
	}
	
	public Przedmiottemat addPrzedmiottemat(Przedmiottemat przedmiottemat) {
		getPrzedmiottemats().add(przedmiottemat);
		przedmiottemat.setPrzedmiot(this);

		return przedmiottemat;
	}

	public Przedmiottemat removePrzedmiottemat(Przedmiottemat przedmiottemat) {
		getPrzedmiottemats().remove(przedmiottemat);
		przedmiottemat.setPrzedmiot(null);

		return przedmiottemat;
	}

	public List<OcenyNaglowek> getOcenyNagloweks() {
		return ocenyNagloweks;
	}

	public void setOcenyNagloweks(List<OcenyNaglowek> ocenyNagloweks) {
		this.ocenyNagloweks = ocenyNagloweks;
	}
	
	public OcenyNaglowek addOcenyNglowek(OcenyNaglowek ocenyNaglowek) {
		getOcenyNagloweks().add(ocenyNaglowek);
		ocenyNaglowek.setPrzedmiot(this);

		return ocenyNaglowek;
	}

	public OcenyNaglowek removeOcenyNaglowek(OcenyNaglowek ocenyNglowek) {
		getOcenyNagloweks().remove(ocenyNglowek);
		ocenyNglowek.setPrzedmiot(null);

		return ocenyNglowek;
	}
}