package Omniwyse.Read;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Omniwyse.Read.EmployeeAttendence;

public class ExtractEmployeeData {
	public static HashMap<Integer, Employee> extract() throws IOException, InvalidFormatException {
		XSSFWorkbook workSheet = null;
		HashMap<Integer, Employee> employeeMap = null;
		try {
			FileInputStream finXLSX = new FileInputStream(new File("src/main/resources/att.xlsx"));
			workSheet = new XSSFWorkbook(finXLSX);
			XSSFSheet sheet = workSheet.getSheetAt(0);
			Row row;
			Row dayRow = null;
			employeeMap = new HashMap<>();
			for (int i = 1; i <= sheet.getLastRowNum() - 2; i++) {
				row = (Row) sheet.getRow(i);
				String id1;
				id1 = row.getCell(1).toString();
				if (id1.trim().equals("Days")) {
					dayRow = row;
					}
				ArrayList<EmployeeAttendence> attendencelist = null;
				Employee employee = null;
				if (id1.trim().equals("Employee Code:-")) {
					employee = new Employee(); 
					employee.setCode(Integer.parseInt(row.getCell(10).toString()));
					employee.setName(row.getCell(24).toString());
					
					EmployeeAttendence attendence = null;
					attendencelist=new ArrayList<EmployeeAttendence>();
					
					for (int j = i + 1; j < sheet.getLastRowNum(); j++) {
						Row inTimeRow = sheet.getRow(j);
						String cells = inTimeRow.getCell(1).toString();
						if (cells.trim().equals("In Time")) {
							Row outTimeRow = sheet.getRow(j+1);
							Row durationRow=sheet.getRow(j+5);
							System.out.println("Days::\t");
							System.out.print("In Time::\t");
							for (int k1 = 2; k1 <= inTimeRow.getLastCellNum() - 1; k1++) {
								attendence = new EmployeeAttendence();
								String inTimeStr = inTimeRow.getCell(k1).toString();
								String outTimeStr = outTimeRow.getCell(k1).toString();
								String durationStr = durationRow.getCell(k1).toString();
								if(null != inTimeStr && !inTimeStr.trim().equals("")) {
									attendence.setInTime(inTimeStr);
									attendence.setOutTime(outTimeStr);
									attendence.setDuration(durationStr);
									String stringdate = dayRow.getCell(k1).toString();
									SimpleDateFormat dateFormat= new SimpleDateFormat("dd-MMM" ,java.util.Locale.ENGLISH);
									Date date = dateFormat.parse(stringdate);
									attendence.setDate(date);
									attendencelist.add(attendence);
								}
							}
							System.out.println();
						} else if (cells.trim().equals("Status")) {
							i = j;
							break;
						}
					}	
					
					if(null != attendencelist) {
						employee.setAttendanceList(new HashSet<EmployeeAttendence>(attendencelist));
					}
				
					employeeMap.put(employee.getCode(), employee);
				}
				
				
			}
			System.out.println(employeeMap);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(null != workSheet)
				workSheet.close();
		}
		return employeeMap;
	}
}
	

