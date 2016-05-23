package com.v7.qpubsub.queue.router;

import org.springframework.jms.core.JmsTemplate;
import org.w3c.dom.Document;

import com.v7.qpubsub.queue.domain.SubscribeType;
import com.v7.qpubsub.queue.domain.SubscriberConfig;
import com.v7.qpubsub.queue.subscribe.Subscriber;

public class MockSubscriber implements Subscriber<Document> {

	private boolean subscribe;
	private boolean sendCalled;
	private String queueName; 
	
	public MockSubscriber(boolean subscribe, String queueName) {
		this.subscribe = subscribe; 
		this.queueName = queueName;
	}
	
	@Override
	public void send(Document message) {
		sendCalled = true;  
	}

	@Override
	public boolean subscribe(Document message) {
		return subscribe;
	}
	
	public boolean sendCalled() {
		return sendCalled; 
	}
	
	public void setSendCalled(boolean flag) {
		this.sendCalled = flag;
	}

	@Override
	public String getQueueName() {
		return queueName;
	}

	@Override
	public SubscribeType getType() {
		return SubscribeType.ALL;
	}

	@Override
	public Subscriber<Document> newInstance(JmsTemplate jmsTemplate, SubscriberConfig subscriberConfig) {
		return new MockSubscriber(true, subscriberConfig.getQueueName());
	}

	@Override
	public int getPriority(Document message) {
		return 5;
	}
}