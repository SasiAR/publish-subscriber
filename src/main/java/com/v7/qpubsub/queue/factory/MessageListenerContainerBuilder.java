package com.v7.qpubsub.queue.factory;

import javax.jms.ConnectionFactory;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class MessageListenerContainerBuilder  {

	private static Logger LOG = LoggerFactory.getLogger(MessageListenerContainerBuilder.class);
	
	private ConnectionFactory connectionFactory;
	private String queueName; 
	private MessageListener messageListener;
	private PlatformTransactionManager transactionManager;
	private Integer consumerCount;
	
	public MessageListenerContainerBuilder connectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
		return this; 
	}

	public MessageListenerContainerBuilder queueName(String queueName) {
		this.queueName = queueName; 
		return this; 
	}
	
	public MessageListenerContainerBuilder messageListener(MessageListener messageListener) {
		this.messageListener = messageListener; 
		return this; 
	}
	
	public MessageListenerContainerBuilder transactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager; 
		return this; 
	}

	public MessageListenerContainerBuilder consumerCount(Integer count) {
		this.consumerCount = count; 
		return this; 
	}
	
	public MessageListenerContainer build() {
		DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();
		listenerContainer.setConnectionFactory(connectionFactory);
		listenerContainer.setDestinationName(queueName);
		listenerContainer.setMessageListener(messageListener);
		listenerContainer.setSessionTransacted(true);
		listenerContainer.setTransactionManager(transactionManager);
		listenerContainer.setAcceptMessagesWhileStopping(false);
		if(consumerCount != null && consumerCount > 0) {
			listenerContainer.setConcurrentConsumers(consumerCount);
		}
		LOG.info("creating messageListener container");
		return listenerContainer;
	};
	
}
