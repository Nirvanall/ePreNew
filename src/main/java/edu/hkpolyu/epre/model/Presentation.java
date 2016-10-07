package edu.hkpolyu.epre.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import edu.hkpolyu.common.model.IdModel;

@Entity
@Table(name = "Presentations")
@DynamicUpdate
public class Presentation extends IdModel implements Comparable<Presentation> {
	@Transient
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;
	
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	
	@Column(length = 64, nullable = false, unique = true)
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "semester", length = 5, nullable = false)
	private String yearSemester;
	
	public String getYearSemester() {
		return yearSemester;
	}
	
	public void setYearSemester(String yearSemester) {
		this.yearSemester = yearSemester;
	}
	
	
	public Short getYear() {
		return Short.parseShort(yearSemester.substring(0, 4));
	}

	public String getAcademicYear() {
		StringBuilder builder = new StringBuilder(yearSemester.substring(0, 4));
		builder.append('~');
		builder.append(getYear() + 1);
		return builder.toString();
	}
	
	public Byte getSemester() {
		return Byte.parseByte(yearSemester.substring(4));
	}
	
	public String infoSemester() {
		char c = yearSemester.charAt(4);
		if ('a' == c) {
			return "All Semesters";
		}
		if ('b' == c) {
			return "Semester 1 and 2";
		}
		return "Semester" + c;
	}
	
	public String infoSemesterFull() {
		return getAcademicYear() + ' ' + infoSemester();
	}
	
	
	public int compareTo(Presentation presentation) {
		int result = department.compareTo(presentation.department);
		if (0 != result) return result;
		
		result = getYear().compareTo(presentation.getYear());
		if (0 != result) return result;
		
		result = getSemester().compareTo(presentation.getSemester());
		if (0 != result) return result;
		
		return name.compareTo(presentation.name);
	}
}
