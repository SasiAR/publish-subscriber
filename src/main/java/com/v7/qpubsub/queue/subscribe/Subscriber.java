package com.v7.qpubsub.queue.subscribe;

import org.springframework.jms.core.JmsTemplate;

import com.v7.qpubsub.queue.domain.SubscribeType;
import com.v7.qpubsub.queue.domain.SubscriberConfig;


public interface Subscriber<T> {

	public void send(T message);
	
	public boolean subscribe(T message);
	
	public String getQueueName();

	public SubscribeType getType(); 
	
	default public boolean isApplicable(SubscribeType type) {
		return getType() == type; 
	}

	public int getPriority(T message);
	
	public Subscriber<T> newInstance(JmsTemplate jmsTemplate, SubscriberConfig subscriberConfig );
	
}
