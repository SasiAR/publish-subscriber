package com.v7.qpubsub.queue.subscribe;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.v7.qpubsub.queue.domain.SubscribeType;
import com.v7.qpubsub.queue.domain.SubscriberConfig;

@Component
public class SubcribeToAllSubscriber extends AbstractDocumentSubscriber {
	
	public SubcribeToAllSubscriber(){};
	
	public SubcribeToAllSubscriber(JmsTemplate jmsTemplate, SubscriberConfig subscriberConfig) {
		super(jmsTemplate,subscriberConfig);
	}
	
	public boolean subscribe(Document message) {
		return true;
	}

	@Override
	public SubscribeType getType() {
		return SubscribeType.ALL;
	}

	@Override
	public Subscriber<Document> newInstance(JmsTemplate jmsTemplate,SubscriberConfig subscriberConfig) {
		return new SubcribeToAllSubscriber(jmsTemplate, subscriberConfig);
	}

}
