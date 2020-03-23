package com.idontchop.dateLocation.dto;

public class SuccessMessage {
	
	SuccessMessage () {}
	
	String message;
	
	public static SuccessMessage message (String message) {
		SuccessMessage sm = new SuccessMessage();
		sm.setMessage(message);
		return sm;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
