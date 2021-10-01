package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the obecnosc database table.
 * 
 */
@Entity(name="Obecnosc")
@NamedQueries({
@NamedQuery(name="Obecnosc.findAll", query="SELECT o FROM Obecnosc o"),
@NamedQuery(name="Obecnosc.findWgPlutonuDoObecnosci", query="SELECT DISTINCT o FROM Obecnosc o LEFT JOIN o.sluchacze s WHERE o.data LIKE :data AND s.pluton.idPluton LIKE :idPluton"),
@NamedQuery(name="Obecnosc.findTemDoFrekw", query="SELECT o FROM Obecnosc o LEFT JOIN o.sluchacze s  WHERE s.pluton.idPluton LIKE :idPluton AND o.godzinaLekcyjna LIKE :godzLekc AND o.data LIKE :data"),
@NamedQuery(name="Obecnosc.findFrewDoTemat", query="SELECT o FROM Obecnosc o LEFT JOIN o.sluchacze s  WHERE s.pluton.idPluton LIKE :idPluton AND o.data LIKE :data AND o.usunieta LIKE 'NIE'"),
@NamedQuery(name="Obecnosc.findObNotOrCross", query="SELECT o FROM Obecnosc o LEFT JOIN o.sluchacze s WHERE  s.pluton.idPluton LIKE :idPluton  AND o.usunieta LIKE :usunieta"),
@NamedQuery(name="Obecnosc.findNbSp", query="SELECT o FROM Obecnosc o LEFT JOIN o.sluchacze s WHERE  s.pluton.idPluton LIKE :idPluton  AND o.usunieta LIKE 'NIE'"),
})
public class Obecnosc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_obecnosc")
	private int idObecnosc;

	@Temporal(TemporalType.DATE)
	private Date data;

	@Column(name="data_poprawy")
	private Timestamp dataPoprawy;

	@Column(name="data_wpisu")
	private Timestamp dataWpisu;

	@Column(name="godzina_lekcyjna")
	private int godzinaLekcyjna;

	@Column(name="id_user_poprawa")
	private int idUserPoprawa;

	@Column(name="id_users")
	private int idUsers;

	private int idPluton;
	
	private String komentarz;

	private String wartosc;
	
	private String imieNazwiskoUser;
	
	private String imieNazwiskoUserUsunal;
	
	@Column(name="wartosc_poprawa")
	private int wartoscPoprawa;

	//bi-directional many-to-one association to Sluchacze
	@ManyToOne
	@JoinColumn(name="id_s")
	private Sluchacze sluchacze;
	
	private String classObNb;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_usunieta")
	private Date dataUsunieta;
	
	@Column(name="id_user_usunieta")
	private int idUserUsunieta;
	
	private String usunieta;
	
	public Obecnosc() {
	}

	public int getIdObecnosc() {
		return this.idObecnosc;
	}

	public void setIdObecnosc(int idObecnosc) {
		this.idObecnosc = idObecnosc;
	}

	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Timestamp getDataPoprawy() {
		return this.dataPoprawy;
	}

	public void setDataPoprawy(Timestamp dataPoprawy) {
		this.dataPoprawy = dataPoprawy;
	}

	public Timestamp getDataWpisu() {
		return this.dataWpisu;
	}

	public void setDataWpisu(Timestamp dataWpisu) {
		this.dataWpisu = dataWpisu;
	}

	public int getGodzinaLekcyjna() {
		return this.godzinaLekcyjna;
	}

	public void setGodzinaLekcyjna(int godzinaLekcyjna) {
		this.godzinaLekcyjna = godzinaLekcyjna;
	}

	public int getIdUserPoprawa() {
		return this.idUserPoprawa;
	}

	public void setIdUserPoprawa(int idUserPoprawa) {
		this.idUserPoprawa = idUserPoprawa;
	}

	public int getIdUsers() {
		return this.idUsers;
	}

	public void setIdUsers(int idUsers) {
		this.idUsers = idUsers;
	}

	public String getKomentarz() {
		return this.komentarz;
	}

	public void setKomentarz(String komentarz) {
		this.komentarz = komentarz;
	}

	public String getWartosc() {
		return this.wartosc;
	}

	public void setWartosc(String wartosc) {
		this.wartosc = wartosc;
	}

	public int getWartoscPoprawa() {
		return this.wartoscPoprawa;
	}

	public void setWartoscPoprawa(int wartoscPoprawa) {
		this.wartoscPoprawa = wartoscPoprawa;
	}

	public Sluchacze getSluchacze() {
		return this.sluchacze;
	}

	public void setSluchacze(Sluchacze sluchacze) {
		this.sluchacze = sluchacze;
	}
	public Date getDataUsunieta() {
		return dataUsunieta;
	}

	public void setDataUsunieta(Date dataUsunieta) {
		this.dataUsunieta = dataUsunieta;
	}

	public int getIdUserUsunieta() {
		return idUserUsunieta;
	}

	public void setIdUserUsunieta(int idUserUsunieta) {
		this.idUserUsunieta = idUserUsunieta;
	}
	public String getClassObNb() {
	
		
				if(this.wartosc!=null) {
						if(this.wartosc.equals("Nb") || this.wartosc.equals("N") || this.wartosc.equals("nb"))
							classObNb="classNb";
						else if(this.wartosc.equals("Sp") || this.wartosc.equals("sp"))
							classObNb="classSp";
						else
							classObNb="classOb";
						return classObNb;
				}
			
		classObNb="classOb";
		return classObNb;
	}

	public void setClassObNb(String classObNb) {
		if(classObNb.equals("Nb") || classObNb.equals("N") || classObNb.equals("nb"))
			classObNb="classNb";
		else if(classObNb.equals("Sp") || classObNb.equals("sp"))
			classObNb="classSp";
		else
			classObNb="classOb";
	}

	public String getUsunieta() {
		return usunieta;
	}

	public void setUsunieta(String usunieta) {
		this.usunieta = usunieta;
	}

	public int getIdPluton() {
		return idPluton;
	}

	public void setIdPluton(int idPluton) {
		this.idPluton = idPluton;
	}

	public String getImieNazwiskoUser() {
		return imieNazwiskoUser;
	}

	public void setImieNazwiskoUser(String imieNazwiskoUser) {
		this.imieNazwiskoUser = imieNazwiskoUser;
	}

	public String getImieNazwiskoUserUsunal() {
		return imieNazwiskoUserUsunal;
	}

	public void setImieNazwiskoUserUsunal(String imieNazwiskoUserUsunal) {
		this.imieNazwiskoUserUsunal = imieNazwiskoUserUsunal;
	}

}