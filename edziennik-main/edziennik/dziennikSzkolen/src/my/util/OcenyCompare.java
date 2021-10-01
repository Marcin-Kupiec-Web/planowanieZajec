package my.util;

import java.util.Comparator;

import com.edziennik.sluchacze.zajecia.model.Ocena;


public class OcenyCompare implements Comparator<Ocena>{

	@Override
	public int compare(Ocena s1, Ocena s2) {
		// TODO Auto-generated method stub
		return s2.getDataOcena().compareTo(s1.getDataOcena());
	}

}
