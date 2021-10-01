package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.edziennik.sale.model.Sale;
import com.userManager.model.User;


/**
 * The persistent class for the specjalizacja database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Specjalizacja.findAll", query="SELECT s FROM Specjalizacja s ORDER BY s.nazwa"),
@NamedQuery(name="Specjalizacja.findAllDist", query="SELECT s FROM Specjalizacja s ORDER BY s.nazwa")

})
public class Specjalizacja implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_specjalizacja")
	private int idSpecjalizacja;

	private String komentarz;

	private String nazwa;

	
	//bi-directional many-to-many association to Przedmiottemat
	@ManyToMany(mappedBy="specjalizacjas")
	private List<Przedmiottemat> przedmiottemats;
	
	//bi-directional many-to-many association to Przedmiottemat
	@ManyToMany(mappedBy="specjalizacjas")
	private List<Sale> sales;
	
	//bi-directional many-to-many association to Przedmiottemat
	@ManyToMany(mappedBy="specjalizacjas")
	private List<User> userss;
	
	public List<User> getUserss() {
		return userss;
	}

	public void setUserss(List<User> userss) {
		this.userss = userss;
	}

	public List<Przedmiottemat> getPrzedmiottemats() {
		return przedmiottemats;
	}

	public void setPrzedmiottemats(List<Przedmiottemat> przedmiottemats) {
		this.przedmiottemats = przedmiottemats;
	}

	public Specjalizacja() {
	}

	public int getIdSpecjalizacja() {
		return this.idSpecjalizacja;
	}

	public void setIdSpecjalizacja(int idSpecjalizacja) {
		this.idSpecjalizacja = idSpecjalizacja;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public String getNazwa() {
		return this.nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}


	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

}