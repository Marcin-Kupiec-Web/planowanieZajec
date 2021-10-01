package com.edziennik.sluchacze.zajecia.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the tematy database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Tematy.findAll", query="SELECT t FROM Tematy t"),
@NamedQuery(name="Tematy.findDoPensum", query="SELECT t FROM Tematy t  WHERE t.ktoZrealizowal LIKE :ktoZrealizowal AND t.tematUsuniety LIKE 'NIE' AND DATE_FORMAT(t.zrealizowaneCzas, '%Y') LIKE :zrealizowaneCzas"),
@NamedQuery(name="Tematy.findDate", query="SELECT t FROM Tematy t  WHERE t.pluton.idPluton LIKE :idPluton AND t.zrealizowaneCzas LIKE :zrealizowaneCzas AND t.tematUsuniety LIKE 'NIE'"),
@NamedQuery(name="Tematy.findTemNotCross", query="SELECT t FROM Tematy t  WHERE t.pluton.idPluton LIKE :idPluton AND t.tematUsuniety LIKE 'NIE'"),
@NamedQuery(name="Tematy.findTemDoFrekw", query="SELECT t FROM Tematy t  WHERE t.pluton.idPluton LIKE :idPluton AND t.tematyGdzl LIKE :godzLekc AND t.zrealizowaneCzas LIKE :dataRealiz"),
@NamedQuery(name="Tematy.findTemNotOrCross", query="SELECT t FROM Tematy t  WHERE t.pluton.idPluton LIKE :idPluton AND t.tematUsuniety LIKE :usuniety"),
@NamedQuery(name="Tematy.findTemSearch", query="SELECT t FROM Tematy t  WHERE t.pluton.idPluton LIKE :idPluton AND t.JM_tematy LIKE :jmTematy AND t.JS_tematy LIKE :jsTematy"
		+ " AND t.KPN_tematy LIKE :kpnTematy AND t.ktoZrealizowal LIKE :ktoZrealizowal AND t.ktoWpisal LIKE :ktoWpisal  AND t.tematUsuniety LIKE :tematUsuniety"),
})
public class Tematy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_tematy")
	private int idTematy;

	@Column(name="data_wpisu")
	private Timestamp dataWpisu;

	@Column(name="id_wpisal")
	private int idWpisal;

	private String JM_tematy;

	private String JS_tematy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="kiedy_aktualizacja")
	private Date kiedyAktualizacja;

	@Column(name="kiedy_usuniety")
	private Timestamp kiedyUsuniety;

	@Column(name="komentarz_tematy")
	private String komentarzTematy;

	private String KPN_tematy;

	@Column(name="kto_usuna")
	private int ktoUsuna;

	@Column(name="kto_zrealizowal")
	private String ktoZrealizowal;

	private String ktoWpisal;
	
	private String ktoUsunalStr;
	
	private String nieobecni;

	private String obecni;

	private String powodUsuniecia;

	private boolean hasNext=false;
	
	@Column(name="temat_usuniety")
	private String tematUsuniety;

	@Column(name="tematy_gdzl")
	private int tematyGdzl;

	@Temporal(TemporalType.DATE)
	@Column(name="zrealizowane_czas")
	private Date zrealizowaneCzas;

	//bi-directional many-to-one association to Pluton
	@ManyToOne
	@JoinColumn(name="id_pluton")
	private Pluton pluton;

	public Tematy() {
	}

	public int getIdTematy() {
		return this.idTematy;
	}

	public void setIdTematy(int idTematy) {
		this.idTematy = idTematy;
	}

	public Timestamp getDataWpisu() {
		return this.dataWpisu;
	}

	public void setDataWpisu(Timestamp dataWpisu) {
		this.dataWpisu = dataWpisu;
	}

	public int getIdWpisal() {
		return this.idWpisal;
	}

	public void setIdWpisal(int idWpisal) {
		this.idWpisal = idWpisal;
	}

	public String getJM_tematy() {
		return this.JM_tematy;
	}

	public void setJM_tematy(String JM_tematy) {
		this.JM_tematy = JM_tematy;
	}

	public String getJS_tematy() {
		return this.JS_tematy;
	}

	public void setJS_tematy(String JS_tematy) {
		this.JS_tematy = JS_tematy;
	}

	public Date getKiedyAktualizacja() {
		return this.kiedyAktualizacja;
	}

	public void setKiedyAktualizacja(Date kiedyAktualizacja) {
		this.kiedyAktualizacja = kiedyAktualizacja;
	}

	public Timestamp getKiedyUsuniety() {
		return this.kiedyUsuniety;
	}

	public void setKiedyUsuniety(Timestamp kiedyUsuniety) {
		this.kiedyUsuniety = kiedyUsuniety;
	}

	public String getKomentarzTematy() {
		return this.komentarzTematy;
	}

	public void setKomentarzTematy(String komentarzTematy) {
		this.komentarzTematy = komentarzTematy;
	}

	public String getKPN_tematy() {
		return this.KPN_tematy;
	}

	public void setKPN_tematy(String KPN_tematy) {
		this.KPN_tematy = KPN_tematy;
	}

	public int getKtoUsuna() {
		return this.ktoUsuna;
	}

	public void setKtoUsuna(int ktoUsuna) {
		this.ktoUsuna = ktoUsuna;
	}

	public String getKtoZrealizowal() {
		return this.ktoZrealizowal;
	}

	public void setKtoZrealizowal(String ktoZrealizowal) {
		this.ktoZrealizowal = ktoZrealizowal;
	}

	public String getNieobecni() {
		return this.nieobecni;
	}

	public void setNieobecni(String nieobecni) {
		this.nieobecni = nieobecni;
	}

	public String getObecni() {
		return this.obecni;
	}

	public void setObecni(String obecni) {
		this.obecni = obecni;
	}

	public String getPowodUsuniecia() {
		return this.powodUsuniecia;
	}

	public void setPowodUsuniecia(String powodUsuniecia) {
		this.powodUsuniecia = powodUsuniecia;
	}

	public String getTematUsuniety() {
		return this.tematUsuniety;
	}

	public void setTematUsuniety(String tematUsuniety) {
		this.tematUsuniety = tematUsuniety;
	}

	public int getTematyGdzl() {
		return this.tematyGdzl;
	}

	public void setTematyGdzl(int tematyGdzl) {
		this.tematyGdzl = tematyGdzl;
	}

	public Date getZrealizowaneCzas() {
		return this.zrealizowaneCzas;
	}

	public void setZrealizowaneCzas(Date zrealizowaneCzas) {
		this.zrealizowaneCzas = zrealizowaneCzas;
	}

	public Pluton getPluton() {
		return this.pluton;
	}

	public void setPluton(Pluton pluton) {
		this.pluton = pluton;
	}

	public String getKtoWpisal() {
		return ktoWpisal;
	}

	public void setKtoWpisal(String ktoWpisal) {
		this.ktoWpisal = ktoWpisal;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public String getKtoUsunalStr() {
		return ktoUsunalStr;
	}

	public void setKtoUsunalStr(String ktoUsunalStr) {
		this.ktoUsunalStr = ktoUsunalStr;
	}

}