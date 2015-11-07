package fyp.models;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class IdStatusTimeModel extends StatusTimeModel {
	@Transient
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected Integer id;
	
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
}
