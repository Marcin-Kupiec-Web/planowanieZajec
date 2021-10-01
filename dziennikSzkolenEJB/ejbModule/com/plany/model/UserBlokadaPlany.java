package com.plany.model;

import java.io.Serializable;
import javax.persistence.*;

import com.edziennik.sluchacze.zajecia.model.StrukturaKursu;
import com.userManager.model.User;

import java.util.Calendar;
import java.util.Date;


/**
 * The persistent class for the user_blokada_plany database table.
 * 
 */
@Entity
@Table(name="user_blokada_plany")
@NamedQuery(name="UserBlokadaPlany.findAll", query="SELECT u FROM UserBlokadaPlany u")
public class UserBlokadaPlany implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_user_blokada_plany")
	private int idUserBlokadaPlany;

	private boolean calyDzien;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dzienOd;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dzienDo;
	
	public Date getDzienOd() { 
		return dzienOd;
	}

	public void setDzienOd(Date dzienOd) {
		this.dzienOd = dzienOd;
	}

	public Date getDzienDo() {
		return dzienDo;
	}

	public void setDzienDo(Date dzienDo) {
		this.dzienDo = dzienDo;
	}

	@Lob
	private String komentarz;

	public UserBlokadaPlany() {
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_users")
	private User users;
	
	public User getUsers() {
		return users;
	}

	public void setUsers(User users) {
		this.users = users;
	}

	public int getIdUserBlokadaPlany() {
		return this.idUserBlokadaPlany;
	}

	public void setIdUserBlokadaPlany(int idUserBlokadaPlany) {
		this.idUserBlokadaPlany = idUserBlokadaPlany;
	}

	public boolean getCalyDzien() {
		return this.calyDzien;
	}

	public void setCalyDzien(boolean blokadaCalydzien) {
		this.calyDzien = blokadaCalydzien;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

}