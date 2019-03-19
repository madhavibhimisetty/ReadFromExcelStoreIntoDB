package Omniwyse.Read;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import Omniwyse.Read.EmployeeAttendence;


@Entity
@Table(name = "employee", schema = "assignment")
public class Employee {

	private String name;

	@Id
	private Integer code;

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="code")
	private Set<EmployeeAttendence> attendanceList;

	public Employee(String name, Integer code, Set<EmployeeAttendence> attendanceList) {
		super();
		
		this.name = name;
		this.code = code;
		this.attendanceList = attendanceList;
	}

	public Employee() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	
	  public Set<EmployeeAttendence> getAttendanceList() { 
		  return attendanceList;
	  }
	  
	  public void setAttendanceList(Set<EmployeeAttendence> attendanceList) {
	  this.attendanceList = attendanceList; 
	  }
	 
	

}
