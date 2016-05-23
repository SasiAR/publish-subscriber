package com.v7.qpubsub.queue.exception;


public class InitializationException extends RuntimeException{

	private static final long serialVersionUID = -4447906463812183568L;

	private String message;
	private Exception exception; 
	
	public InitializationException(String message) {
		this.message = message; 
	}
	
	public InitializationException(Exception e) {
		this.exception = e; 
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMessage() {
		return message;
	}

	public Exception getException() {
		return exception;
	}
	
	
}
