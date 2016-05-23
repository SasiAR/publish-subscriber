package com.v7.qpubsub.app.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.v7.qpubsub.app.container.QPubSubContainer;
import com.v7.qpubsub.app.event.QueueStopEvent;

@Component
public class QueueStopListener implements ApplicationListener<QueueStopEvent> {

	private Logger LOG = LoggerFactory.getLogger(QueueStopListener.class);
	
	@Autowired
	private QPubSubContainer rootContainer; 
	
	@Override
	public void onApplicationEvent(QueueStopEvent event) {
		LOG.error("received stop event " + event);
		rootContainer.stopIocContainer(event.getQueueConfigName());
	}

}
