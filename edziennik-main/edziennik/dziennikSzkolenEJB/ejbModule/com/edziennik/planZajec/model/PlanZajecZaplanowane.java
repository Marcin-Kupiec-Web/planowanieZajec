package com.edziennik.planZajec.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the plan_zajec_zaplanowane database table.
 * 
 */
@Entity
@Table(name="plan_zajec_zaplanowane")
@NamedQueries({
@NamedQuery(name="PlanZajecZaplanowane.findAll", query="SELECT p FROM PlanZajecZaplanowane p"),
@NamedQuery(name="PlanZajecZaplanowane.findImieNazwisko", query="SELECT p FROM PlanZajecZaplanowane p WHERE p.imie LIKE :imie AND p.nazwisko LIKE :nazwisko AND p.rok LIKE :rok "),
})
public class PlanZajecZaplanowane implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_plan_zajec")
	private int idPlanZajec;

	@Column(name="czy_usunac")
	private String czyUsunac;

	private int dzien;

	private String godzina;

	@Column(name="godzina_lekcyjna")
	private int godzinaLekcyjna;

	@Column(name="ile_godz_dodatkowe")
	private int ileGodzDodatkowe;

	private String imie;

	private String jm;

	private String js;

	private String klasa;

	@Column(name="klasa_niezatwierdzone")
	private String klasaNiezatwierdzone;

	private int kompania;

	private String kpn;

	@Column(name="kto_wpisal")
	private String ktoWpisal;

	@Column(name="kto_zatwierdzil")
	private String ktoZatwierdzil;

	private String miesiac;

	private String nazwisko;

	@Column(name="opis_dodatkowy")
	private String opisDodatkowy;

	private int pluton;

	@Column(name="rodzaj_zajec")
	private String rodzajZajec;

	@Column(name="rodzaj_zajec_niezatwierdzony")
	private String rodzajZajecNiezatwierdzony;

	private int rok;

	private String sala;

	private int szkolenie;

	private String zatwierdzony;

	public PlanZajecZaplanowane() {
	}

	public int getIdPlanZajec() {
		return this.idPlanZajec;
	}

	public void setIdPlanZajec(int idPlanZajec) {
		this.idPlanZajec = idPlanZajec;
	}

	public String getCzyUsunac() {
		return this.czyUsunac;
	}

	public void setCzyUsunac(String czyUsunac) {
		this.czyUsunac = czyUsunac;
	}

	public int getDzien() {
		return this.dzien;
	}

	public void setDzien(int dzien) {
		this.dzien = dzien;
	}

	public String getGodzina() {
		return this.godzina;
	}

	public void setGodzina(String godzina) {
		this.godzina = godzina;
	}

	public int getGodzinaLekcyjna() {
		return this.godzinaLekcyjna;
	}

	public void setGodzinaLekcyjna(int godzinaLekcyjna) {
		this.godzinaLekcyjna = godzinaLekcyjna;
	}

	public int getIleGodzDodatkowe() {
		return this.ileGodzDodatkowe;
	}

	public void setIleGodzDodatkowe(int ileGodzDodatkowe) {
		this.ileGodzDodatkowe = ileGodzDodatkowe;
	}

	public String getImie() {
		return this.imie;
	}

	public void setImie(String imie) {
		this.imie = imie;
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

	public String getKlasa() {
		return this.klasa;
	}

	public void setKlasa(String klasa) {
		this.klasa = klasa;
	}

	public String getKlasaNiezatwierdzone() {
		return this.klasaNiezatwierdzone;
	}

	public void setKlasaNiezatwierdzone(String klasaNiezatwierdzone) {
		this.klasaNiezatwierdzone = klasaNiezatwierdzone;
	}

	public int getKompania() {
		return this.kompania;
	}

	public void setKompania(int kompania) {
		this.kompania = kompania;
	}

	public String getKpn() {
		return this.kpn;
	}

	public void setKpn(String kpn) {
		this.kpn = kpn;
	}

	public String getKtoWpisal() {
		return this.ktoWpisal;
	}

	public void setKtoWpisal(String ktoWpisal) {
		this.ktoWpisal = ktoWpisal;
	}

	public String getKtoZatwierdzil() {
		return this.ktoZatwierdzil;
	}

	public void setKtoZatwierdzil(String ktoZatwierdzil) {
		this.ktoZatwierdzil = ktoZatwierdzil;
	}

	public String getMiesiac() {
		return this.miesiac;
	}

	public void setMiesiac(String miesiac) {
		this.miesiac = miesiac;
	}

	public String getNazwisko() {
		return this.nazwisko;
	}

	public void setNazwisko(String nazwisko) {
		this.nazwisko = nazwisko;
	}

	public String getOpisDodatkowy() {
		return this.opisDodatkowy;
	}

	public void setOpisDodatkowy(String opisDodatkowy) {
		this.opisDodatkowy = opisDodatkowy;
	}

	public int getPluton() {
		return this.pluton;
	}

	public void setPluton(int pluton) {
		this.pluton = pluton;
	}

	public String getRodzajZajec() {
		return this.rodzajZajec;
	}

	public void setRodzajZajec(String rodzajZajec) {
		this.rodzajZajec = rodzajZajec;
	}

	public String getRodzajZajecNiezatwierdzony() {
		return this.rodzajZajecNiezatwierdzony;
	}

	public void setRodzajZajecNiezatwierdzony(String rodzajZajecNiezatwierdzony) {
		this.rodzajZajecNiezatwierdzony = rodzajZajecNiezatwierdzony;
	}

	public int getRok() {
		return this.rok;
	}

	public void setRok(int rok) {
		this.rok = rok;
	}

	public String getSala() {
		return this.sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public int getSzkolenie() {
		return this.szkolenie;
	}

	public void setSzkolenie(int szkolenie) {
		this.szkolenie = szkolenie;
	}

	public String getZatwierdzony() {
		return this.zatwierdzony;
	}

	public void setZatwierdzony(String zatwierdzony) {
		this.zatwierdzony = zatwierdzony;
	}

}