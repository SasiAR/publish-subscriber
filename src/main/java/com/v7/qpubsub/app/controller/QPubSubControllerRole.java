package com.v7.qpubsub.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.annotation.Secured;

import com.v7.qpubsub.app.domain.QueueStats;
import com.v7.qpubsub.queue.domain.QPubSubConfig;

public interface QPubSubControllerRole {

	public String getEnv();
	
	@Secured({"READONLY","ADMIN"})
	public Map<String,QPubSubConfig> getQueueConfig(); 
	
	@Secured({"READONLY","ADMIN"})
	public List<QueueStats> getQueueStats();

	@Secured("ADMIN")
	public boolean stopQueueIoc(String queueConfigName);

	@Secured("ADMIN")
	public boolean startQueueIoc(String queueConfigName);
	
}
