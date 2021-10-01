package serwis.RejestryLogi;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.edziennik.logi.model.Logi;
import com.edziennik.logi.model.LogiDaneosobowe;

public class RejestryLogi {
	FacesContext fct=FacesContext.getCurrentInstance();
	HttpSession session=(HttpSession) fct.getExternalContext().getSession(false);
	public Logi zapiszLogi(Timestamp dataFormGlownyTimeStamp,String dzialanie,String modol,int IdObiektu,String wartosc,String nazwaObiektu) {
		Logi log=new Logi();
		log.setData(dataFormGlownyTimeStamp);
		log.setDzialanie(dzialanie);
		log.setIdObiektu(IdObiektu);
		
		if(nazwaObiektu.equalsIgnoreCase("osoba"))
			log.setIdOsoba(IdObiektu); 
		else
		log.setIdOsoba(0);
		
		log.setKto(session.getAttribute("userImieNazwisko").toString());
		log.setModol(modol);
		log.setSesja(session.getId());
		log.setIdUser(Integer.valueOf(session.getAttribute("idUser").toString()));
		try {
			log.setIp(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.setNazwaObiektu(nazwaObiektu);
		log.setOpis(dzialanie+". Obiekt: "+nazwaObiektu+". Wartość: "+wartosc);
		return log;
	}
	
	//z opisem
	public Logi zapiszLogiOpis(Timestamp dataFormGlownyTimeStamp,String dzialanie,String modol,String sesja,int idUsers,String kto,int IdObiektu,String wartosc,String nazwaObiektu,String opis,int idOsoba) {
		Logi log=new Logi();
		log.setData(dataFormGlownyTimeStamp);
		log.setDzialanie(dzialanie);
		log.setIdObiektu(IdObiektu);
		log.setIdOsoba(idOsoba);
		log.setKto(kto);
		log.setModol(modol);
		log.setSesja(sesja);
		log.setIdUser(Integer.valueOf(idUsers));
		try {
			log.setIp(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.setNazwaObiektu(nazwaObiektu);
		log.setOpis(opis);
		return log;
	}
	
	public LogiDaneosobowe zapiszLogiOsoba(Timestamp dataFormGlownyTimeStamp,String dzialanie,String opis,String users,int idUser,int IdObiektu,String nazwaObiektu) {
		LogiDaneosobowe log=new LogiDaneosobowe();
		log.setData(dataFormGlownyTimeStamp);
		log.setDzialanie(dzialanie);
		log.setIdObiektu(IdObiektu);
		log.setIdOsoba(IdObiektu);
		log.setKto(users);
		log.setIdUser(idUser);
		try {
			log.setIp(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.setNazwaObiektu(nazwaObiektu);
		log.setOpis(opis);
		return log;
	}
}
