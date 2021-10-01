package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;

import com.edziennik.sale.model.Sale;
import com.userManager.model.User;

import java.util.List;


/**
 * The persistent class for the zaklad database table.
 * 
 */
@Entity
@NamedQuery(name="Zaklad.findAll", query="SELECT z FROM Zaklad z ORDER BY z.nazwaPelna")
public class Zaklad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_zaklad")
	private int idZaklad;

	@Lob
	private String komentarz;

	@Lob
	private String nazwaPelna;

	private String nazwaSkrot;
	
	private String usuniety;
	
	//bi-directional many-to-many association to Przedmiottemat
	@ManyToMany(mappedBy="zaklads")
	private List<Przedmiottemat> przedmiottemats;

	//bi-directional many-to-many association to Przedmiottemat
	@ManyToMany(mappedBy="zaklads")
	private List<User> userss;
	
	//bi-directional many-to-many association to Przedmiottemat
	@ManyToMany(mappedBy="zaklads")
	private List<Sale> sales;
	
	public List<User> getUserss() {
		return userss;
	}

	public void setUserss(List<User> userss) {
		this.userss = userss;
	}

	public Zaklad() {
	}

	public int getIdZaklad() {
		return this.idZaklad;
	}

	public void setIdZaklad(int idZaklad) {
		this.idZaklad = idZaklad;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public String getNazwaPelna() {
		return this.nazwaPelna;
	}

	public void setNazwaPelna(String nazwaPelna) {
		this.nazwaPelna = nazwaPelna;
	}

	public String getNazwaSkrot() {
		return this.nazwaSkrot;
	}

	public void setNazwaSkrot(String nazwaSkrot) {
		this.nazwaSkrot = nazwaSkrot;
	}

	public List<Przedmiottemat> getPrzedmiottemats() {
		return this.przedmiottemats;
	}

	public void setPrzedmiottemats(List<Przedmiottemat> przedmiottemats) {
		this.przedmiottemats = przedmiottemats;
	}

	public String getUsuniety() {
		return usuniety;
	}

	public void setUsuniety(String usuniety) {
		this.usuniety = usuniety;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

}