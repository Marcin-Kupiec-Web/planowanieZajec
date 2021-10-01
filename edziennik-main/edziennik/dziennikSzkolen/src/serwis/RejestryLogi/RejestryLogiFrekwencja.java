package serwis.RejestryLogi;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;

import com.edziennik.logi.model.Logi;
import com.edziennik.sluchacze.zajecia.model.Obecnosc;

public class RejestryLogiFrekwencja {

	public Logi zapiszLogi(Timestamp dataFormGlownyTimeStamp,String dzialanie,Integer idUsers,Obecnosc ob,String nazwaObiektu) {
		Logi log=new Logi();
		log.setData(dataFormGlownyTimeStamp);
		log.setDzialanie(dzialanie);
		log.setIdObiektu(ob.getIdObecnosc());
		log.setIdOsoba(ob.getSluchacze().getIdSluchacz());
		log.setKto(String.valueOf(idUsers));
		try {
			log.setIp(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.setNazwaObiektu(nazwaObiektu);
		log.setOpis(dzialanie+". Obiekt: "+nazwaObiektu+". Wartość: "+ob.getWartosc()+". Osoba: "+ob.getSluchacze().getNazwiskoSluchacz()+" "+ob.getSluchacze().getImieSluchacz()+". Pluton: id:"+ob.getSluchacze().getPluton().getIdPluton()+" komp-plut-szkol: "+ob.getSluchacze().getPluton().getKompania().getNazwaKompania()+"-"+ob.getSluchacze().getPluton().getNazwaPluton()+"-"+ob.getSluchacze().getPluton().getOznaczenieSzkolenia());
		return log;
	}

}
