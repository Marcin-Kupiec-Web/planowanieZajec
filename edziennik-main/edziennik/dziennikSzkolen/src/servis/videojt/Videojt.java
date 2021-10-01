package servis.videojt;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.DatatypeConverter;

import com.edziennik.crudAll.CrudAllLocal;

@Named
@RequestScoped
public class Videojt implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Videojt() {
		// TODO Auto-generated constructor stub
	}
	
	@EJB
	private CrudAllLocal crudPlutony;
	@Inject
	private serwis.wyborPlutonu.wyborPlutonu wyborPlutonu; 
	private String myHashVideo="https://jitsi.intsps.lokalna.pl/";
	private Integer pluton;
		
	@PostConstruct
    public void init() {
		pluton=wyborPlutonu.getIdPlutonWybrany();
		if(pluton!=null) {
		LocalDate td=LocalDate.now();
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String token=String.valueOf(pluton)+"/"+td.toString();
		md.update(token.getBytes());
		byte[] digest=md.digest();
		myHashVideo="https://jitsi.intsps.lokalna.pl/"+DatatypeConverter.printHexBinary(digest);
		}
	}

	public serwis.wyborPlutonu.wyborPlutonu getWyborPlutonu() {
		return wyborPlutonu;
	}

	public void setWyborPlutonu(serwis.wyborPlutonu.wyborPlutonu wyborPlutonu) {
		this.wyborPlutonu = wyborPlutonu;
	}

	public Integer getPluton() {
		return pluton;
	}

	public void setPluton(Integer pluton) {
		this.pluton = pluton;
	}

	public String getMyHashVideo() {
		return myHashVideo;
	}

	public void setMyHashVideo(String myHashVideo) {
		this.myHashVideo = myHashVideo;
	}
	
}
