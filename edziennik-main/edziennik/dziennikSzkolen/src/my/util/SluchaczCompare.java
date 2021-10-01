package my.util;

import java.util.Comparator;

import com.edziennik.sluchacze.zajecia.model.Sluchacze;

public class SluchaczCompare implements Comparator<Sluchacze>{

	@Override
	public int compare(Sluchacze s1, Sluchacze s2) {
		// TODO Auto-generated method stub
		return Integer.valueOf(s1.getNrDziennik()).compareTo(Integer.valueOf(s2.getNrDziennik()));
	}

}
