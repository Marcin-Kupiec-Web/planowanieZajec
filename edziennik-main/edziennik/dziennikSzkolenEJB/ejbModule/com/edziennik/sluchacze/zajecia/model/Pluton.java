package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.edziennik.terminarz.model.Terminarz;
import com.edziennik.wiadomosci.model.Wiadomosci;
import com.plany.model.Planyzaklady;


/**
 * The persistent class for the pluton database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Pluton.findAll", query="SELECT p FROM Pluton p WHERE p.archiwum LIKE :archiwum AND p.kompania.archiwum LIKE :archiwum"),
@NamedQuery(name="Pluton.findSluchaczePoIdPluton", query="SELECT p FROM Pluton p WHERE p.idPluton LIKE :idPluton"),
@NamedQuery(name="Pluton.findSzkolenia", query="SELECT p FROM Pluton p WHERE p.archiwum LIKE :archiwum AND p.kompania.archiwum LIKE :archiwum ORDER BY p.oznaczenieSzkolenia"),
@NamedQuery(name="Pluton.findOgolnie", query="SELECT p FROM Pluton p WHERE p.archiwum LIKE 'NIE' AND p.kompania.archiwum LIKE 'NIE' AND p.kompania.nazwaKompania LIKE :nazwaKompania "
		+ "AND p.nazwaPluton LIKE :nazwaPluton AND p.oznaczenieSzkolenia LIKE :oznaczenieSzkolenia"),
})
public class Pluton implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_pluton")
	private int idPluton;

	private String archiwum;

	@Column(name="nazwa_pluton")
	private String nazwaPluton;

	@Column(name="oznaczenie_szkolenia")
	private String oznaczenieSzkolenia;

	@Column(name="rodzaj_kursu")
	private String rodzajKursu;

	@Column(name="rok_pluton")
	private int rokPluton;

	@OneToMany(mappedBy="pluton", fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	private List<Wiadomosci> wiadomoscis;

	@OneToMany(mappedBy="plutont", fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	private List<Terminarz> terminarzs;
	
	@Temporal(TemporalType.DATE)
	private Date terminDo;

	@Temporal(TemporalType.DATE)
	private Date terminOd;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_struktura_kursu")
	private StrukturaKursu idKurs;
	
	//bi-directional many-to-one association to Ocena
	@OneToMany(mappedBy="pluton", fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	private List<OcenyNaglowek> ocenyNagloweks;
	
	//bi-directional many-to-one association to Kompania
	@ManyToOne
	@JoinColumn(name="id_kompania")
	private Kompania kompania;

	//bi-directional many-to-one association to Tematy
	@OneToMany(mappedBy="pluton",fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	private List<Tematy> tematies;

	//bi-directional many-to-one association to Wydarzenia
	@OneToMany(mappedBy="pluton",fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	private List<Sluchacze> sluchaczes;
	
	//bi-directional many-to-one association to Tematy
	@OneToMany(mappedBy="pluton",fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	private List<Planyzaklady> planyZakladys;

	//bi-directional many-to-one association to Wydarzenia
	@OneToMany(mappedBy="pluton", fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	private List<Wydarzenia> wydarzenias;

	public Pluton() {
	}

	public int getIdPluton() {
		return this.idPluton;
	}

	public void setIdPluton(int idPluton) {
		this.idPluton = idPluton;
	}

	public String getArchiwum() {
		return this.archiwum;
	}

	public void setArchiwum(String archiwum) {
		this.archiwum = archiwum;
	}

	public String getNazwaPluton() {
		return this.nazwaPluton;
	}

	public void setNazwaPluton(String nazwaPluton) {
		this.nazwaPluton = nazwaPluton;
	}

	public String getOznaczenieSzkolenia() {
		return this.oznaczenieSzkolenia;
	}

	public void setOznaczenieSzkolenia(String oznaczenieSzkolenia) {
		this.oznaczenieSzkolenia = oznaczenieSzkolenia;
	}

	public String getRodzajKursu() {
		return this.rodzajKursu;
	}

	public void setRodzajKursu(String rodzajKursu) {
		this.rodzajKursu = rodzajKursu;
	}

	public int getRokPluton() {
		return this.rokPluton;
	}

	public void setRokPluton(int rokPluton) {
		this.rokPluton = rokPluton;
	}

	public Date getTerminDo() {
		return this.terminDo;
	}

	public void setTerminDo(Date terminDo) {
		this.terminDo = terminDo;
	}

	public Date getTerminOd() {
		return this.terminOd;
	}

	public void setTerminOd(Date terminOd) {
		this.terminOd = terminOd;
	}

	public Kompania getKompania() {
		return this.kompania;
	}

	public void setKompania(Kompania kompania) {
		this.kompania = kompania;
	}


	public List<Tematy> getTematies() {
		return this.tematies;
	}

	public void setTematies(List<Tematy> tematies) {
		this.tematies = tematies;
	}

	public Tematy addTematy(Tematy tematy) {
		getTematies().add(tematy);
		tematy.setPluton(this);

		return tematy;
	}

	public Tematy removeTematy(Tematy tematy) {
		getTematies().remove(tematy);
		tematy.setPluton(null);

		return tematy;
	}

	public List<Wydarzenia> getWydarzenias() {
		return this.wydarzenias;
	}

	public void setWydarzenias(List<Wydarzenia> wydarzenias) {
		this.wydarzenias = wydarzenias;
	}

	public Wydarzenia addWydarzenia(Wydarzenia wydarzenia) {
		getWydarzenias().add(wydarzenia);
		wydarzenia.setPluton(this);

		return wydarzenia;
	}

	public Wydarzenia removeWydarzenia(Wydarzenia wydarzenia) {
		getWydarzenias().remove(wydarzenia);
		wydarzenia.setPluton(null);

		return wydarzenia;
	}
	public Sluchacze addSluchacze(Sluchacze sluchacze) {
		getSluchaczes().add(sluchacze);
		sluchacze.setPluton(this);

		return sluchacze;
	}

	public Sluchacze removeSluchacze(Sluchacze sluchacze) {
		getSluchaczes().remove(sluchacze);
		sluchacze.setPluton(null);

		return sluchacze;
	}
	
	
	public List<Sluchacze> getSluchaczes() {
		return sluchaczes;
	}

	public void setSluchaczes(List<Sluchacze> sluchaczes) {
		this.sluchaczes = sluchaczes;
	}
	public StrukturaKursu getIdKurs() {
		return idKurs;
	}

	public void setIdKurs(StrukturaKursu idKurs) {
		this.idKurs = idKurs;
	}
	public List<Planyzaklady> getPlanyZakladys() {
		return planyZakladys;
	}

	public void setPlanyZakladys(List<Planyzaklady> planyZakladys) {
		this.planyZakladys = planyZakladys;
	}
	
	public Planyzaklady addPlanyzaklady(Planyzaklady planyzaklady) {
		getPlanyZakladys().add(planyzaklady);
		planyzaklady.setPluton(this);
		return planyzaklady;
	}

	public Planyzaklady removePlanyzaklady(Planyzaklady planyzaklady) {
		getPlanyZakladys().remove(planyzaklady);
		planyzaklady.setPluton(null);
		return planyzaklady;
	}

	public List<OcenyNaglowek> getOcenyNagloweks() {
		return ocenyNagloweks;
	}

	public void setOcenyNagloweks(List<OcenyNaglowek> ocenyNagloweks) {
		this.ocenyNagloweks = ocenyNagloweks;
	}
	
	public OcenyNaglowek addOcenyNglowek(OcenyNaglowek ocenyNaglowek) {
		getOcenyNagloweks().add(ocenyNaglowek);
		ocenyNaglowek.setPluton(this);

		return ocenyNaglowek;
	}

	public OcenyNaglowek removeOcenyNaglowek(OcenyNaglowek ocenyNglowek) {
		getOcenyNagloweks().remove(ocenyNglowek);
		ocenyNglowek.setPluton(null);

		return ocenyNglowek; 
	}

	public List<Wiadomosci> getWiadomoscis() {
		return wiadomoscis;
	}

	public void setWiadomoscis(List<Wiadomosci> wiadomoscis) {
		this.wiadomoscis = wiadomoscis;
	}

	public List<Terminarz> getTerminarzs() {
		return terminarzs;
	}

	public void setTerminarzs(List<Terminarz> terminarzs) {
		this.terminarzs = terminarzs;
	}
}