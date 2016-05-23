package com.v7.qpubsub.queue.factory;

import java.util.ArrayList;
import java.util.List;

import javax.jms.ConnectionFactory;
import javax.jms.MessageListener;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.transaction.PlatformTransactionManager;
import org.w3c.dom.Document;

import com.v7.qpubsub.app.event.QueueStopPublisher;
import com.v7.qpubsub.queue.domain.MessageType;
import com.v7.qpubsub.queue.domain.PublisherConfig;
import com.v7.qpubsub.queue.domain.QPubSubConfig;
import com.v7.qpubsub.queue.filter.Filter;
import com.v7.qpubsub.queue.listener.ExtendedDocumentMessageListener;
import com.v7.qpubsub.queue.routing.PublishToAllRouter;
import com.v7.qpubsub.queue.routing.Router;
import com.v7.qpubsub.queue.subscribe.Subscriber;

@Configuration
public class DocumentMessageListenerContainerFactory implements ApplicationContextAware {

	@Autowired 
	private List<Filter<Document>> filters; 
	
	@Autowired 
	private List<Router<Document>> routers; 

	@Autowired 
	private List<Subscriber<Document>> subscribers; 
	
	@Autowired 
	private PlatformTransactionManager transactionManager; 
	
	@Autowired
	@Qualifier("pubSubConfig")
	private QPubSubConfig pubSubConfig;
	
	@Autowired
	private ConnectionFactory connectionFactory;

	@Autowired
	private QueueStopPublisher queueStopPublisher;
	
	private ApplicationContext applicationContext;
	
	@Bean
	public MessageListenerContainer createListener() {

		PublisherConfig publisherConfig = pubSubConfig.getPublisherConfig();
		
		Filter<Document> filter = createFilter(publisherConfig);  
		Router<Document> router = createRouter(publisherConfig);
		List<Subscriber<Document>> subscribers = createSubscribers(publisherConfig);
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		
		MessageListener messageListener = null; 
		if(publisherConfig.getMessageType() == MessageType.XML) {
			ExtendedDocumentMessageListener documentMessageListener = new ExtendedDocumentMessageListener(filter, router, subscribers,
																					  publisherConfig.getErrorQueue(), jmsTemplate, 
																					  null,
																					  publisherConfig.getQueueName(),
																					  queueStopPublisher, publisherConfig.getConsumerCount()); 
			
			documentMessageListener.setQueueConfigName(pubSubConfig.getqPubSubConfigName());
			messageListener = documentMessageListener; 
		}
		
		
		((ConfigurableApplicationContext) applicationContext).getBeanFactory().registerSingleton("messageListener", messageListener);
		
		return new MessageListenerContainerBuilder()
				.connectionFactory(connectionFactory)
				.messageListener(messageListener)
				.queueName(publisherConfig.getQueueName())
				.transactionManager(transactionManager)
				.build()
				; 
	}

	private Filter<Document> createFilter(PublisherConfig publisherConfig) {
		Filter<Document> filter = null;
		if (filters.stream().anyMatch(f -> f.isFilterApplicable(publisherConfig.getFilterType()))) {
			filter = filters.stream().filter(f -> f.isFilterApplicable(publisherConfig.getFilterType())).findFirst().get(); 
			filter.setConfig(publisherConfig);
		}
		return filter;
	}

	private Router<Document> createRouter(PublisherConfig publisherConfig) {
		Router<Document> router = new PublishToAllRouter(); 
		if(routers.stream().anyMatch(r -> r.isApplicable(publisherConfig.getRoutingType()))) {
			router = routers.stream().filter(r -> r.isApplicable(publisherConfig.getRoutingType())).findFirst().get();
			router.setConfig(publisherConfig);
		}
		return router;
	}

	private List<Subscriber<Document>> createSubscribers(PublisherConfig publisherConfig) {
		List<Subscriber<Document>> queueSubscribers = new ArrayList<>();
		publisherConfig
				.getSubscriberConfigs()
				.stream()
				.forEach(subscriberConfig -> 
						subscribers
						   .stream()
				           .filter(s-> s.getType() == subscriberConfig.getSubscriberType())
						   .forEach(s -> queueSubscribers.add(s.newInstance(new JmsTemplate(connectionFactory), subscriberConfig)))
						);
		
		return queueSubscribers;
	}
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}
}
