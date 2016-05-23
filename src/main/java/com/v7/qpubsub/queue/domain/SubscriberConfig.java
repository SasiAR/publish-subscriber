package com.v7.qpubsub.queue.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility=Visibility.NONE)
public class SubscriberConfig {

	private String queueName;
	private SubscribeType subscriberType;
	private String subscribeXPath;
	private List<PriorityConfig> priorities; 
	private String transformerClass; 
	private String filterXpath;
	
	@JsonCreator
	public SubscriberConfig(
			@JsonProperty("queueName") String queueName,
			@JsonProperty("subscriberType") SubscribeType subscriberType,
			@JsonProperty("subscribeXPath") String subscribeXPath,
			@JsonProperty("priorities") List<PriorityConfig> priorities,
			@JsonProperty("transformerClass") String transformerClass,
			@JsonProperty("filterXPath") String filterXPath			
			) {
		this.queueName = queueName; 
		this.subscriberType = subscriberType;
		this.subscribeXPath = subscribeXPath;
		this.priorities = priorities;
		this.transformerClass = transformerClass;
		this.filterXpath = filterXPath;
	}

	public String getQueueName() {
		return queueName;
	}

	public SubscribeType getSubscriberType() {
		return subscriberType;
	}

	public String getSubscribeXPath() {
		return subscribeXPath;
	}

	public List<PriorityConfig> getPriorities() {
		return priorities;
	}

	public String getTransformerClass() {
		return transformerClass;
	}

	public String getFilterXpath() {
		return filterXpath;
	}
	
}
