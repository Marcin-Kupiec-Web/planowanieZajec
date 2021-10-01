package serwis.RejestryLogi;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;

import com.edziennik.logi.model.Logi;
import com.edziennik.logi.model.LogiDaneosobowe;
public class RejestryLogi {
	
//universalne
	public Logi zapiszLogi(Timestamp dataFormGlownyTimeStamp,String dzialanie,int idUsers,String kto,int IdObiektu,String wartosc,String nazwaObiektu) {
		Logi log=new Logi();
		log.setData(dataFormGlownyTimeStamp);
		log.setDzialanie(dzialanie);
		log.setIdObiektu(IdObiektu);
		log.setIdOsoba(0);
		log.setKto(kto);
		log.setIdUser(idUsers);
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
	
//logi dane osobowe
		public LogiDaneosobowe zapiszLogiDaneOsob(Timestamp dataFormGlownyTimeStamp,String dzialanie,int idUsers,String kto,int IdObiektu,String wartosc,String nazwaObiektu,String opis,int idOsoba) {
			LogiDaneosobowe log=new LogiDaneosobowe();
			log.setData(dataFormGlownyTimeStamp);
			log.setDzialanie(dzialanie);
			log.setIdObiektu(IdObiektu);
			log.setIdOsoba(idOsoba);
			log.setKto(kto);
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

}
