package com.v7.qpubsub.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("qPubSubProperty")
public class QPubSubProperty {
	
	@Autowired
	@Value("${mq_host_name}")
	private String mqHost;

	@Autowired
	@Value("${mq_port}")
	private String mqPort;

	@Autowired
	@Value("${mq_channel}")
	private String mqChannel;

	@Autowired
	@Value("#{systemProperties['environment']}")
	private String env;
	
	@Autowired
	@Value("#{systemProperties['configLocation']}")
	private String configLocation;

	@Autowired(required=false)
	@Value("#{systemProperties['socketBufferSize']}")
	private Integer socketBufferSize; 
	
	@Autowired(required=false)
	@Value("#{systemProperties['ioBufferSize']}")
	private Integer ioBufferSize; 
	
	@Autowired(required=false)
	@Value("#{systemProperties['prefetchPolicy']}")
	private Integer prefetchPolicy; 
	
	public String getMqHost() {
		return mqHost;
	}

	public String getMqPort() {
		return mqPort;
	}

	public String getMqChannel() {
		return mqChannel;
	}

	public String getEnv() {
		return env;
	}

	public String getConfigLocation() {
		return configLocation;
	}

	public Integer getSocketBufferSize() {
		return socketBufferSize;
	}

	public Integer getIoBufferSize() {
		return ioBufferSize;
	}

	public Integer getPrefetchPolicy() {
		return prefetchPolicy;
	}
	
	
	
}
