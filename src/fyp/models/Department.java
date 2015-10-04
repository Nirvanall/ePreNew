package fyp.models;

import java.sql.Date;

public class Department{
	
	private Byte id;
	
	public Byte getId(){
		return id;
	}
	
	public void setId(Byte id){
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
	
	
	private String abbreviation;
	
	public String getAbbreviation(){
		return abbreviation;
	}
	
	public void setAbbreviation(String abbreviation){
		this.abbreviation = abbreviation;
	}
	
	
	private String name;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
}