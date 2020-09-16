package view;

import java.io.FileWriter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import db.EmployeeDB;
import models.Employee;
import sorter.SortByAgeAsc;
import sorter.SortByAgeDesc;
import sorter.SortBySurnameAsc;
import util.HRManagerUtil;

public class View {

	private static final Scanner scanner = new Scanner(System.in);
	private static EmployeeDB db = new EmployeeDB();

	/**
	 * Show the interface to add a employee to the db
	 * @throws ParseException 
	 */
	public void showAddEmployee() throws ParseException {

		//Input all Stuff needed for Object generation
		System.out.println("Prename: ");
		String prename = scanner.next();

		System.out.println("Surname: ");
		String surname = scanner.next();

		Date birthdate = null;
		System.out.println("Birthdate (dd.MM.yyyy): ");
		try {
			birthdate = HRManagerUtil.formatter.parse(scanner.next());
		} catch (ParseException e1) {
			System.out.println("Invalid Date Format!");
			birthdate = HRManagerUtil.formatter.parse("01.01.1970");
		}

		System.out.println("Jobdescription: ");
		String jobDescription = scanner.next();

		// While input != double
		System.out.print("Salary: ");
		while (!scanner.hasNextDouble()) {
			scanner.next();
			System.out.println("Please enter a valid Double!");
		}
		double salary = scanner.nextDouble();

		Date employmentDate = null;
		System.out.println("EmploymentDate (dd.MM.yyyy): ");
		try {
			employmentDate = HRManagerUtil.formatter.parse(scanner.next());
		} catch (ParseException e2) {
			System.out.println("Invalid Date Format!");
			employmentDate= HRManagerUtil.formatter.parse("01.01.1970");
		}
		//Make a new Employee
		Employee e = new Employee(prename, surname, jobDescription, birthdate, salary, employmentDate);
		//Add it to the "db"
		db.addEmployee(e);
		System.out.println("Added!");

	}

	/**
	 * Show the interface to edit a employee to the db
	 * @throws ParseException 
	 */
	public void showEditEmployee() throws ParseException {
		showListEmployees();
		//Get Object with Surname and Prename
		System.out.println("Enter the Prename of the Person you want to delete: ");
		String prename = scanner.next();
		System.out.println("Enter the Surname of the Person you want to delete: ");
		String surname = scanner.next();

		Employee editEmployee = HRManagerUtil.getEmployeeByName(prename, surname, db.getEmployees());
		//If Employee exsist
		if (editEmployee != null) {
			showEmployee(editEmployee);
			System.out.println("\nEnter 1 for Prename");
			System.out.println("Enter 2 for Surname");
			System.out.println("Enter 3 for Birthdate");
			System.out.println("Enter 4 for Jobdescription");
			System.out.println("Enter 5 for Salary");
			System.out.println("Enter 6 for EmploymentDate");

			// While input != int
			while (!scanner.hasNextInt()) {
				scanner.next();
				System.out.println("Please enter a valid Integer!");
			}
			int menuInput = scanner.nextInt();

			//Switch case for input
			switch (menuInput) {
			case 1:
				System.out.println("new Prename: ");
				String prename1 = scanner.next();
				editEmployee.setPrename(prename1);
				break;
			case 2:
				System.out.print("new Surname: ");
				editEmployee.setSurname(scanner.next());
				break;
			case 3:
				System.out.print("new Birthdate(dd.MM.yyyy): ");
				try {
					editEmployee.setBirthdate(HRManagerUtil.formatter.parse(scanner.next()));
				} catch (ParseException e2) {
					System.out.println("Invalid Date Format!");
					editEmployee.setBirthdate(HRManagerUtil.formatter.parse("01.01.1970"));
				}
				break;
			case 4:
				System.out.print("new Jobdescription: ");
				editEmployee.setJobDescription(scanner.next());
				break;
			case 5:
				System.out.print("new Salary: ");
				editEmployee.setSalary(scanner.nextDouble());
				break;
			case 6:
				System.out.print("new EmploymentDate(dd.MM.yyyy): ");
				try {
					editEmployee.setEmploymentDate(HRManagerUtil.formatter.parse(scanner.next()));
				} catch (ParseException e2) {
					System.out.println("Invalid Date Format!");
					editEmployee.setEmploymentDate(HRManagerUtil.formatter.parse("01.01.1970"));
				}
				break;
			default:
				System.out.println("Wrong Input!");
				break;
			}

			db.updateEmployee(editEmployee);
		}
		else {
			System.out.println("Employee dosen't exist");
		}
	}

