package fyp.models;

import java.sql.Date;

public class Assessment{
	
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
	private int userId;
	
	public int getUserId(){
		return userId;
	}
	
	public void setUserId(int userId){
		this.userId = userId;
	}
	*/
	private User viewer;
	
	public User getViewer(){
		return viewer;
	}
	
	public void setViewer(User viewer){
		this.viewer = viewer;
	}
	
	
	/*
	private int videoId;
	
	public int getVideoId(){
		return videoId;
	}
	
	public void setVideoId(int videoId){
		this.videoId = videoId;
	}
	*/
	private Video video;
	
	public Video getVideo(){
		return video;
	}
	
	public void setVideo(Video video){
		this.video = video;
	}
	
	
	private Byte permission;
	
	public Byte getPermission(){
		return permission;
	}
	
	public void setPermission(Byte permission){
		this.permission = permission;
	}
	
	public boolean canComment(){
		return permission > 0;
	}
	
	public boolean canGrade(){
		return permission > 1;
	}
	
	
	private Byte grade;
	
	public Byte getGrade(){
		return grade;
	}
	
	public void setGrade(Byte grade){
		this.grade = grade;
	}
}