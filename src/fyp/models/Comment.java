package fyp.models;

import java.sql.Date;

public class Comment{
	
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
	private User commenter;
	
	public User getCommenter(){
		return commenter;
	}
	
	public void setCommenter(User commenter){
		this.commenter = commenter;
	}
	
	
	/*
	private Integer videoId;
	
	public Integer getVideoId(){
		return videoId;
	}
	
	public void setVideoId(Integer videoId){
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
	
	
	private Float playtime;
	
	public Float getPlaytime(){
		return playtime;
	}
	
	public void setPlaytime(Float playtime){
		this.playtime = playtime;
	}
	
	
	private String content;
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
}