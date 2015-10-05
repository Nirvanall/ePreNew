package fyp.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Table(name = "Departments")
public class Department{
	@Id
	@GeneratedValue
	private Byte id;
	
	public Byte getId(){
		return id;
	}
	
	public void setId(Byte id){
		this.id = id;
	}
	
	@Column(name = "create_time", insertable = false, updatable = false)
	@Generated(value = GenerationTime.INSERT)
	private Date createTime;
	
	public Date getCreateTime(){
		return createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	@Column(name = "update_time", insertable = false, updatable = false)
	@Generated(value = GenerationTime.ALWAYS)
	private Date updateTime;
	
	public Date getUpdateTime(){
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	
	@Column(length = 8, nullable = false, unique = true)
	private String abbreviation;
	
	public String getAbbreviation(){
		return abbreviation;
	}
	
	public void setAbbreviation(String abbreviation){
		this.abbreviation = abbreviation;
	}
	
	@Column(length = 64, nullable = false, unique = true)
	private String name;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
}