package fyp.models;

import java.sql.Date;

public class Presentation{
	
	private Integer id;
	
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	
	private Date createTime;
	
	public Date getCreateTime(){
		return createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	
	private Date updateTime;
	
	public Date getUpdateTime(){
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	
	/*
	private Byte departmentId;
	
	public Byte getDepartmentId(){
		return departmentId;
	}
	public void setDepartmentId(Byte departmentId){
		this.departmentId = departmentId;
	}
	*/
	private Department department;
	
	public Department getDepartment(){
		return department;
	}
	public void setDepartment(Department department){
		this.department = department;
	}
	
	
	private String name;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	
	private String yearSemester;
	
	public String getYearSemester(){
		return yearSemester;
	}
	
	public void setYearSemester(String yearSemester){
		this.yearSemester = yearSemester;
	}
	
	
	public Short getYear(){
		return Short.parseShort(yearSemester.substring(0, 4));
	}
	
	public Byte getSemester(){
		return Byte.parseByte(yearSemester.substring(4));
	}
	
	public String getSemesterString(){
		char c = yearSemester.charAt(4);
		if('a' == c){
			return "All Semesters";
		}
		if('b' == c){
			return "Semester 1 and 2";
		}
		return "Semester" + c;
	}
	
}