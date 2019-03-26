package omniwyse.read;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class InsertEmployeeDetails {
	public static void main(String[] args) throws InvalidFormatException, IOException {

		HashMap<Integer, Employee> extract = ExtractEmployeeData.extract();

		EntityManager entityManager = EntityManagerFactoryUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		Employee employee=null;
		for (Entry<Integer, Employee> strKey : extract.entrySet()) {
			employee = new Employee(strKey.getValue().getName(), strKey.getValue().getCode(),strKey.getValue().getAttendanceList());
			System.out.println(employee.getCode() + "------------------------------------- " + employee.getAttendanceList());
			Set<EmployeeAttendence> attendanceList = new HashSet<>();
			EmployeeAttendence attendence = new EmployeeAttendence();
			String str1 = "select e from EmployeeAttendence e where employee_code=" + employee.getCode()+ "order by date";
			List<EmployeeAttendence> resultList = entityManager.createQuery(str1, EmployeeAttendence.class).getResultList();
			for (EmployeeAttendence empAttendance : resultList) {
				System.out.println(empAttendance.getEmployee().getCode());
				System.out.println(empAttendance.getDate());
				System.out.println(empAttendance.getInTime());
				System.out.println(empAttendance.getOutTime());

				Integer code = empAttendance.getEmployee().getCode();
				String dbdate = empAttendance.getDate();
				String dbinTime = empAttendance.getInTime();
				String dboutTime = empAttendance.getOutTime();
				String[] dbinTimesplit = dbinTime.split(":");
				int inTimedbfirstHour = Integer.parseInt(dbinTimesplit[0]);
				int inTimedbfirstMinute = Integer.parseInt(dbinTimesplit[1]);
				String[] dboutTimesplit = dboutTime.split(":");
				int outtimedbfirstHour = Integer.parseInt(dboutTimesplit[0]);
				int outtimedbfirstMinute = Integer.parseInt(dboutTimesplit[1]);

				Set<EmployeeAttendence> attendanceList2 = employee.getAttendanceList();
				for (EmployeeAttendence employeeAttendence : attendanceList2) {
					Integer code2 = employee.getCode();
					String exceldate = employeeAttendence.getDate();
					String excelinTime = employeeAttendence.getInTime();
					String exceloutTime = employeeAttendence.getOutTime();
					String[] excelinTimesplit = excelinTime.split(":");
					int inTimexcelfirstHour = Integer.parseInt(excelinTimesplit[0]);
					int inTimexcelfirstMinute = Integer.parseInt(excelinTimesplit[1]);
					String[] exceloutTimesplit = exceloutTime.split(":");
					int outtimexcelfirstHour = Integer.parseInt(exceloutTimesplit[0]);
					int outtimexcelfirstMinute = Integer.parseInt(exceloutTimesplit[1]);

					if ((dbdate.equals(exceldate)) && (code.equals(code2))) {
						if (((inTimedbfirstHour == 0 && inTimedbfirstMinute == 0)
								&& (inTimexcelfirstHour == 0 && inTimexcelfirstMinute == 0))
								&& ((outtimedbfirstHour == 0 && outtimedbfirstMinute == 0)
										&& (outtimexcelfirstHour == 0 && outtimexcelfirstMinute == 0))) {
							employee.setCode(code);
							empAttendance.setInTime(dbinTime);
							empAttendance.setOutTime(dboutTime);
						} else if (((inTimedbfirstHour == 0 && inTimedbfirstMinute == 0)
								&& (inTimexcelfirstHour < 24 && inTimexcelfirstMinute < 60))
								&& ((outtimedbfirstHour == 0 && outtimedbfirstMinute == 0)
										&& (outtimexcelfirstHour < 24 && outtimexcelfirstMinute < 60))) {
							employee.setCode(code);
							empAttendance.setInTime(excelinTime);
							empAttendance.setOutTime(exceloutTime);
						} else if (((inTimedbfirstHour < 24 && inTimedbfirstMinute < 60)
								&& (inTimexcelfirstHour == 0 && inTimexcelfirstMinute == 0))
								&& ((outtimedbfirstHour < 24 && outtimedbfirstMinute < 60)
										&& (outtimexcelfirstHour == 0 && outtimexcelfirstMinute == 0))) {
							employee.setCode(code);
							empAttendance.setInTime(dbinTime);
							empAttendance.setOutTime(dboutTime);

						} else if (((inTimedbfirstHour < 24 && inTimedbfirstMinute < 60)
								&& (inTimexcelfirstHour < 24 && inTimexcelfirstMinute < 60))
								&& ((outtimedbfirstHour < 24 && outtimedbfirstMinute < 60)
										&& (outtimexcelfirstHour < 24 && outtimexcelfirstMinute < 60))) {

							if (inTimedbfirstHour <= inTimexcelfirstHour) {
	//								&& inTimedbfirstMinute <= inTimexcelfirstMinute) {
								employee.setCode(code);
								empAttendance.setInTime(dbinTime);
							} else {
								employee.setCode(code);
								empAttendance.setInTime(excelinTime);
							}
							if (outtimedbfirstHour > outtimexcelfirstHour) {
								employee.setCode(code);
								empAttendance.setOutTime(dboutTime);
							} else {
								employee.setCode(code);
								empAttendance.setOutTime(exceloutTime);
							}
						}
						attendanceList.add(empAttendance);
					}
				}
			}
			if (null != attendanceList) {
				employee.setAttendanceList(attendanceList);

			}
		}
		entityManager.merge(employee);          
	transaction.commit();                    
	entityManager.close();              
	}
	}



