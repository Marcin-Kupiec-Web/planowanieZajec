package my.util;

import java.util.Comparator;

import com.edziennik.sluchacze.zajecia.model.Ocena;


public class OcenyCompareDataWpisu implements Comparator<Ocena>{

	@Override
	public int compare(Ocena s1, Ocena s2) {
	return s2.getDataWpisu().compareTo(s1.getDataWpisu());
	}

}
