package com.v7.qpubsub.queue.domain;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility=Visibility.NONE)
public class PriorityConfig {

	private int value; 
	private String xpath;
	
	@JsonCreator
	public PriorityConfig (@JsonProperty("value") int value, @JsonProperty("xpath") String xpath) {
		this.value = value; 
		this.xpath = xpath;
	}

	public int getValue() {
		return value;
	}

	public String getXpath() {
		return xpath;
	}
	
	
}
