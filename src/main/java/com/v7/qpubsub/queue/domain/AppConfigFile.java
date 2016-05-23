package com.v7.qpubsub.queue.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility=Visibility.NONE)
public class AppConfigFile {

	private List<String> configFiles; 
	
	@JsonCreator
	public AppConfigFile(@JsonProperty("configFiles") List<String> configFiles) {
	   this.configFiles = configFiles; 	
	}

	public List<String> getConfigFiles() {
		return configFiles;
	}
	
}
