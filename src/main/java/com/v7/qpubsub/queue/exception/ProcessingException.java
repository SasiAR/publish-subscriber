package com.v7.qpubsub.queue.exception;

public class ProcessingException extends RuntimeException {

	private static final long serialVersionUID = -52867120803871485L;
	private Exception exception; 

	private String message; 
	
	public ProcessingException(Exception e) {
		this.exception = e; 
	}
	
	public ProcessingException(String msg) {
		this.message = msg; 
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Exception getException() {
		return exception;
	}

	public String getMessage() {
		return message;
	}
	
	
}
