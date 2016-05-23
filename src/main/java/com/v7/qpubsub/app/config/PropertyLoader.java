package com.v7.qpubsub.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class PropertyLoader {
	
	private Logger LOG = LoggerFactory.getLogger(QPubSubProperty.class);
	
	@Bean
	public PropertyPlaceholderConfigurer loadProperties() {
		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		String propertyFile = "property/"+System.getProperty("environment")+".properties";
		LOG.info("loading property file " + propertyFile);
		configurer.setLocation(new ClassPathResource(propertyFile));
		return configurer; 
	}

}
