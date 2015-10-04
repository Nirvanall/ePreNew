package fyp.models;

import java.sql.Date;

public class Video{
	
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
	private Integer presentationId;
	
	public Integer getPresentationId(){
		return presentationId;
	}
	
	public void setPresentationId(Integer presentationId){
		this.presentationId = presentationId;
	}
	*/
	private Presentation presentation;
	
	public Presentation getPresentation(){
		return presentation;
	}
	
	public void setPresentation(Presentation presentation){
		this.presentation = presentation;
	}
	
	
	/*
	private Integer userId;
	
	public Integer getUserId(){
		return userId;
	}
	
	public void setUserId(Integer userId){
		this.userId = userId;
	}
	*/
	private User owner;
	
	public User getOwner(){
		return owner;
	}
	
	public void setOwner(User owner){
		this.owner = owner;
	}
	
	
	private String name;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	
	private String info;
	
	public String getInfo(){
		return info;
	}
	
	public void setInfo(String info){
		this.info = info;
	}
	
	
	
}