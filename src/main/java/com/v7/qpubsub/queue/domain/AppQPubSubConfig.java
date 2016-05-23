package com.v7.qpubsub.queue.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility=Visibility.NONE)
public class AppQPubSubConfig {

	 private List<QPubSubConfig> qPubSubConfigs; 
	 
	 @JsonCreator
	 public AppQPubSubConfig(@JsonProperty("qPubSubConfigs") List<QPubSubConfig> qPubSubConfigs) {
		 this.qPubSubConfigs = qPubSubConfigs;
	 }

	public List<QPubSubConfig> getqPubSubConfigs() {
		return qPubSubConfigs;
	}
	 
	 
}
