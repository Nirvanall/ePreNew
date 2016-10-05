package edu.hkpolyu.epre.response;

import java.util.Date;

public class JsonResponse {
	public int code;
	public static final int CODE_SUCC = 0, CODE_FAIL = 100,
			CODE_FAIL_LOGIN = 200, CODE_FAIL_PERMISSION = 201,
			CODE_FAIL_PASSWORD = 202,
			CODE_FAIL_LACK_PARAM = 300, CODE_FAIL_INVALID_PARAM = 301,
			CODE_FAIL_NOT_FOUND = 400, CODE_FAIL_NOT_OWN = 401;
	
	public long time;
	
	public String desc;
	public static final String DESC_SUCC = "Success", DESC_FAIL = "Failure",
			DESC_FAIL_LOGIN = "Not Logged In", DESC_FAIL_PERMISSION = "No Permission",
			DESC_FAIL_PASSWORD = "Incorrect Password",
			DESC_FAIL_LACK_PARAM = "Lack of Parameter", DESC_FAIL_INVALID_PARAM = "Invalid Parameter",
			DESC_FAIL_NOT_FOUND = "The object of the requested id does not exist",
			DESC_FAIL_NOT_OWN = "The object of the request id does not belongs to you";
	
	public Object data;
	
	public JsonResponse() {
		this(null);
	}
	
	public JsonResponse(Object data) {
		code = CODE_SUCC;
		desc = DESC_SUCC;
		time = new Date().getTime();
		this.data = data;
	}
	
	public static JsonResponse getFailInstance(String desc) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_FAIL;
		result.desc = (null == desc || desc.length() == 0) ? DESC_FAIL : desc;
		return result;
	}
	
	public static JsonResponse getFailLoginInstance(String desc) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_FAIL_LOGIN;
		result.desc = (null == desc || desc.length() == 0) ? DESC_FAIL_LOGIN : desc;
		return result;
	}
	
	public static JsonResponse getFailPermissionInstance(String desc) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_FAIL_PERMISSION;
		result.desc = (null == desc || desc.length() == 0) ? DESC_FAIL_PERMISSION : desc;
		return result;
	}
	
	public static JsonResponse getFailPasswordInstance(String desc) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_FAIL_PASSWORD;
		result.desc = (null == desc || desc.length() == 0) ? DESC_FAIL_PASSWORD : desc;
		return result;
	}
	
	public static JsonResponse getFailLackParamInstance(String desc, String[] parameters) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_FAIL_LACK_PARAM;
		result.desc = (null == desc || desc.length() == 0) ? DESC_FAIL_LACK_PARAM : desc;
		result.data = parameters;
		return result;
	}
	
	public static JsonResponse getFailInvalidParamInstance(String desc, String[] parameters) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_FAIL_INVALID_PARAM;
		result.desc = (null == desc || desc.length() == 0) ? DESC_FAIL_INVALID_PARAM : desc;
		result.data = parameters;
		return result;
	}
	
	public static JsonResponse getFailNotFoundInstance(
			String desc,
			@SuppressWarnings("rawtypes") Class model
	) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_FAIL_NOT_FOUND;
		result.desc = (null == desc || desc.length() == 0) ? DESC_FAIL_NOT_FOUND : desc;
		result.data = model.getName();
		return result;
	}
	
	public static JsonResponse getFailNotOwnInstance(
			String desc,
			@SuppressWarnings("rawtypes") Class model
	) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_FAIL_NOT_OWN;
		result.desc = (null == desc || desc.length() == 0) ? DESC_FAIL_NOT_OWN : desc;
		result.data = model.getName();
		return result;
	}
	
}
