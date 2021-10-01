package com.userManager.model;

import java.io.Serializable;
import java.sql.Timestamp;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.edziennik.sluchacze.zajecia.model.Ocena;
import com.edziennik.sluchacze.zajecia.model.Specjalizacja;
import com.edziennik.sluchacze.zajecia.model.Zaklad;
import com.edziennik.wiadomosci.model.Wiadomosci;
import com.plany.model.Planyzaklady;
import com.plany.model.UserBlokadaPlany;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(name="findAllUser", query="SELECT u FROM User u ORDER BY u.nazwiskoUsers"),
	@NamedQuery(name="findAllUserCount", query="SELECT u FROM User u WHERE u.zablokowany LIKE 'NIE'"),
	@NamedQuery(name="findUserLogin", query="SELECT u FROM User u WHERE u.loginUsers LIKE :loginUsers"),
	@NamedQuery(name="findAllUserNotBlock", query="SELECT u FROM User u WHERE u.zablokowany LIKE 'NIE' ORDER BY u.nazwiskoUsers"),
	@NamedQuery(name="findAllUserNiezabl", query="SELECT u FROM User u WHERE u.zablokowany LIKE 'NIE' ORDER BY u.nazwiskoUsers"),
	@NamedQuery(name="loginUser", query="SELECT u FROM User u WHERE u.loginUsers LIKE :loginUsers AND u.hasloUsers LIKE :hasloUsers AND u.zablokowany LIKE 'NIE'"),
	@NamedQuery(name="findUser", query="SELECT u FROM User u WHERE u.loginUsers LIKE :loginUsers OR u.idSluchacz LIKE :idSluchacz OR u.nazwiskoUsers LIKE :nazwiskoUsers OR u.imieUsers LIKE :imieUsers OR u.statusUsers LIKE :statusUsers OR u.zakladUsers LIKE :zakladUsers ORDER BY u.nazwiskoUsers"),
	@NamedQuery(name="findUserPoId", query="SELECT DISTINCT u FROM User u WHERE u.idUsers LIKE :idUsers"),
	@NamedQuery(name="findUserMail.findAll", query="SELECT DISTINCT t FROM Wiadomosci t INNER JOIN t.userOdbiorca u WHERE t.dataWpisu > :dateFirst AND u.idUsers LIKE :idUsers"),
	@NamedQuery(name="findUserMailOut.findAll", query="SELECT DISTINCT t FROM Wiadomosci t  WHERE t.dataWpisu > :dateFirst AND t.ktoWpisal LIKE :idUsers"),
	@NamedQuery(name="findUserPoZaklad", query="SELECT DISTINCT u FROM User u WHERE u.zakladUsers LIKE :zakladUsers"),
	@NamedQuery(name="findZakladPoUsers", query="SELECT DISTINCT u.zakladUsers FROM User u"),
	@NamedQuery(name="loginUserIfRepeat", query="SELECT DISTINCT u FROM User u WHERE u.loginUsers LIKE :loginUsers"),
})
//@NamedQuery(name="Wiadomosci.findAll", query="SELECT t FROM Wiadomosci t WHERE t.dataWpisu > function('ADD_MONTH', CURRENT_DATE, -6) "),
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_users")
	private int idUsers;

	@Column(name="haslo_users")
	private String hasloUsers;
 
	@Column(name="id_sluchacz",columnDefinition = "int default 0")
	private int idSluchacz;

	@Column(name="imie_users")
	private String imieUsers;
 
	@Column(name="ip_komputera")
	private String ipKomputera;

	private String kompania;

	@Column(name="login_users", unique=true)
	private String loginUsers;

	@Column(name="nazwisko_users")
	private String nazwiskoUsers;

	@Column(name="ostatnie_logowanie")
	private Timestamp ostatnieLogowanie;

	private String pluton;
	
	private Boolean usuniety;

	@Column(name="sesja_id")
	private String sesjaId;

	@Column(name="status_users")
	private String statusUsers;
	
	private String poziomUprawnien;
	
	private String szkolenie;

	private String zablokowany;

	@Column(name="zaklad_users")
	private String zakladUsers;
	
	@Column(name="errLog",columnDefinition = "int default 0")
	private int errLog;
	
	private Timestamp whenErrLog;
	
	@Temporal(TemporalType.DATE)
	@Column(name="zmiana_hasla")
	private Date zmianaHasla;
	
	@Column(name="id")
	private Integer id;
	
	@Column(name="idw")
	private Integer idw;
	
	//bi-directional many-to-many association to Zaklad
	@ManyToMany
	@JoinTable(
		name="zaklad_users"
		, joinColumns={
			@JoinColumn(name="id_users")
			}
		, inverseJoinColumns={
			@JoinColumn(name="id_zaklad")
			}
		)
	private List<Zaklad> zaklads;
	
	//bi-directional many-to-many association to Przedmiottemat
	@ManyToMany(mappedBy="userss")
	private List<Planyzaklady> playnZakladys; 
	
	//bi-directional many-to-many association to Zaklad
	@ManyToMany
	@JoinTable(
		name="specjalizacja_users"
		, joinColumns={
			@JoinColumn(name="id_users")
			}
		, inverseJoinColumns={
			@JoinColumn(name="id_specjalizacja")
			}
		)
	private List<Specjalizacja> specjalizacjas;
	
	@OneToMany(mappedBy="idUzytkownik",cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private List<Ocena> ocenas;
	
	//bi-directional many-to-one association to Tematy
   @OneToMany(mappedBy="users",cascade=CascadeType.REMOVE)
			private List<UserBlokadaPlany> userBlokadaPlanys;
			
	@OneToMany(mappedBy="user", fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
		private List<Wiadomosci> wiadomoscis;

	@ManyToMany(mappedBy="userOdbiorca")
	private List<Wiadomosci> WiadomosciUserOdbiorca;
	
	public List<Specjalizacja> getSpecjalizacjas() {
		return specjalizacjas;
	}
	@Transient
	private String specjalizacjaToString;
	

	@Transient
	private String zakladToString;
	
	public String getZakladToString() {		
		String str="";
		for(Zaklad zak:zaklads){
			str+=zak.getNazwaSkrot()+", ";
		}
		str = str.substring(0, str.length() - 2);
		return str;
	}


	public String getSpecjalizacjaToString() {
		String str="";
		for(Specjalizacja spec:specjalizacjas){
			str+=spec.getNazwa()+", ";
		}
		str = str.substring(0, str.length() - 2);
		return str;
	}

	public void setSpecjalizacjaToString(String specjalizacjaToString) {
		this.specjalizacjaToString = specjalizacjaToString;
	}


	public void setZakladToString(String zakladToString) {
		this.zakladToString = zakladToString;
	}
	public void setSpecjalizacjas(List<Specjalizacja> specjalizacjas) {
		this.specjalizacjas = specjalizacjas;
	}

	public List<Zaklad> getZaklads() {
		return zaklads;
	}

	public void setZaklads(List<Zaklad> zaklads) {
		this.zaklads = zaklads;
	}

	private boolean editAble;
	
	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getIdw() {
		return this.idw;
	}

	public void setIdw(int idw) {
		this.idw = idw;
	}
	public int getIdUsers() {
		return this.idUsers;
	}

	public void setIdUsers(int idUsers) {
		this.idUsers = idUsers;
	}

	public String getHasloUsers() {
		return this.hasloUsers;
	}

	public void setHasloUsers(String hasloUsers) {
		this.hasloUsers = hasloUsers;
	}

	public int getIdSluchacz() {
		return this.idSluchacz;
	}

	public void setIdSluchacz(int idSluchacz) {
		this.idSluchacz = idSluchacz;
	}

	public String getImieUsers() {
		return this.imieUsers;
	}

	public void setImieUsers(String imieUsers) {
		this.imieUsers = imieUsers;
	}

	public String getIpKomputera() {
		return this.ipKomputera;
	}

	public void setIpKomputera(String ipKomputera) {
		this.ipKomputera = ipKomputera;
	}

	public String getKompania() {
		return this.kompania;
	}

	public void setKompania(String kompania) {
		this.kompania = kompania;
	}

	public String getLoginUsers() {
		return this.loginUsers;
	}

	public void setLoginUsers(String loginUsers) {
		this.loginUsers = loginUsers;
	}

	public String getNazwiskoUsers() {
		return this.nazwiskoUsers;
	}

	public void setNazwiskoUsers(String nazwiskoUsers) {
		this.nazwiskoUsers = nazwiskoUsers;
	}

	public Timestamp getOstatnieLogowanie() {
		return this.ostatnieLogowanie;
	}

	public void setOstatnieLogowanie(Timestamp ostatnieLogowanie) {
		this.ostatnieLogowanie = ostatnieLogowanie;
	}

	public String getPluton() {
		return this.pluton;
	}



	public void setPluton(String pluton) {
		this.pluton = pluton;
	}

	public String getSesjaId() {
		return this.sesjaId;
	}

	public void setSesjaId(String sesjaId) {
		this.sesjaId = sesjaId;
	}

	public String getStatusUsers() {
		return this.statusUsers;
	}

	public void setStatusUsers(String statusUsers) {
		this.statusUsers = statusUsers;
	}

	public String getSzkolenie() {
		return this.szkolenie;
	}

	public void setSzkolenie(String szkolenie) {
		this.szkolenie = szkolenie;
	}

	public String getZablokowany() {
		return this.zablokowany;
	}

	public void setZablokowany(String zablokowany) {
		this.zablokowany = zablokowany;
	}

	public String getZakladUsers() {
		return this.zakladUsers;
	}

	public void setZakladUsers(String zakladUsers) {
		this.zakladUsers = zakladUsers;
	}

	public Date getZmianaHasla() {
		return this.zmianaHasla;
	}

	public void setZmianaHasla(Date zmianaHasla) {
		this.zmianaHasla = zmianaHasla;
	}

	public boolean getEditAble() {
		return editAble;
	}

	public void setEditAble(boolean editAble) {
		this.editAble = editAble;
	}

	public String getPoziomUprawnien() {
		return poziomUprawnien;
	}

	public void setPoziomUprawnien(String poziomUprawnien) {
		this.poziomUprawnien = poziomUprawnien;
	}
	public List<Planyzaklady> getPlaynZakladys() {
		return playnZakladys;
	}

	public void setPlaynZakladys(List<Planyzaklady> playnZakladys) {
		this.playnZakladys = playnZakladys;
	}

public String toString() {
	return this.nazwiskoUsers;
}
public List<UserBlokadaPlany> getUserBlokadaPlanys() {
	return userBlokadaPlanys;
}

public void setUserBlokadaPlanys(List<UserBlokadaPlany> userBlokadaPlanys) {
	this.userBlokadaPlanys = userBlokadaPlanys;
}

public UserBlokadaPlany addUserBlokadaPlany(UserBlokadaPlany userBlokadaPlany) {
	getUserBlokadaPlanys().add(userBlokadaPlany);
	userBlokadaPlany.setUsers(this);
	return userBlokadaPlany;
}

public UserBlokadaPlany removeUserBlokadaPlany(UserBlokadaPlany userBlokadaPlany) {
	getUserBlokadaPlanys().remove(userBlokadaPlany);
	userBlokadaPlany.setUsers(null);
	return userBlokadaPlany;
}

public List<Ocena> getOcenas() {
	return ocenas;
}

public void setOcenas(List<Ocena> ocenas) {
	this.ocenas = ocenas;
}

public List<Wiadomosci> getWiadomoscis() {
	return wiadomoscis;
}

public void setWiadomoscis(List<Wiadomosci> wiadomoscis) {
	this.wiadomoscis = wiadomoscis;
}

public List<Wiadomosci> getWiadomosciUserOdbiorca() {
	return WiadomosciUserOdbiorca;
}

public void setWiadomosciUserOdbiorca(List<Wiadomosci> wiadomosciUserOdbiorca) {
	WiadomosciUserOdbiorca = wiadomosciUserOdbiorca;
}

public int getErrLog() {
	return errLog;
}

public void setErrLog(int errLog) {
	this.errLog = errLog;
}

public Timestamp getWhenErrLog() {
	return whenErrLog;
}

public void setWhenErrLog(Timestamp whenErrLog) {
	this.whenErrLog = whenErrLog;
}


public Boolean getUsuniety() {
	return usuniety;
}


public void setUsuniety(Boolean usuniety) {
	this.usuniety = usuniety;
}
}