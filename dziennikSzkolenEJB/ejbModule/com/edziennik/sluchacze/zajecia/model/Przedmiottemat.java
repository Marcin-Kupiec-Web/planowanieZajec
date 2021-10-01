package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;

import com.edziennik.sale.model.Sale;
import com.plany.model.Planyzaklady;

import java.util.List;


/**
 * The persistent class for the przedmiottemat database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Przedmiottemat.findAll", query="SELECT p FROM Przedmiottemat p"),
@NamedQuery(name="Przedmiottemat.wybranyPluton", query="SELECT p FROM Przedmiottemat p WHERE p.przedmiot.strukturaKursu.idStrukturaKursu LIKE :idStrukturaKursu ORDER BY p.przedmiot.jm,p.przedmiot.js,p.nazwa")
})
public class Przedmiottemat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id_przedmiotTemat;

	//bi-directional many-to-one association to Przedmiot
	@ManyToOne
	@JoinColumn(name="id_przedmiot")
	private Przedmiot przedmiot;

	@Lob
	private String komentarz;
	private Integer ileGodzin;
	private Integer ileWykladowcow;
	private String nazwa;
	private String zakladyToString;
	private String specjalToString;
	private Integer korelacja;
	//bi-directional many-to-one association to Tematy
	@OneToMany(mappedBy="przedmiottemat",cascade=CascadeType.REMOVE)
	private List<Planyzaklady> planyZakladys;

	//bi-directional many-to-many association to Zaklad
	@ManyToMany
	@JoinTable(
		name="zaklad_przedmiottemat"
		, joinColumns={
			@JoinColumn(name="id_przedmiotTemat")
			}
		, inverseJoinColumns={
			@JoinColumn(name="id_zaklad")
			}
		)
	private List<Zaklad> zaklads;
	//bi-directional many-to-many association to Zaklad
	@ManyToMany
	@JoinTable(
		name="specjalizacja_przedmiottemat"
		, joinColumns={
			@JoinColumn(name="id_przedmiotTemat")
			}
		, inverseJoinColumns={
			@JoinColumn(name="id_specjalizacja")
			}
		)
	private List<Specjalizacja> specjalizacjas;
	
	//bi-directional many-to-many association to Zaklad
	@ManyToMany
	@JoinTable(
		name="sale_przedmiottemat"
		, joinColumns={
			@JoinColumn(name="id_przedmiottemat")
			} 
		, inverseJoinColumns={
			@JoinColumn(name="id_sale")
			}
		)
	private List<Sale> sales;
	
	
	public List<Specjalizacja> getSpecjalizacjas() {
		return specjalizacjas;
	}

	public void setSpecjalizacjas(List<Specjalizacja> specjalizacjas) {
		this.specjalizacjas = specjalizacjas;
	}

	public Przedmiottemat() {
	}

	public int getId_przedmiotTemat() {
		return this.id_przedmiotTemat;
	}

	public void setId_przedmiotTemat(int id_przedmiotTemat) {
		this.id_przedmiotTemat = id_przedmiotTemat;
	}

	public Przedmiot getPrzedmiot() {
		return przedmiot;
	}

	public void setPrzedmiot(Przedmiot przedmiot) {
		this.przedmiot = przedmiot;
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


	public List<Zaklad> getZaklads() {
		return this.zaklads;
	}

	public void setZaklads(List<Zaklad> zaklads) {
		this.zaklads = zaklads;
	}

	public Integer getIleGodzin() {
		return ileGodzin;
	}

	public void setIleGodzin(Integer ileGodzin) {
		this.ileGodzin = ileGodzin;
	}

	public Integer getIleWykladowcow() {
		return ileWykladowcow;
	}

	public void setIleWykladowcow(Integer ileWykladowcow) {
		this.ileWykladowcow = ileWykladowcow;
	}

	public String getZakladyToString() {
		zakladyToString="";
		for(Zaklad zk:this.getZaklads()) {
			zakladyToString+=zk.getNazwaSkrot()+", ";
		}
		zakladyToString=zakladyToString.trim();
		zakladyToString=zakladyToString.substring(0,zakladyToString.length()-1);
		return zakladyToString;
	}

	public void setZakladyToString(String zakladyToString) {
		this.zakladyToString = zakladyToString;
	}

	public String getSpecjalToString() {
		specjalToString="";
		for(Specjalizacja spec:this.getSpecjalizacjas()) {
			specjalToString+=spec.getNazwa()+", ";
		}
		specjalToString=specjalToString.trim();
		specjalToString=specjalToString.substring(0,specjalToString.length()-1);
		return specjalToString;
	}

	public void setSpecjalToString(String specjalToString) {
		this.specjalToString = specjalToString;
	}
	
	public List<Planyzaklady> getPlanyZakladys() {
		return planyZakladys;
	}

	public void setPlanyZakladys(List<Planyzaklady> planyZakladys) {
		this.planyZakladys = planyZakladys;
	}
	public Planyzaklady addPlanyzaklady(Planyzaklady planyzaklady) {
		getPlanyZakladys().add(planyzaklady);
		planyzaklady.setPrzedmiottemat(this);;
		return planyzaklady;
	}

	public Planyzaklady removePlanyzaklady(Planyzaklady planyzaklady) {
		getPlanyZakladys().remove(planyzaklady);
		planyzaklady.setPrzedmiottemat(null);;
		return planyzaklady;
	}

	public Integer getKorelacja() {
		return korelacja;
	}

	public void setKorelacja(Integer korelacja) {
		this.korelacja = korelacja;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

}