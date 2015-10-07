package fyp.models;

import java.util.Comparator;

public class PresentationComparatorBySemester implements Comparator<Presentation> {
	public int compare(Presentation a, Presentation b) {
		int result = a.getYear().compareTo(b.getYear());
		if (result != 0) return result;
		
		result = a.getSemester().compareTo(b.getSemester());
		if (result != 0) return result;
		
		result = a.getDepartment().compareTo(b.getDepartment());
		if (result != 0) return result;
		
		return a.getName().compareTo(b.getName());
	}
}
