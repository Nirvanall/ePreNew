package edu.hkpolyu.epre.model;

import java.io.File;
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
import edu.hkpolyu.common.model.IdModel;

@Entity
@Table(name = "t_video")
@DynamicUpdate
public class Video extends IdModel implements Comparable<Video> {
	@Transient
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "presentation_id", nullable = false)
	private Presentation presentation;
	
	public Presentation getPresentation(){
		return presentation;
	}
	
	public void setPresentation(Presentation presentation){
		this.presentation = presentation;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User owner;
	
	public User getOwner(){
		return owner;
	}
	
	public void setOwner(User owner){
		this.owner = owner;
	}
	
	@Column(length = 64, nullable = false)
	private String name;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	@Column(length = 256, nullable = false)
	private String info;
	
	public String getInfo(){
		return info;
	}
	
	public void setInfo(String info){
		this.info = info;
	}
	
	@OneToMany(mappedBy = "video", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("playtime")
	private List<Comment> comments;
	
	public List<Comment> getComments() {
		return comments;
	}
	
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	@OneToMany(mappedBy = "video", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Assessment> assessments;
	
	public List<Assessment> getAssessments() {
		return assessments;
	}
	
	public void setAssessments(List<Assessment> assessments) {
		this.assessments = assessments;
	}
	
	
	public int compareTo(Video video) {
		int result = presentation.compareTo(video.presentation);
		if (0 != result) return result;
		
		result = owner.compareTo(video.owner);
		if (0 != result) return result;
		
		return -(this.getCreateTime().compareTo(video.getCreateTime()));
	}


	public String getPath() {
		Presentation p = this.getPresentation();
		return "videos" + File.separator +
				p.getDepartment().getAbbreviation() + File.separator +
				p.getYearSemester() + File.separator +
				"Pre" + p.getId() + File.separator +
				this.getOwner().getUserName() + "_" + this.getId();
	}
	
	
	public String averageGrade() {
		// Assessment
		return "none";
	}
}
