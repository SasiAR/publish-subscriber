package com.v7.qpubsub.queue.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility=Visibility.NONE)
public class PublisherConfig {

	private String queueManager; 
	private String queueName; 
	private MessageType messageType;
	private RoutingStrategy routingType;
	private String routingXPath; 
	private FilterType filterType;
	private String filterXPath;
	private List<SubscriberConfig> subscriberConfigs; 
	private String errorQueue; 
	private String transformerClass; 
	private Integer consumerCount;
	
	@JsonCreator
	public PublisherConfig(
			@JsonProperty("queueManager") String queueManager, 
			@JsonProperty("queueName") String queueName,
			@JsonProperty("messageType") MessageType messageType,
			@JsonProperty("routingStrategy") RoutingStrategy routingType,
			@JsonProperty("routingXPath") String routingXPath,
			@JsonProperty("filterType") FilterType filterType,
			@JsonProperty("filterXPath") String filterXPath,
			@JsonProperty("subscribers") List<SubscriberConfig> subscribers,
			@JsonProperty("errorQueue") String errorQueue,
			@JsonProperty("transformerClass") String transformerClass,
			@JsonProperty("consumerCount") Integer consumerCount
			) {
		this.queueManager = queueManager;
		this.queueName = queueName;
		this.messageType = messageType; 
		this.routingType = routingType; 
		this.routingXPath = routingXPath; 
		this.filterType = filterType; 
		this.filterXPath = filterXPath;
		this.subscriberConfigs = subscribers;
		this.errorQueue = errorQueue;
		this.transformerClass = transformerClass;
		this.consumerCount = consumerCount;
	}

	public String getQueueManager() {
		return queueManager;
	}

	public String getQueueName() {
		return queueName;
	}

	public RoutingStrategy getRoutingType() {
		return routingType;
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public String getFilterXPath() {
		return filterXPath;
	}

	public List<SubscriberConfig> getSubscriberConfigs() {
		return subscriberConfigs;
	}

	public String getRoutingXPath() {
		return routingXPath;
	}
	
	public String getErrorQueue() {
		return errorQueue;
	}
	
	public String getTransfromerClass() {
		return transformerClass;
	}
	
	public Integer getConsumerCount() {
		return consumerCount;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public String getTransformerClass() {
		return transformerClass;
	}
	
}
