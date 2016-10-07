package edu.hkpolyu.epre.model;

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
@Table(name = "Assessments")
@DynamicUpdate
public class Assessment extends IdModel {
	@Transient
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User viewer;
	
	public User getViewer() {
		return viewer;
	}
	
	public void setViewer(User viewer) {
		this.viewer = viewer;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "video_id", nullable = false)
	private Video video;
	
	public Video getVideo() {
		return video;
	}
	
	public void setVideo(Video video) {
		this.video = video;
	}
	
	@Column
	private Byte permission;
	
	public Byte getPermission(){
		return permission;
	}
	
	public void setPermission(Byte permission) {
		this.permission = permission;
	}
	
	public boolean canComment() {
		return permission > 0;
	}
	
	public boolean canGrade() {
		return permission > 1;
	}
	
	@Column
	private Byte grade;
	
	public Byte getGrade() {
		return grade;
	}
	
	public void setGrade(Byte grade) {
		this.grade = grade;
	}
	
	public String gradeValue() {
		return null != grade ? grade.toString() : "";
	}
	
	@Column(name = "grade_weight")
	private Byte gradeWeight;
	
	public Byte getGradeWeight() {
		return gradeWeight;
	}
	
	public void setGradeWeight(Byte gradeWeight) {
		this.gradeWeight = gradeWeight;
	}
	
}
