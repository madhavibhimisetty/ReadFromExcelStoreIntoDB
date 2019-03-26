package omniwyse.read;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class InsertEmp {
	public static void main(String[] args) throws InvalidFormatException, IOException {

		HashMap<Integer, Employee> extract = ExtractEmployeeData.extract();

		EntityManager entityManager = EntityManagerFactoryUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		Employee employee=null;
		for (Entry<Integer, Employee> strKey : extract.entrySet()) {
			employee = new Employee(strKey.getValue().getName(), strKey.getValue().getCode(),
					strKey.getValue().getAttendanceList());
			System.out.println(employee.getCode() + "------------------------------------- " + employee.getAttendanceList());
			entityManager.persist(employee);
		}
		transaction.commit();                    
		entityManager.close();
	}
	
}