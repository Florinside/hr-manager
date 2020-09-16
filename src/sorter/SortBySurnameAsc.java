package sorter;

import java.util.Comparator;

import models.Employee;

public class SortBySurnameAsc implements Comparator<Employee> {

	@Override
	public int compare(Employee o1, Employee o2) {
		return o1.getSurname().compareTo(o2.getSurname());
	}
}
