package fyp.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "Comments")
@DynamicUpdate
public class Comment extends IdStatusTimeModel implements Comparable<Comment> {
	@Transient
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User commenter;
	
	public User getCommenter(){
		return commenter;
	}
	
	public void setCommenter(User commenter){
		this.commenter = commenter;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "video_id", nullable = false)
	private Video video;
	
	public Video getVideo(){
		return video;
	}
	
	public void setVideo(Video video){
		this.video = video;
	}
	
	@Column(nullable = false)
	private Float playtime;
	
	public Float getPlaytime(){
		return playtime;
	}
	
	public void setPlaytime(Float playtime){
		this.playtime = playtime;
	}
	
	public String playtimeInfo() {
		int time = playtime.intValue(), min = time / 60, sec = time % 60;
		StringBuilder builder = new StringBuilder();
		if (min < 10) builder.append('0');
		builder.append(min);
		builder.append(':');
		if (sec < 10) builder.append('0');
		builder.append(sec);
		return builder.toString();
	}
	
	@Column(length = 4096, nullable = false)
	private String content;
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("createTime")
	private List<Response> responses;
	
	public List<Response> getResponses() {
		return responses;
	}
	
	public void setResponses(List<Response> responses) {
		this.responses = responses;
	}
	
	
	public int compareTo(Comment comment) {
		int result = video.compareTo(comment.video);
		if (0 != result) return result;
		
		result = playtime.compareTo(comment.playtime);
		if (0 != result) return result;
		
		return -(createTime.compareTo(comment.createTime));
	}
}