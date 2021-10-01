package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the oceny_nglowek database table.
 * 
 */
@Entity
@Table(name="oceny_naglowek")
@NamedQueries({
@NamedQuery(name="OcenyNaglowek.findAll", query="SELECT o FROM OcenyNaglowek o"),
@NamedQuery(name="OcenyNaglowek.findPlutonPrzedmiot", query="SELECT o FROM OcenyNaglowek o WHERE o.przedmiot.idPrzedmiot LIKE :idPrzedmiot AND o.pluton.idPluton LIKE :idPluton AND o.usunieta=:usunieta ORDER BY o.kiedyWpisal"),
@NamedQuery(name="OcenyNaglowek.findPoPluton", query="SELECT o FROM OcenyNaglowek o WHERE  o.pluton.idPluton LIKE :idPluton AND o.usunieta=:usunieta ORDER BY o.kiedyWpisal")
})
public class OcenyNaglowek implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_oceny_naglowek")
	private int idOcenyNaglowek;

	@Column(name="kiedy_wpisal")
	private Timestamp kiedyWpisal;

	@Lob
	private String komentarz;

	@Column(name="kto_wpisal")
	private String ktoWpisal;
	private int idWpisal;
	private String naglowek;
	private String kolor;
	private String zadanie;
	private boolean doSredniej;
	private boolean usunieta;
	private boolean okresowa;
	private Timestamp kiedyUsunal;
//bi-directional many-to-one association to OcenyNglowek
	@ManyToOne
	@JoinColumn(name="id_przedmiot")
	private Przedmiot przedmiot;	
		
	private String imieNazwiskoUserUsunal;
	private Integer imieNazwiskoUserUsunalId;
		
//bi-directional many-to-one association to OcenyNglowek
	@ManyToOne
	@JoinColumn(name="id_pluton")
	private Pluton pluton;	
			
//bi-directional many-to-one association to Ocena
	@OneToMany(mappedBy="ocenyNglowek", fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	private List<Ocena> ocenas;

	public OcenyNaglowek() {
	}

	public int getIdOcenyNaglowek() {
		return this.idOcenyNaglowek;
	}

	public void setIdOcenyNaglowek(int idOcenyNaglowek) {
		this.idOcenyNaglowek = idOcenyNaglowek;
	}

	public Timestamp getKiedyWpisal() {
		return this.kiedyWpisal;
	}

	public void setKiedyWpisal(Timestamp kiedyWpisal) {
		this.kiedyWpisal = kiedyWpisal;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public String getKtoWpisal() {
		return this.ktoWpisal;
	}

	public void setKtoWpisal(String ktoWpisal) {
		this.ktoWpisal = ktoWpisal;
	}

	public String getNaglowek() {
		return this.naglowek;
	}

	public void setNaglowek(String naglowek) {
		this.naglowek = naglowek;
	}

	public List<Ocena> getOcenas() {
		return this.ocenas;
	}

	public void setOcenas(List<Ocena> ocenas) {
		this.ocenas = ocenas;
	}

	public Ocena addOcena(Ocena ocena) {
		getOcenas().add(ocena);
		ocena.setOcenyNglowek(this);

		return ocena;
	}

	public Ocena removeOcena(Ocena ocena) {
		getOcenas().remove(ocena);
		ocena.setOcenyNglowek(null);
		return ocena;
	}

	public Przedmiot getPrzedmiot() {
		return przedmiot;
	}

	public void setPrzedmiot(Przedmiot przedmiot) {
		this.przedmiot = przedmiot;
	}

	public Pluton getPluton() {
		return pluton;
	}
 
	public void setPluton(Pluton pluton) {
		this.pluton = pluton;
	}

	public String getKolor() {
		return kolor;
	}

	public void setKolor(String kolor) {
		this.kolor = kolor;
	}

	public boolean isDoSredniej() {
		return doSredniej;
	}

	public void setDoSredniej(boolean doSredniej) {
		this.doSredniej = doSredniej;
	}

	public int getIdWpisal() {
		return idWpisal;
	}

	public void setIdWpisal(int idWpisal) {
		this.idWpisal = idWpisal;
	}

	public boolean isUsunieta() {
		return usunieta;
	}

	public void setUsunieta(boolean usunieta) {
		this.usunieta = usunieta;
	}

	public String getZadanie() {
		return zadanie;
	}

	public void setZadanie(String zadanie) {
		this.zadanie = zadanie;
	}

	public boolean isOkresowa() {
		return okresowa;
	}

	public void setOkresowa(boolean okresowa) {
		this.okresowa = okresowa;
	}

	public String getImieNazwiskoUserUsunal() {
		return imieNazwiskoUserUsunal;
	}

	public void setImieNazwiskoUserUsunal(String imieNazwiskoUserUsunal) {
		this.imieNazwiskoUserUsunal = imieNazwiskoUserUsunal;
	}

	public Integer getImieNazwiskoUserUsunalId() {
		return imieNazwiskoUserUsunalId;
	}

	public void setImieNazwiskoUserUsunalId(Integer imieNazwiskoUserUsunalId) {
		this.imieNazwiskoUserUsunalId = imieNazwiskoUserUsunalId;
	}

	public Timestamp getKiedyUsunal() {
		return kiedyUsunal;
	}

	public void setKiedyUsunal(Timestamp kiedyUsunal) {
		this.kiedyUsunal = kiedyUsunal;
	}

}