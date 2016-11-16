package com.oim.core.net.http;

public class ResultData {

	private String code;
	private String message;
	private Object data;

	public static final String code_fail = "0";
	public static final String code_success = "1";
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@SuppressWarnings("unchecked")
	public <T> T getData() {
		return (T) data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
