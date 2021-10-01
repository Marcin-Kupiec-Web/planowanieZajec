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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.edziennik.wiadomosci.model.Wiadomosci;
import com.userManager.model.UsersStudent;


/**
 * The persistent class for the sluchacze database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Sluchacze.findSluchaczeObecnosc", query="SELECT s FROM Sluchacze s LEFT JOIN s.obecnoscs o WHERE o.data LIKE :data AND s.pluton.idPluton LIKE :idPluton ORDER BY s.nrDziennik"),
	@NamedQuery(name="Sluchacze.findAllSluchacze", query="SELECT s FROM Sluchacze s WHERE s.pluton.archiwum LIKE 'NIE' ORDER BY s.nazwiskoSluchacz"),
	@NamedQuery(name="Sluchacze.findPoId", query="SELECT s FROM Sluchacze s WHERE s.idSluchacz LIKE :idSluchacz"),
	@NamedQuery(name="Sluchacze.findSluchaczeFinder", query="SELECT s FROM Sluchacze s WHERE s.pluton.nazwaPluton LIKE :nazwaPluton AND s.pluton.kompania.nazwaKompania LIKE :nazwaKompania AND "
			+ "s.pluton.oznaczenieSzkolenia LIKE :oznaczenieSzkolenia AND s.stopienSluchacz LIKE :stopienSluchacz AND s.jednostkaKierujaca LIKE :jednostkaKierujaca AND s.imieSluchacz LIKE :imieSluchacz "
			+ "AND s.nazwiskoSluchacz LIKE :nazwiskoSluchacz AND CONVERT(s.idSluchacz,char) LIKE :idSluchacz AND s.pluton.archiwum LIKE :archiwum AND s.pokoj LIKE :pokoj ORDER BY s.nazwiskoSluchacz")
}) 
public class Sluchacze implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_s")
	private int idS;
	
@OneToOne(mappedBy="sluchacze",cascade=CascadeType.REMOVE)
private UsersStudent usersStudent;

@ManyToMany(mappedBy="sluchaczOdbiorca")
private List<Wiadomosci> WiadomosciSluchaczOdbiorca;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_remove")
	private Date dataRemove;
	
	@Column(name="user_remove")
	private Integer userRemove;
	
	@Column(name="id_sluchacz")
	private int idSluchacz;

	@Column(name="imie_sluchacz")
	private String imieSluchacz;

	@Column(name="jednostka_kierujaca")
	private String jednostkaKierujaca;

	@Column(name="nazwisko_sluchacz")
	private String nazwiskoSluchacz;

	@Column(name="nr_dziennik")
	private String nrDziennik;

	@Column(name="stanowisko_sluchacz")
	private String stanowiskoSluchacz;

	@Column(name="staz_sluzby_lata")
	private int stazSluzbyLata;

	@Column(name="staz_sluzby_miesiace")
	private int stazSluzbyMiesiace;

	@Column(name="staz_stanowisko_lata")
	private int stazStanowiskoLata;

	@Column(name="staz_stanowisko_miesiace")
	private int stazStanowiskoMiesiace;

	@Column(name="stopien_sluchacz")
	private String stopienSluchacz;

	private String usuniety;

	private String pokoj;
	
	//bi-directional many-to-one association to Obecnosc
	@OneToMany(mappedBy="sluchacze",cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private List<Obecnosc> obecnoscs;

	//bi-directional many-to-one association to Ocena
	@OneToMany(mappedBy="sluchacze",cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private List<Ocena> ocenas;

	//bi-directional many-to-one association to Pluton
//(cascade={CascadeType.ALL})
	@ManyToOne
	@JoinColumn(name="id_pluton")
	private Pluton pluton;
	
	public Sluchacze() {
	}

	public int getIdS() {
		return this.idS;
	}

	public void setIdS(int idS) {
		this.idS = idS;
	}

	public int getIdSluchacz() {
		return this.idSluchacz;
	}

	public void setIdSluchacz(int idSluchacz) {
		this.idSluchacz = idSluchacz;
	}

	public String getImieSluchacz() {
		return this.imieSluchacz;
	}

	public void setImieSluchacz(String imieSluchacz) {
		this.imieSluchacz = imieSluchacz;
	}

	public String getJednostkaKierujaca() {
		return this.jednostkaKierujaca;
	}

	public void setJednostkaKierujaca(String jednostkaKierujaca) {
		this.jednostkaKierujaca = jednostkaKierujaca;
	}

	public String getNazwiskoSluchacz() {
		return this.nazwiskoSluchacz;
	}

	public void setNazwiskoSluchacz(String nazwiskoSluchacz) {
		this.nazwiskoSluchacz = nazwiskoSluchacz;
	}

	public String getNrDziennik() {
		return this.nrDziennik;
	}

	public void setNrDziennik(String nrDziennik) {
		this.nrDziennik = nrDziennik;
	}

	public String getStanowiskoSluchacz() {
		return this.stanowiskoSluchacz;
	}

	public void setStanowiskoSluchacz(String stanowiskoSluchacz) {
		this.stanowiskoSluchacz = stanowiskoSluchacz;
	}

	public int getStazSluzbyLata() {
		return this.stazSluzbyLata;
	}

	public void setStazSluzbyLata(int stazSluzbyLata) {
		this.stazSluzbyLata = stazSluzbyLata;
	}

	public int getStazSluzbyMiesiace() {
		return this.stazSluzbyMiesiace;
	}

	public void setStazSluzbyMiesiace(int stazSluzbyMiesiace) {
		this.stazSluzbyMiesiace = stazSluzbyMiesiace;
	}

	public int getStazStanowiskoLata() {
		return this.stazStanowiskoLata;
	}

	public void setStazStanowiskoLata(int stazStanowiskoLata) {
		this.stazStanowiskoLata = stazStanowiskoLata;
	}

	public int getStazStanowiskoMiesiace() {
		return this.stazStanowiskoMiesiace;
	}

	public void setStazStanowiskoMiesiace(int stazStanowiskoMiesiace) {
		this.stazStanowiskoMiesiace = stazStanowiskoMiesiace;
	}

	public String getStopienSluchacz() {
		return this.stopienSluchacz;
	}

	public void setStopienSluchacz(String stopienSluchacz) {
		this.stopienSluchacz = stopienSluchacz;
	}

	public String getUsuniety() {
		return this.usuniety;
	}

	public void setUsuniety(String usuniety) {
		this.usuniety = usuniety;
	}

	public List<Obecnosc> getObecnoscs() {
		return this.obecnoscs;
	}

	public void setObecnoscs(List<Obecnosc> obecnoscs) {
		this.obecnoscs = obecnoscs;
	}

	public Obecnosc addObecnosc(Obecnosc obecnosc) {
		getObecnoscs().add(obecnosc);
		obecnosc.setSluchacze(this);
		return obecnosc;
	}

	public Obecnosc removeObecnosc(Obecnosc obecnosc) {
		getObecnoscs().remove(obecnosc);
		obecnosc.setSluchacze(null);
		return obecnosc;
	}


	public Pluton getPluton() {
		return this.pluton;
	}

	public void setPluton(Pluton pluton) {
		this.pluton = pluton;
	}


	public List<Ocena> getOcenas() {
		return this.ocenas;
	}

	public void setOcenas(List<Ocena> ocenas) {
		this.ocenas = ocenas;
	}

	public Ocena addOcena(Ocena ocena) {
		getOcenas().add(ocena);
		ocena.setSluchacze(this);

		return ocena;
	}

	public Ocena removeOcena(Ocena ocena) {
		getOcenas().remove(ocena);
		ocena.setSluchacze(null);

		return ocena;
	}

	public UsersStudent getUsersStudent() {
	return usersStudent;
}

public void setUsersStudent(UsersStudent usersStudent) {
	this.usersStudent = usersStudent;
}

public Integer getUserRemove() {
	return userRemove;
}

public void setUserRemove(Integer userRemove) {
	this.userRemove = userRemove;
}

public Date getDataRemove() {
	return dataRemove;
}

public void setDataRemove(Date dataRemove) {
	this.dataRemove = dataRemove;
}

public List<Wiadomosci> getWiadomosciSluchaczOdbiorca() {
	return WiadomosciSluchaczOdbiorca;
}

public void setWiadomosciSluchaczOdbiorca(List<Wiadomosci> wiadomosciSluchaczOdbiorca) {
	WiadomosciSluchaczOdbiorca = wiadomosciSluchaczOdbiorca;
}

public String getPokoj() {
	return pokoj;
}

public void setPokoj(String pokoj) {
	this.pokoj = pokoj;
}


}