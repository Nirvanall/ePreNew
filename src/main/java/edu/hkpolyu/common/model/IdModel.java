package edu.hkpolyu.common.model;

import java.util.Map;
import java.util.HashMap;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import edu.hkpolyu.common.helper.TrueValueHelper;

@MappedSuperclass
public abstract class IdModel extends StatusModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;
	
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public HashMap<String, Object> toMap(Map<String, Object> options) {
		HashMap<String, Object> result = super.toMap(options);
		if (TrueValueHelper.isTrue(options, "id")) {
			result.put("id", this.getId());
		}
		return result;
	}
}
