package edu.hkpolyu.common.model;

import java.util.Map;
import java.util.HashMap;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import edu.hkpolyu.common.helper.TrueValueHelper;

@MappedSuperclass
public abstract class StatusModel extends TimeModel {
	
	public static final byte STATUS_DELETED = 0;
	public static final byte STATUS_NORMAL = 1;
	public static final byte STATUS_EXAMINING = 2;
	public static final byte STATUS_EDITING = 3;
	public static final byte STATUS_FROZEN = 4;
	public static final byte STATUS_UPLOADING = 5;
	
	@Column(nullable = false)
	protected byte status = STATUS_NORMAL;
	
	public boolean isStatusDeleted() {
		return STATUS_DELETED == status;
	}
	
	public boolean isStatusNormal() {
		return STATUS_NORMAL == status;
	}
	
	public boolean isStatusExamining() {
		return STATUS_EXAMINING == status;
	}
	
	public boolean isStatusEditing() {
		return STATUS_EDITING == status;
	}
	
	public boolean isStatusFrozen() {
		return STATUS_FROZEN == status;
	}
	
    public boolean isStatusUploading() {
        return STATUS_UPLOADING == status;
    }

	public void statusDeleted() {
		status = STATUS_DELETED;
	}

	public void statusNormal() {
		status = STATUS_NORMAL;
	}

	public void statusExamining() {
		status = STATUS_EXAMINING;
	}

	public void statusEditing() {
		status = STATUS_EDITING;
	}

	public void statusFrozen() {
		status = STATUS_FROZEN;
	}

	public void statusUploading() {
		status = STATUS_UPLOADING;
	}
	
	public String getStatusInfo() {
		// TODO:
		return "Undefined";
	}
	
	public HashMap<String, Object> toMap(Map<String, Object> options) {
		HashMap<String, Object> result = super.toMap(options);
		if (TrueValueHelper.isTrue(options, "status")) {
			result.put("status", this.getStatusInfo());
		}
		return result;
	}
}
