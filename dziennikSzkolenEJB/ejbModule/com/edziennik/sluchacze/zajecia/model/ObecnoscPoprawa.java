package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the obecnosc_poprawa database table.
 * 
 */
@Entity
@Table(name="obecnosc_poprawa")
@NamedQueries({
@NamedQuery(name="ObecnoscPoprawa.findAll", query="SELECT o FROM ObecnoscPoprawa o"),
@NamedQuery(name="ObecnoscPoprawa.findById", query="SELECT o FROM ObecnoscPoprawa o WHERE o.idObecnoscPoprawa LIKE :idObPopr"),
})
public class ObecnoscPoprawa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_obecnosc_poprawa")
	private int idObecnoscPoprawa;

	@Column(name="data_ob_poprawa")
	private Timestamp dataObPoprawa;

	@Column(name="id_obecnosc")
	private int idObecnosc;

	@Column(name="kto_wstawil")
	private int ktoWstawil;

	private String wartosc;
	
	private String usunieta;
	
	
	private Timestamp dataUsunieta;
	
	public Timestamp getDataUsunieta() {
		return dataUsunieta;
	}

	public void setDataUsunieta(Timestamp dataUsunieta) {
		this.dataUsunieta = dataUsunieta;
	}

	public int getIdUserUsuna() {
		return idUserUsuna;
	}

	public void setIdUserUsuna(int idUserUsuna) {
		this.idUserUsuna = idUserUsuna;
	}

	private int idUserUsuna;
	
	public ObecnoscPoprawa() {
	}

	public int getIdObecnoscPoprawa() {
		return this.idObecnoscPoprawa;
	}

	public void setIdObecnoscPoprawa(int idObecnoscPoprawa) {
		this.idObecnoscPoprawa = idObecnoscPoprawa;
	}

	public Timestamp getDataObPoprawa() {
		return this.dataObPoprawa;
	}

	public void setDataObPoprawa(Timestamp dataObPoprawa) {
		this.dataObPoprawa = dataObPoprawa;
	}

	public int getIdObecnosc() {
		return this.idObecnosc;
	}

	public void setIdObecnosc(int i) {
		this.idObecnosc = i;
	}

	public int getKtoWstawil() {
		return this.ktoWstawil;
	}

	public void setKtoWstawil(int ktoWstawil) {
		this.ktoWstawil = ktoWstawil;
	}

	public String getWartosc() {
		return this.wartosc;
	}

	public void setWartosc(String wartosc) {
		this.wartosc = wartosc;
	}

	public String getUsunieta() {
		return usunieta;
	}

	public void setUsunieta(String usunieta) {
		this.usunieta = usunieta;
	}

}