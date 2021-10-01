package com.edziennik.wiadomosci.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.edziennik.sluchacze.zajecia.model.Sluchacze;
import com.userManager.model.User;
import com.userManager.model.UsersStudent;


/**
 * The persistent class for the Wiadomosci database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Wiadomosci.findAll", query="SELECT t FROM Wiadomosci t "),
	@NamedQuery(name="Wiadomosci.findAllowed", query="SELECT t FROM Wiadomosci t WHERE t.ktoWpisal LIKE :ktoWpisal OR t.doWiadomosci LIKE 'wszyscy' OR t.doWiadomosci LIKE :zaklad")
})
public class Wiadomosci implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_wiadomosc")
	private int idWiadomosc;


	@Column(name="data_wpisu")
	private Timestamp dataWpisu;

	private String doWiadomosci;
	
	private boolean przeczytane;

	@ManyToOne
	@JoinColumn(name="id_pluton")
	private Pluton pluton;
	
	@ManyToMany
	@JoinTable(
		name="wiadomosci_sluchaczOdbiorca"
		, joinColumns={
			@JoinColumn(name="id_wiadomosc")
			}
		, inverseJoinColumns={
			@JoinColumn(name="id_s")
			}
		)
	private List<Sluchacze> sluchaczOdbiorca;
	
	@ManyToOne
	@JoinColumn(name="id_users")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="id_userStudent")
	private UsersStudent userStudent;
	
	@Column(name="kto_wpisal")
	private int ktoWpisal;

	private String tytul;
	
	@Lob
	private String tresc;

	public Wiadomosci() {
	}

	@ManyToMany
	@JoinTable(
		name="wiadomosci_userOdbiorca"
		, joinColumns={
			@JoinColumn(name="id_wiadomosc")
			}
		, inverseJoinColumns={
			@JoinColumn(name="id_users")
			}
		)
	private List<User> userOdbiorca;

	public Timestamp getDataWpisu() {
		return this.dataWpisu;
	}

	public void setDataWpisu(Timestamp calendar) {
		this.dataWpisu = calendar;
	}

	public String getDoWiadomosci() {
		return this.doWiadomosci;
	}

	public void setDoWiadomosci(String doWiadomosci) {
		this.doWiadomosci = doWiadomosci;
	}

	public int getKtoWpisal() {
		return this.ktoWpisal;
	}

	public void setKtoWpisal(int ktoWpisal) {
		this.ktoWpisal = ktoWpisal;
	}

	public String getTresc() {
		return this.tresc;
	}

	public void setTresc(String tresc) {
		this.tresc = tresc;
	}


	public String getTytul() {
		return tytul;
	}


	public void setTytul(String tytul) {
		this.tytul = tytul;
	}


	public int getIdWiadomosc() {
		return idWiadomosc;
	}


	public void setIdWiadomosc(int idWiadomosc) {
		this.idWiadomosc = idWiadomosc;
	}


	public Pluton getPluton() {
		return pluton;
	}


	public void setPluton(Pluton pluton) {
		this.pluton = pluton;
	}


	public List<Sluchacze> getSluchaczOdbiorca() {
		return sluchaczOdbiorca;
	}

	public void setSluchaczOdbiorca(List<Sluchacze> sluchaczOdbiorca) {
		this.sluchaczOdbiorca = sluchaczOdbiorca;
	}

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}



	public UsersStudent getUserStudent() {
		return userStudent;
	}


	public void setUserStudent(UsersStudent userStudent) {
		this.userStudent = userStudent;
	}


	public boolean isPrzeczytane() {
		return przeczytane;
	}


	public void setPrzeczytane(boolean przeczytane) {
		this.przeczytane = przeczytane;
	}


	public List<User> getUserOdbiorca() {
		return userOdbiorca;
	}


	public void setUserOdbiorca(List<User> userOdbiorca) {
		this.userOdbiorca = userOdbiorca;
	}

	public String UsertoString() {
		String uor="";
		int i=0;
		for(User u:userOdbiorca) {
			i++;
			uor+=u.getNazwiskoUsers()+" "+u.getImieUsers();
			if(userOdbiorca.size()>i)
				uor+=", ";
		}
		return uor;
	}

	public String SluchacztoString() {
		String sr="";
		int i=0;
		if(sluchaczOdbiorca.size()>0) {
			Sluchacze ssz=sluchaczOdbiorca.get(0);
			sr=ssz.getPluton().getKompania().getNazwaKompania()+" "+ssz.getPluton().getNazwaPluton()+" "+ssz.getPluton().getOznaczenieSzkolenia()+": ";
		};
		for(Sluchacze s:sluchaczOdbiorca) {
			i++;
		sr+=s.getNazwiskoSluchacz()+" "+s.getImieSluchacz();
			if(sluchaczOdbiorca.size()>i)
			sr+=", ";
		}
		return sr;
	}

}