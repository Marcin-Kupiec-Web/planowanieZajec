package com.godzinyZajec.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;


/**
 * The persistent class for the godzinyzajec database table.
 * 
 */
@Entity
@NamedQuery(name="Godzinyzajec.findAll", query="SELECT g FROM Godzinyzajec g ORDER BY g.nrGodzina")
public class Godzinyzajec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idGodzinyZajec;

	private Date godzDo;

	private Date godzOd;

	@Lob
	private String komentarz;

	private int nrGodzina;

	public Godzinyzajec() {
	}

	public int getIdGodzinyZajec() {
		return this.idGodzinyZajec;
	}

	public void setIdGodzinyZajec(int idGodzinyZajec) {
		this.idGodzinyZajec = idGodzinyZajec;
	}

	public Date getGodzDo() {
		return this.godzDo;
	}

	public void setGodzDo(Date godzDo) {
		this.godzDo = godzDo;
	}

	public Date getGodzOd() {
		return this.godzOd;
	}

	public void setGodzOd(Date godzOd) {
		this.godzOd = godzOd;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public int getNrGodzina() {
		return this.nrGodzina;
	}

	public void setNrGodzina(int nrGodzina) {
		this.nrGodzina = nrGodzina;
	}

}