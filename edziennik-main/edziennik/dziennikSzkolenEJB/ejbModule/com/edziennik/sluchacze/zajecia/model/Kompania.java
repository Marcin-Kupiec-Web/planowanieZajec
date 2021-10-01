package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the kompania database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Kompania.findAll", query="SELECT k FROM Kompania k WHERE k.archiwum LIKE :archiwum ORDER BY LENGTH(k.nazwaKompania),k.nazwaKompania"),
@NamedQuery(name="Kompania.findAllall", query="SELECT k FROM Kompania k ORDER BY LENGTH(k.nazwaKompania),k.nazwaKompania"),
})
public class Kompania implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_kompania")
	private int idKompania;

	private String archiwum;

	@Column(name="data_utowrzenia_kompania")
	private Timestamp dataUtowrzeniaKompania;

	@Column(name="nazwa_kompania")
	private String nazwaKompania;

	//bi-directional many-to-one association to Pluton
	@OneToMany(cascade= {CascadeType.REMOVE},fetch=FetchType.LAZY,mappedBy="kompania")
	private List<Pluton> plutons;

	public Kompania() {
	}

	public int getIdKompania() {
		return this.idKompania;
	}

	public void setIdKompania(int idKompania) {
		this.idKompania = idKompania;
	}

	public String getArchiwum() {
		return this.archiwum;
	}

	public void setArchiwum(String archiwum) {
		this.archiwum = archiwum;
	}

	public Timestamp getDataUtowrzeniaKompania() {
		return this.dataUtowrzeniaKompania;
	}

	public void setDataUtowrzeniaKompania(Timestamp dataUtowrzeniaKompania) {
		this.dataUtowrzeniaKompania = dataUtowrzeniaKompania;
	}

	public String getNazwaKompania() {
		return this.nazwaKompania;
	}

	public void setNazwaKompania(String nazwaKompania) {
		this.nazwaKompania = nazwaKompania;
	}

	public List<Pluton> getPlutons() {
		return this.plutons;
	}

	public void setPlutons(List<Pluton> plutons) {
		this.plutons = plutons;
	}

	public Pluton addPluton(Pluton pluton) {
		getPlutons().add(pluton);
		pluton.setKompania(this);

		return pluton;
	}

	public Pluton removePluton(Pluton pluton) {
		getPlutons().remove(pluton);
		pluton.setKompania(null);

		return pluton;
	}

}