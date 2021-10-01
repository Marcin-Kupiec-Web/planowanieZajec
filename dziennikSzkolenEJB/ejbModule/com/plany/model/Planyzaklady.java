package com.plany.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.edziennik.sluchacze.zajecia.model.Pluton;
import com.edziennik.sluchacze.zajecia.model.Przedmiottemat;
import com.userManager.model.User;

/**
 * The persistent class for the planyzaklady database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Planyzaklady.findAll", query="SELECT p FROM Planyzaklady p"),
@NamedQuery(name="Planyzaklady.findPoIdPlutonu", query="SELECT p FROM Planyzaklady p WHERE p.pluton.idPluton LIKE :idPluton"),
@NamedQuery(name="Planyzaklady.findPoRok", query="SELECT p FROM Planyzaklady p WHERE p.pluton.archiwum LIKE 'NIE' AND p.rok LIKE :rok"),
@NamedQuery(name="Planyzaklady.findAllNotArche", query="SELECT p FROM Planyzaklady p WHERE p.pluton.archiwum LIKE 'NIE' AND p.miesiac LIKE :miesiac AND p.rok LIKE :rok ORDER BY p.przedmiottemat.przedmiot.jm,p.przedmiottemat.przedmiot.js,p.przedmiottemat.nazwa"),
})
public class Planyzaklady implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id_planyZaklady;

	@Temporal(TemporalType.DATE)
	@Column(name="data_wpisu")
	private Date dataWpisu;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_pluton")
	private Pluton pluton;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_przedmiotTemat")
	private Przedmiottemat przedmiottemat;
	
	//bi-directional many-to-many association to Zaklad
		@ManyToMany
		@JoinTable(
			name="planyzaklady_users"
			, joinColumns={
				@JoinColumn(name="id_planyzaklady")
				}
			, inverseJoinColumns={
				@JoinColumn(name="id_users")
				}
			)
		private List<User> userss;

	@Lob
	private String komentarz;

	private Integer miesiac;
	
	private Integer rok;

	private int wpisal;
	
	@Column(name="ile_godzin")
	private int ileGodzin;
	
	public Planyzaklady() {
	}

	public int getId_planyZaklady() {
		return this.id_planyZaklady;
	}

	public void setId_planyZaklady(int id_planyZaklady) {
		this.id_planyZaklady = id_planyZaklady;
	}

	public Date getDataWpisu() {
		return this.dataWpisu;
	}

	public void setDataWpisu(Date dataWpisu) {
		this.dataWpisu = dataWpisu;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public Integer getMiesiac() {
		return this.miesiac;
	}

	public void setMiesiac(Integer miesiac) {
		this.miesiac = miesiac;
	}

	public int getWpisal() {
		return this.wpisal;
	}

	public void setWpisal(int wpisal) {
		this.wpisal = wpisal;
	}
	public Pluton getPluton() {
		return pluton;
	}

	public void setPluton(Pluton pluton) {
		this.pluton = pluton;
	}
	
	public int getIleGodzin() {
		return ileGodzin;
	}

	public void setIleGodzin(int ileGodzin) {
		this.ileGodzin = ileGodzin;
	}

	public Integer getRok() {
		return rok;
	}

	public void setRok(Integer rok) {
		this.rok = rok;
	}
	public Przedmiottemat getPrzedmiottemat() {
		return przedmiottemat;
	}

	public void setPrzedmiottemat(Przedmiottemat przedmiottemat) {
		this.przedmiottemat = przedmiottemat;
	}
	public List<User> getUserss() {
		return userss;
	}

	public void setUserss(List<User> userss) {
		this.userss = userss;
	}
}