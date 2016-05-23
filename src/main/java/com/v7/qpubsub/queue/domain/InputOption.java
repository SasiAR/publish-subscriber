package com.v7.qpubsub.queue.domain;

import org.kohsuke.args4j.Option;

public class InputOption {

	@Option(name = "-c",aliases="-config",required=true,usage=" config file location")
	private String configLocation;

	@Option(name = "-e",aliases="-environment",required=true,usage="environment")
	private String env;

	public String getConfigLocation() {
		return configLocation;
	}
	
	public String getEnv() {
		return env; 
	}

}
