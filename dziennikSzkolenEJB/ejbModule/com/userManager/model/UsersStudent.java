package com.userManager.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import com.edziennik.sluchacze.zajecia.model.Sluchacze;
import com.edziennik.wiadomosci.model.Wiadomosci;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the users_students database table.
 * 
 */
@Entity
@Table(name="users_students")
@NamedQueries({
	@NamedQuery(name="findUserStudentLogin", query="SELECT u FROM UsersStudent u WHERE u.loginUsers LIKE :loginUsers"),
	@NamedQuery(name="findAllUserStudentCount", query="SELECT u FROM UsersStudent u WHERE u.sluchacze.pluton.archiwum LIKE 'NIE' ORDER BY u.sluchacze.nazwiskoSluchacz"),
	@NamedQuery(name="findAllUserStudent", query="SELECT u FROM UsersStudent u WHERE u.sluchacze.pluton.archiwum LIKE :archiwum ORDER BY u.sluchacze.nazwiskoSluchacz"),
	@NamedQuery(name="loginUserStudent", query="SELECT u FROM UsersStudent u WHERE u.loginUsers LIKE :loginUsers AND u.hasloUsers LIKE :hasloUsers AND u.zablokowany LIKE 'NIE'"),
	@NamedQuery(name="findUserStudent", query="SELECT u FROM UsersStudent u WHERE u.sluchacze.pluton.archiwum LIKE :archiwum AND (u.loginUsers LIKE :loginUsers OR u.sluchacze.idS LIKE :idSluchacz OR u.sluchacze.nazwiskoSluchacz LIKE :nazwiskoUsers OR u.sluchacze.imieSluchacz LIKE :imieUsers OR u.statusUsers LIKE :statusUsers OR u.zakladUsers LIKE :zakladUsers) ORDER BY u.nazwiskoUsers"),
	@NamedQuery(name="loginUserIfRepeatStudent", query="SELECT u FROM UsersStudent u WHERE u.loginUsers LIKE :loginUsers"),
	@NamedQuery(name="findUserPoIdus", query="SELECT DISTINCT u FROM UsersStudent u WHERE u.idUsers LIKE :idUsers"),
})
public class UsersStudent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_users")
	private int idUsers;

	private byte editAble;

	@Column(name="haslo_users")
	private String hasloUsers;

	private int id;

@OneToOne
	@JoinColumn(name="id_sluchacz",referencedColumnName="id_s")
	private Sluchacze sluchacze;

@OneToMany(mappedBy="userStudent", fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
private List<Wiadomosci> wiadomoscis;

	private int idw;

	@Column(name="imie_users")
	private String imieUsers;

	@Column(name="ip_komputera")
	private String ipKomputera;

	private String kompania;
	
	private Boolean usuniety;
	
	@Column(name="login_users", unique=true)
	private String loginUsers;

	@Column(name="nazwisko_users")
	private String nazwiskoUsers;

	@Column(name="ostatnie_logowanie")
	private Timestamp ostatnieLogowanie;
	@Column(name="errLog",columnDefinition = "int default 0")
	private int errLog;
	
	private Timestamp whenErrLog;
	
	private String pluton;

	@Column(name="sesja_id")
	private String sesjaId;

	@Column(name="status_users")
	private String statusUsers;
	
	private String poziomUprawnien;
	
	private String szkolenie;

	private String zablokowany;

	@Column(name="zaklad_users")
	private String zakladUsers;

	@Temporal(TemporalType.DATE)
	@Column(name="zmiana_hasla")
	private Date zmianaHasla;

	public UsersStudent() {
	}

	public int getIdUsers() {
		return this.idUsers;
	}

	public void setIdUsers(int idUsers) {
		this.idUsers = idUsers;
	}

	public byte getEditAble() {
		return this.editAble;
	}

	public void setEditAble(byte editAble) {
		this.editAble = editAble;
	}

	public String getHasloUsers() {
		return this.hasloUsers;
	}

	public void setHasloUsers(String hasloUsers) {
		this.hasloUsers = hasloUsers;
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

	public String getImieUsers() {
		return this.imieUsers;
	}

	public void setImieUsers(String imieUsers) {
		this.imieUsers = imieUsers;
	}

	public String getIpKomputera() {
		return this.ipKomputera;
	}

	public List<Wiadomosci> getWiadomoscis() {
		return wiadomoscis;
	}

	public void setWiadomoscis(List<Wiadomosci> wiadomoscis) {
		this.wiadomoscis = wiadomoscis;
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

	public String getPoziomUprawnien() {
		return poziomUprawnien;
	}

	public void setPoziomUprawnien(String poziomUprawnien) {
		this.poziomUprawnien = poziomUprawnien;
	}
	 public Sluchacze getSluchacze() {
			return sluchacze;
		}

		public void setSluchacze(Sluchacze sluchacze) {
			this.sluchacze = sluchacze;
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