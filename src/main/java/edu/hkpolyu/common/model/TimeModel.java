package edu.hkpolyu.common.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Map;
import java.util.HashMap;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import edu.hkpolyu.common.helper.TrueValueHelper;

@MappedSuperclass
public abstract class TimeModel implements Serializable {
	@Transient
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(TimeModel.class);
	
	@Column(name = "create_time", insertable = false, updatable = false)
	@Generated(value = GenerationTime.INSERT)
	protected Timestamp createTime;
	
	public Timestamp getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getCreateTimeInfo(DateFormat formater) {
		if (null == formater) formater = DateFormat.getDateInstance(DateFormat.MEDIUM);
		return formater.format(createTime);
	}
	
	@Column(name = "update_time", insertable = false, updatable = false)
	@Generated(value = GenerationTime.ALWAYS)
	protected Timestamp updateTime;
	
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getUpdateTimeInfo(DateFormat formater) {
		if (null == formater) formater = DateFormat.getDateInstance(DateFormat.MEDIUM);
		return formater.format(updateTime);
	}
	
	public HashMap<String, Object> toMap(Map<String, Object> options) {
		log.debug(this.getCreateTime().toString());
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		if (TrueValueHelper.isTrue(options, "create_time")) {
			result.put("create_time", this.getCreateTime().getTime());
		}
		if (TrueValueHelper.isTrue(options, "update_time")) {
			result.put("update_time", this.getUpdateTime().getTime());
		}
		
		Object obj = options.get("create_time_info");
		if (obj instanceof DateFormat) {
			result.put("create_time_info", this.getCreateTimeInfo(
					(DateFormat)obj));
		}
		obj = options.get("update_time_info");
		if (obj instanceof DateFormat) {
			result.put("update_time_info", this.getUpdateTimeInfo(
					(DateFormat)obj));
		}
		return result;
	}
}
