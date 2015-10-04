package fyp.models;

import java.sql.Date;

public class Announcement{
	
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
	private Integer userId;
	
	public Integer getUserId(){
		return userId;
	}
	public void setUserId(Integer userId){
		this.userId = userId;
	}
	*/
	private User publisher;
	
	public User getPublisher(){
		return publisher;
	}
	
	public void setPublisher(User publisher){
		this.publisher = publisher;
	}
	
	
	private String content;
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
}