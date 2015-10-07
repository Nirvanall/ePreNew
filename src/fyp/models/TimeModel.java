package fyp.models;

import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@MappedSuperclass
public abstract class TimeModel implements Serializable {
	@Transient
	private static final long serialVersionUID = 1L;
	
	@Column(name = "create_time", insertable = false, updatable = false)
	@Generated(value = GenerationTime.INSERT)
	protected Date createTime;
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getCreateTimeInfo(DateFormat formater) {
		if (null == formater) formater = DateFormat.getDateInstance(DateFormat.MEDIUM);
		return formater.format(createTime);
	}
	
	@Column(name = "update_time", insertable = false, updatable = false)
	@Generated(value = GenerationTime.ALWAYS)
	protected Date updateTime;
	
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getUpdateTimeInfo(DateFormat formater) {
		if (null == formater) formater = DateFormat.getDateInstance(DateFormat.MEDIUM);
		return formater.format(updateTime);
	}
}
