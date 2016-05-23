package com.v7.qpubsub.queue.domain;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility=Visibility.NONE)
public class QPubSubConfig {

	private String qPubSubConfigName; 
	private PublisherConfig publisherConfig; 
	
	@JsonCreator
	public QPubSubConfig(@JsonProperty("qPubSubConfigName") String qPubSubConfigName,
						@JsonProperty("publisherConfig") PublisherConfig publisherConfig) {
		this.qPubSubConfigName = qPubSubConfigName; 
		this.publisherConfig = publisherConfig; 
	}

	public String getqPubSubConfigName() {
		return qPubSubConfigName;
	}

	public PublisherConfig getPublisherConfig() {
		return publisherConfig;
	}
	
	
}
