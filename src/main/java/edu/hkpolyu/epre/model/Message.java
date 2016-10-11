package edu.hkpolyu.epre.model;

import java.util.Map;
import java.util.HashMap;
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
@Table(name = "t_message")
@DynamicUpdate
public class Message extends IdModel implements Comparable<Message> {
	@Transient
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "from_user_id", nullable = false)
	private User fromUser;
	public User getFromUser() {
		return fromUser;
	}
	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user_id")
	private User toUser;
	public User getToUser() {
		return toUser;
	}
	public void setToUser(User toUser) {
		this.toUser = toUser;
	}
	
	@Column(length = 64, nullable = false)
	private String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(length = 4096, nullable = false)
	private String content;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	public int compareTo(Message message) {
		return -(this.getCreateTime().compareTo(message.getCreateTime()));
	}
	
	public HashMap<String, Object> toMap(Map<String, Object> options) {
		HashMap<String, Object> result = super.toMap(options);
		result.put("title", this.getTitle());
		result.put("content", this.getContent());
		return result;
	}
}
