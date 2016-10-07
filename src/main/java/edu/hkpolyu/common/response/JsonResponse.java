package edu.hkpolyu.common.response;

import java.util.Date;
import java.io.Serializable;

public class JsonResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;

	private long timestamp;
    public long getTimestamp() {
        return timestamp;
    }
	
	protected int code;
    public int getCode() {
        return code;
    }

	protected String message;
    public String getMessage() {
        return message;
    }
	
	public static final int CODE_SUCCESS = 0;
	public static final String MESSAGE_SUCCESS = "Success";
	
	public static final int CODE_NEED_LOGIN = 10;
	public static final String MESSAGE_NEED_LOGIN = "Need Login";
	
	public static final int CODE_WRONG_PASSWORD = 100;
	public static final String MESSAGE_WRONG_PASSWORD = "Wrong Password";
	
	public static final int CODE_WRONG_TOKEN = 110;
	public static final String MESSAGE_WRONG_TOKEN = "Wrong Access Token";
	
	public static final int CODE_WRONG_CODE = 120;
	public static final String MESSAGE_WRONG_CODE = "Wrong Verification Code";
	
	public static final int CODE_OCCUPIED = 10000;
	public static final String MESSAGE_OCCUPIED = "Occupied";

	public static final int CODE_USER_ID_OCCUPIED = 10100;
	public static final String MESSAGE_USER_ID_OCCUPIED = "The UserID has been Occupied";

	public static final int CODE_NO_PERMISSION = -10;
	public static final String MESSAGE_NO_PERMISSION = "No Permission";

    public static final int CODE_NOT_FOUND = -10000;
    public static final String MESSAGE_NOT_FOUND = "Not Found";

    public static final int CODE_USER_NOT_FOUND = -10010;
    public static final String MESSAGE_USER_NOT_FOUND = "User Not Found";

    public static final int CODE_DEPARTMENT_NOT_FOUND = -10011;
    public static final String MESSAGE_DEPARTMENT_NOT_FOUND = "Department Not Found";

    public static final int CODE_PRESENTATION_NOT_FOUND = -10012;
    public static final String MESSAGE_PRESENTATION_NOT_FOUND = "Presentation Not Found";

    public static final int CODE_VIDEO_NOT_FOUND = -10013;
    public static final String MESSAGE_VIDEO_NOT_FOUND = "Video Not Found";

    public static final int CODE_COMMENT_NOT_FOUND = -10014;
    public static final String MESSAGE_COMMENT_NOT_FOUND = "Comment Not Found";

    public static final int CODE_MESSAGE_NOT_FOUND = -10015;
    public static final String MESSAGE_MESSAGE_NOT_FOUND = "Message Not Found";

	public static final int CODE_INVALID_INPUT = -20000;
	public static final String MESSAGE_INVALID_INPUT = "Invalid Input";

	public static final int CODE_INVALID_USER_NAME = -20001;
	public static final String MESSAGE_INVALID_USER_NAME = "Invalid User Name";

	public static final int CODE_INVALID_DATETIME = -20010;
	public static final String MESSAGE_INVALID_DATETIME = "Invalid Date Or Time";

	public static final int CODE_INVALID_DATE = -20011;
	public static final String MESSAGE_INVALID_DATE = "Invalid Date";

	public static final int CODE_INVALID_TIME = -20012;
	public static final String MESSAGE_INVALID_TIME = "Invalid Time";

	public static final int CODE_INPUT_TOO_SHORT = -20100;
	public static final String MESSAGE_INPUT_TOO_SHORT = "An Input Field Is Too Short";

	public static final int CODE_INPUT_TOO_LONG = -20200;
	public static final String MESSAGE_INPUT_TOO_LONG = "An Input Field Is Too Long";

	public static final int CODE_INPUT_TOO_BIG = -20300;
	public static final String MESSAGE_INPUT_TOO_BIG = "An Input Field Is Too Big";

	public static final int CODE_INPUT_TOO_SMALL = -20400;
	public static final String MESSAGE_INPUT_TOO_SMALL = "An Input Field Is Too Small";

	
	protected Serializable data;
    public Serializable getData() {
        return data;
    }
	
	public JsonResponse() {
		this(null);
	}
	
	public JsonResponse(Serializable data) {
		this.timestamp = new Date().getTime();
		this.code = CODE_SUCCESS;
		this.message = MESSAGE_SUCCESS;
		this.data = data;
	}
	
	public static JsonResponse getNeedLoginInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_NEED_LOGIN;
		result.message = (null == message || message.length() == 0) ? MESSAGE_NEED_LOGIN : message;
		return result;
	}
	
	public static JsonResponse getWrongPasswordInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_WRONG_PASSWORD;
		result.message = (null == message || message.length() == 0) ? MESSAGE_WRONG_PASSWORD : message;
		return result;
	}
	
	public static JsonResponse getUserIdOccupiedInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_USER_ID_OCCUPIED;
		result.message = (null == message || message.length() == 0) ? MESSAGE_USER_ID_OCCUPIED : message;
		return result;
	}
	
	public static JsonResponse getNoPermissionInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_NO_PERMISSION;
		result.message = (null == message || message.length() == 0) ? MESSAGE_NO_PERMISSION : message;
		return result;
	}
	
	public static JsonResponse getInvalidInputInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_INVALID_INPUT;
		result.message = (null == message || message.length() == 0) ? MESSAGE_INVALID_INPUT : message;
		return result;
	}
	
	public static JsonResponse getInputTooShortInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_INPUT_TOO_SHORT;
		result.message = (null == message || message.length() == 0) ? MESSAGE_INPUT_TOO_SHORT : message;
		return result;
	}
	
	public static JsonResponse getInputTooLongInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_INPUT_TOO_LONG;
		result.message = (null == message || message.length() == 0) ? MESSAGE_INPUT_TOO_LONG : message;
		return result;
	}
	
	public static JsonResponse getInputTooBigInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_INPUT_TOO_BIG;
		result.message = (null == message || message.length() == 0) ? MESSAGE_INPUT_TOO_BIG : message;
		return result;
	}
	
	public static JsonResponse getInputTooSmallInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_INPUT_TOO_SMALL;
		result.message = (null == message || message.length() == 0) ? MESSAGE_INPUT_TOO_SMALL : message;
		return result;
	}
	
	public static JsonResponse getUserNotFoundInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_USER_NOT_FOUND;
		result.message = (null == message || message.length() == 0) ? MESSAGE_USER_NOT_FOUND : message;
		return result;
	}
	
	public static JsonResponse getDepartmentNotFoundInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_DEPARTMENT_NOT_FOUND;
		result.message = (null == message || message.length() == 0) ? MESSAGE_DEPARTMENT_NOT_FOUND : message;
		return result;
	}
	
	public static JsonResponse getPresentationNotFoundInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_PRESENTATION_NOT_FOUND;
		result.message = (null == message || message.length() == 0) ? MESSAGE_PRESENTATION_NOT_FOUND : message;
		return result;
	}
	
	public static JsonResponse getVideoNotFoundInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_VIDEO_NOT_FOUND;
		result.message = (null == message || message.length() == 0) ? MESSAGE_VIDEO_NOT_FOUND : message;
		return result;
	}
	
	public static JsonResponse getCommentNotFoundInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_COMMENT_NOT_FOUND;
		result.message = (null == message || message.length() == 0) ? MESSAGE_COMMENT_NOT_FOUND : message;
		return result;
	}
	
	public static JsonResponse getMessageNotFoundInstance(String message) {
		JsonResponse result = new JsonResponse();
		result.code = CODE_MESSAGE_NOT_FOUND;
		result.message = (null == message || message.length() == 0) ? MESSAGE_MESSAGE_NOT_FOUND : message;
		return result;
	}
	
}