	/**
	 * Show the list of all employees from the db
	 */
	public void showListEmployees() {
		List<Employee> allEmployees = db.getEmployees();
		System.out.printf("%10s %30s %22s %40s %10s %22s", "Id", "Name", "Birthdate", "Jobdescription", "Salary",
				"EmploymentDate");
		for (Employee e : allEmployees) {

			showEmployee(e);
		}
		System.out.println("");
	}

	/**
	 * Show the interface to delete a employee
	 */
	public void showDeleteEmployee() {
		showListEmployees();
		//Get Object with Surname and Prename
		System.out.println("Enter the Prename of the Person you want to delete: ");
		String prename = scanner.next();
		System.out.println("Enter the Surname of the Person you want to delete: ");
		String surname = scanner.next();

		Employee delEmployee = HRManagerUtil.getEmployeeByName(prename, surname, db.getEmployees());

		//If Employee exists with the input name
		if (delEmployee != null) {
			//delete it
			db.deleteEmployee(delEmployee);
			System.out.println("Deleted!");
		}
		else {
			System.out.println("Employee dosen't exist");
		}
	}

	/**
	 * Internal method to print out a employee
	 * 
	 * @param employee to show
	 */
	private void showEmployee(Employee e) {
		System.out.printf("\n %10s %30s %22s %40s %10s %22s", e.getId(), e.getPrename() + " " + e.getSurname(),
				e.getBirthdate(), e.getJobDescription(), e.getSalary(), e.getEmploymentDate());
	}
	
	/**
	 * Method to print all Employees sorted by Age Desc
	 */
	public void showEmployeesAgeDesc() {
		List<Employee> allEmployees = db.getEmployees();
		allEmployees.sort(new SortByAgeDesc());
		System.out.printf("%10s %30s %22s %40s %10s %22s", "Id", "Name", "Birthdate", "Jobdescription", "Salary",
				"EmploymentDate");
		for (Employee e : allEmployees) {

			showEmployee(e);
			Period period = Period.between(e.getBirthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now());
			System.out.print(" " +period.getYears());
		}
		System.out.println("");
	}
	/**
	 * Method to print all Employees sorted by Age Asc
	 */
	public void showEmployeesAgeAsc() {
		List<Employee> allEmployees = db.getEmployees();
		allEmployees.sort(new SortByAgeAsc());
		System.out.printf("%10s %30s %22s %40s %10s %22s %10s", "Id", "Name", "Birthdate", "Jobdescription", "Salary",
				"EmploymentDate", "Age");
		for (Employee e : allEmployees) {

			showEmployee(e);
			Period period = Period.between(e.getBirthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now());
			System.out.print(" " +period.getYears());
		}
		System.out.println("");
	}
	
	/**
	 * Method to print all Employees sorted by Surname Asc
	 */
	public void showEmployeesSurnameAsc() {
		List<Employee> allEmployees = db.getEmployees();
		allEmployees.sort(new SortBySurnameAsc());
		System.out.printf("%10s %30s %22s %40s %10s %22s %10s", "Id", "Name", "Birthdate", "Jobdescription", "Salary",
				"EmploymentDate", "Age");
		for (Employee e : allEmployees) {

			showEmployee(e);
		}
		System.out.println("");
	}

}
