package edu.hkpolyu.epre.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import edu.hkpolyu.common.model.IdModel;

@Entity
@Table(name = "t_user")
@DynamicUpdate
public class User extends IdModel implements Comparable<User> {
	@Transient
	private static final long serialVersionUID = 1L;
	
	@Column(name = "user_name", length = 20, nullable = false, unique = true)
	private String userName;
	
	public String getUserName(){
		return userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private UserPassword password;
	
	public UserPassword getPassword(){
		return password;
	}
	
	public void setPassword(UserPassword password){
		this.password = password;
	}
	
	@Column(length = 64, nullable = false)
	private String name;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;
	
	public Department getDepartment(){
		return department;
	}
	
	public void setDepartment(Department department){
		this.department = department;
	}
	
	@Column
	private Byte category;
	
	public Byte getCategory(){
		return category;
	}
	
	public void setCategory(Byte category){
		this.category = category;
	}
	
	public boolean isAdmin(){
		return category == 0;
	}
	
	public boolean isStudent(){
		return category == 1;
	}
	
	public boolean isTeacher(){
		return category == 2;
	}
	
	public boolean isGuest(){
		return !isAdmin() && !isStudent() && !isTeacher();
	}
	
	
	public int compareTo(User user) {
		int result = department.compareTo(user.department);
		if (0 != result) return result;

		if (null == category && null != user.category) return 1;
		if (null != category && null == user.category) return -1;
		// put undefined user at last
		
		result = category.compareTo(user.category);
		if (0 != result) return result;
		
		return userName.compareTo(user.userName);
	}
}
