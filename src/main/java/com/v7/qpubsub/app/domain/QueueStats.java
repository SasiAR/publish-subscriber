package com.v7.qpubsub.app.domain;

public class QueueStats {

	private String queueName;
	private Status status;
	
	
	public QueueStats(String queueName, Status status) {
		this.queueName = queueName;
		this.status = status;
	}

	public String getQueueName() {
		return queueName;
	}

	public Status getStatus() {
		return status;
	}
	
	
	
}
