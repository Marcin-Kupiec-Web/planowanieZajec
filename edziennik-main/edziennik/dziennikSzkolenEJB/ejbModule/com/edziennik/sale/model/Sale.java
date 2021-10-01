package com.edziennik.sale.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.edziennik.sluchacze.zajecia.model.Przedmiottemat;
import com.edziennik.sluchacze.zajecia.model.Specjalizacja;
import com.edziennik.sluchacze.zajecia.model.Zaklad;


/**
 * The persistent class for the sale database table.
 * 
 */
@Entity
@Table(name = "sale", uniqueConstraints={@UniqueConstraint(columnNames = "nazwa")})
@NamedQuery(name="Sale.findAll", query="SELECT s FROM Sale s ORDER BY s.nazwa")
public class Sale  implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_sale")
	private int idSale;

	private String nazwa;
	
	//bi-directional many-to-many association to Zaklad
	@ManyToMany
	@JoinTable(
		name="sale_zaklad"
		, joinColumns={
			@JoinColumn(name="id_sale")
			} 
		, inverseJoinColumns={
			@JoinColumn(name="id_zaklad")
			}
		)
	private List<Zaklad> zaklads;

	//bi-directional many-to-many association to Zaklad
	@ManyToMany
	@JoinTable(
		name="sale_specjalizacja"
		, joinColumns={
			@JoinColumn(name="id_sale")
			} 
		, inverseJoinColumns={
			@JoinColumn(name="id_specjalizacja")
			}
		)
	private List<Specjalizacja> specjalizacjas;
	
	//bi-directional many-to-many association to Przedmiottemat
	@ManyToMany(mappedBy="sales")
	private List<Przedmiottemat> przedmiottemats;
	
	@Lob
	private String komentarz;

	public Sale() {
	}

	public int getIdSale() {
		return this.idSale;
	}

	public void setIdSale(int idSale) {
		this.idSale = idSale;
	}

	public List<Zaklad> getZaklads() {
		return this.zaklads;
	}

	public void setZaklads(List<Zaklad> zaklads) {
		this.zaklads = zaklads;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public List<Specjalizacja> getSpecjalizacjas() {
		return specjalizacjas;
	}

	public void setSpecjalizacjas(List<Specjalizacja> specjalizacjas) {
		this.specjalizacjas = specjalizacjas;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public List<Przedmiottemat> getPrzedmiottemats() {
		return przedmiottemats;
	}

	public void setPrzedmiottemats(List<Przedmiottemat> przedmiottemats) {
		this.przedmiottemats = przedmiottemats;
	}

}