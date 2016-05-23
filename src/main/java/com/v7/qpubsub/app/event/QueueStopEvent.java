package com.v7.qpubsub.app.event;

import org.springframework.context.ApplicationEvent;

public class QueueStopEvent extends ApplicationEvent{

	private static final long serialVersionUID = -557535505536244961L;
	
	private final String queueConfigName;
	
	public QueueStopEvent(String queueConfigName){
		super(queueConfigName);
		this.queueConfigName = queueConfigName; 
	}
	
	public String getQueueConfigName() {
		return this.queueConfigName;
	}
	
	 
}
