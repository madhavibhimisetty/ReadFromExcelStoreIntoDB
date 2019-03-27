package omniwyse.read;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.hibernate.Session;
import org.springframework.cglib.core.CollectionUtils;

public class Insert {
	public static void main(String[] args) throws InvalidFormatException, IOException {

		HashMap<Integer, Employee> extract = ExtractEmployeeData.extract();

		 EntityManager entityManager = EntityManagerFactoryUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		for (Entry<Integer, Employee> strKey : extract.entrySet()) {
			Employee employee = new Employee(strKey.getValue().getName(), strKey.getValue().getCode(),strKey.getValue().getAttendanceList());
			System.out.println(employee.getCode() + "------------------------------------- " + employee.getAttendanceList());
			Set<EmployeeAttendence> attendanceList = new HashSet<>();
			
			employee.getAttendanceList().forEach(fileAttenObj -> {
			
			String str1 = "select e from EmployeeAttendence e where employee_code=:code and date=:date";
			
			TypedQuery<EmployeeAttendence> query = entityManager.createQuery(str1, EmployeeAttendence.class);
			query.setParameter("code", employee.getCode());
			query.setParameter("date", fileAttenObj.getDate());
			EmployeeAttendence dbAttenObj=null;
			try {
			 dbAttenObj = query.getSingleResult();
			}
			catch(Exception e)
			{
				
			}
			System.out.println(dbAttenObj);
			if(dbAttenObj==null) {
				attendanceList.add(fileAttenObj);
			}
			else {
				
				Integer code = employee.getCode();
				Date date = dbAttenObj.getDate();
				
				String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
		
				DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
					Date date1 = new Date();
					try {
						date1 = dateFormat.parse(dateString);//+"-"+Calendar.getInstance().get(Calendar.YEAR));
						Calendar cal = Calendar.getInstance();
						cal.setTime(date1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					dbAttenObj.setDate(date1);
					Date dbdate = dbAttenObj.getDate();
				
				String dbinTime=dbAttenObj.getInTime();
				String dboutTime=dbAttenObj.getOutTime();
				String[] dbinTimesplit = dbinTime.split(":");
				int inTimedbfirstHour = Integer.parseInt(dbinTimesplit[0]);
				int inTimedbfirstMinute = Integer.parseInt(dbinTimesplit[1]);
				String[] dboutTimesplit = dboutTime.split(":");
				int outtimedbfirstHour = Integer.parseInt(dboutTimesplit[0]);
				int outtimedbfirstMinute = Integer.parseInt(dboutTimesplit[1]);
				
				
					Integer code2 = employee.getCode();
					Date exceldate = fileAttenObj.getDate();
					String excelinTime = fileAttenObj.getInTime();
					String exceloutTime = fileAttenObj.getOutTime();
					String[] excelinTimesplit = excelinTime.split(":");
					int inTimexcelfirstHour = Integer.parseInt(excelinTimesplit[0]);
					int inTimexcelfirstMinute = Integer.parseInt(excelinTimesplit[1]);
					String[] exceloutTimesplit = exceloutTime.split(":");
					int outtimexcelfirstHour = Integer.parseInt(exceloutTimesplit[0]);
					int outtimexcelfirstMinute = Integer.parseInt(exceloutTimesplit[1]);

					if ((dbAttenObj.getDate().equals(exceldate)) && (code.equals(code2))) {
						if (((inTimedbfirstHour == 0 && inTimedbfirstMinute == 0)
								&& (inTimexcelfirstHour == 0 && inTimexcelfirstMinute == 0))
								&& ((outtimedbfirstHour == 0 && outtimedbfirstMinute == 0)
										&& (outtimexcelfirstHour == 0 && outtimexcelfirstMinute == 0))) {
							
							employee.setCode(code);
							fileAttenObj.setInTime(dbinTime);
							fileAttenObj.setOutTime(dboutTime);
						} else if (((inTimedbfirstHour == 0 && inTimedbfirstMinute == 0)
								&& (inTimexcelfirstHour < 24 && inTimexcelfirstMinute < 60))
								&& ((outtimedbfirstHour == 0 && outtimedbfirstMinute == 0)
										&& (outtimexcelfirstHour < 24 && outtimexcelfirstMinute < 60))) {
							employee.setCode(code);
							fileAttenObj.setInTime(excelinTime);
							fileAttenObj.setOutTime(exceloutTime);
						} else if (((inTimedbfirstHour < 24 && inTimedbfirstMinute < 60)
								&& (inTimexcelfirstHour == 0 && inTimexcelfirstMinute == 0))
								&& ((outtimedbfirstHour < 24 && outtimedbfirstMinute < 60)
										&& (outtimexcelfirstHour == 0 && outtimexcelfirstMinute == 0))) {
							employee.setCode(code);
							fileAttenObj.setInTime(dbinTime);
							fileAttenObj.setOutTime(dboutTime);

						} else if (((inTimedbfirstHour < 24 && inTimedbfirstMinute < 60)
								&& (inTimexcelfirstHour < 24 && inTimexcelfirstMinute < 60))
								&& ((outtimedbfirstHour < 24 && outtimedbfirstMinute < 60)
										&& (outtimexcelfirstHour < 24 && outtimexcelfirstMinute < 60))) {

							if (inTimedbfirstHour <= inTimexcelfirstHour) {
	//								&& inTimedbfirstMinute <= inTimexcelfirstMinute) {
								employee.setCode(code);
								fileAttenObj.setInTime(dbinTime);
							} else {
								employee.setCode(code);
								fileAttenObj.setInTime(excelinTime);
							}
							if (outtimedbfirstHour > outtimexcelfirstHour) {
								employee.setCode(code);
								fileAttenObj.setOutTime(dboutTime);
							} else {
								employee.setCode(code);
								fileAttenObj.setOutTime(exceloutTime);
							}
						}
						fileAttenObj.setId(dbAttenObj.getId());
						fileAttenObj.setEmployee(dbAttenObj.getEmployee());
						
						{
							
						}
						attendanceList.add(fileAttenObj);
			}
			}
			
		
			
			});
			if (null != attendanceList) {
			employee.setAttendanceList(attendanceList);     
			}  
			
			entityManager.merge(employee);
	}
		transaction.commit();                    
		entityManager.close();		 
			
	}}


