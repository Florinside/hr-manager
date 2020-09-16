package sorter;

import java.util.Comparator;

import models.Employee;

public class SortByAgeDesc implements Comparator<Employee>{
	public static final Comparator<Employee> DESCENDING_COMPARATOR = 
		    Comparator.comparing(Employee::getBirthdate).reversed();
	 @Override
	    public int compare(Employee o1, Employee o2) {
		 return DESCENDING_COMPARATOR.compare(o1, o2);
	    }

}
