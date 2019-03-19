package Omniwyse.Read;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.hibernate.Session;

public class InsertEmployeeDetails {

	public static void main(String[] args) throws InvalidFormatException, IOException {
		
		ExtractEmployeeData employeeDetails = new ExtractEmployeeData();
		HashMap<Integer, Employee> extract = ExtractEmployeeData.extract();
		Session session = SessionFactoryUtil.getSession();
		org.hibernate.Transaction transaction = session.beginTransaction();

		for (Entry<Integer, Employee> strKey : extract.entrySet()) {
			Employee employee = new Employee(strKey.getValue().getName(), strKey.getValue().getCode(),strKey.getValue().getAttendanceList());
			System.out.println(employee.getCode() + "------------------------------------- " + employee.getAttendanceList());
			session.save(employee);
			
			

		}
		transaction.commit();
		session.close();
	
	
		

	}

}


