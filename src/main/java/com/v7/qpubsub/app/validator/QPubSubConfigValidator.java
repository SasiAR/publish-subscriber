package com.v7.qpubsub.app.validator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.v7.qpubsub.queue.domain.FilterType;
import com.v7.qpubsub.queue.domain.MessageType;
import com.v7.qpubsub.queue.domain.QPubSubConfig;
import com.v7.qpubsub.queue.domain.RoutingStrategy;
import com.v7.qpubsub.queue.domain.SubscribeType;
import com.v7.qpubsub.queue.domain.SubscriberConfig;

@Component
public class QPubSubConfigValidator {

	private Logger LOG = LoggerFactory.getLogger(QPubSubConfigValidator.class);
	
	public boolean validate(QPubSubConfig config) {
		LOG.info("validating config " + config.getqPubSubConfigName() );
		boolean valid = true; 

		if(StringUtils.isBlank(config.getqPubSubConfigName())) {
			valid = false; 
			LOG.error("config name is empty");
		}
		
		//should have one publisher 
		if(StringUtils.isBlank(config.getPublisherConfig().getQueueName())) {
			valid = false; 
			LOG.error("no publisher queue defined");
		}
		
		if(StringUtils.isBlank(config.getPublisherConfig().getErrorQueue())) {
			valid = false; 
			LOG.error("no error queue defined");			
		}

		if(config.getPublisherConfig().getMessageType() == null) {
			valid = false; 
			LOG.error("message type is not defined");
		}
		if(config.getPublisherConfig().getRoutingType() == null) {
			valid = false; 
			LOG.error("routing strategy is null");						
		}
		
		if(config.getPublisherConfig().getRoutingType() == RoutingStrategy.XPATH && 
		   StringUtils.isBlank(config.getPublisherConfig().getRoutingXPath())) {
			valid =false; 
			LOG.error("'routing strategy xpath with no XPATH provided");
		}
		
		if(StringUtils.isNoneBlank(config.getPublisherConfig().getTransfromerClass() )) {
			try {
				Class.forName(config.getPublisherConfig().getTransfromerClass());
			} catch (Exception e) {
				valid = false; 
				LOG.error("transformer not found " + config.getPublisherConfig().getTransfromerClass() );
			}
		}
		
		if(config.getPublisherConfig().getFilterType() == FilterType.XPATH && 
		   StringUtils.isBlank(config.getPublisherConfig().getFilterXPath())) {
			valid = false; 
			LOG.error(" filter type is xpath but no xpath provided");
		}
		
		if(config.getPublisherConfig().getSubscriberConfigs() == null ||
		   config.getPublisherConfig().getSubscriberConfigs().size() == 0) {
			valid = false; 
			LOG.error("no subscribers configured");
		}
		
		for(SubscriberConfig sConfig : config.getPublisherConfig().getSubscriberConfigs()) {
			if(config.getPublisherConfig().getRoutingType() == RoutingStrategy.ROUNDROBIN  && 
			   sConfig.getSubscriberType() == SubscribeType.XPATH) {
				valid = false; 
				LOG.error("routing type is Roundrobin but subsriber is xpath or has filter");
			}
			
			if(config.getPublisherConfig().getRoutingType() == RoutingStrategy.XPATH  && 
				sConfig.getSubscriberType() == SubscribeType.XPATH) {
				valid = false; 
				LOG.error("routing type is by xpath value but subsriber is xpath or has filter");
			}
			
			if(sConfig.getSubscriberType() == SubscribeType.XPATH && 
			   (StringUtils.isBlank(sConfig.getFilterXpath()) && StringUtils.isBlank(sConfig.getSubscribeXPath()))) {
				valid = false; 
				LOG.error("subscriber type is xpath but no subscribe xpath or filter xpath is provided");
			}
		}
		
		return valid;
	}
}
