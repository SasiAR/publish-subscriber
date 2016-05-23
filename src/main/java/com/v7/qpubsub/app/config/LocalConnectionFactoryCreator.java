package com.v7.qpubsub.app.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@Qualifier("connectionFactoryCreator")
@Profile("local")
public class LocalConnectionFactoryCreator implements IConnectionFactoryCreator {

	@Autowired
	private QPubSubProperty qPubSubProperty; 
	
	private boolean isFirstParameter = false;
	private StringBuffer connectionUrl;
	
	private static final Logger LOG = LoggerFactory.getLogger(LocalConnectionFactoryCreator.class);
	public ConnectionFactory create() {
		return createBasedOnEnv();
	}
	
	private ConnectionFactory createBasedOnEnv() {
		ConnectionFactory nativeConnectionFactory = null;
		connectionUrl = new StringBuffer("tcp://"+qPubSubProperty.getMqHost()+":"+qPubSubProperty.getMqPort());
		
		if(qPubSubProperty.getPrefetchPolicy() != null && qPubSubProperty.getPrefetchPolicy() > 0 ) {
			addParameter("jms.prefetchPolicy.all="+qPubSubProperty.getPrefetchPolicy());
		}
		if(qPubSubProperty.getSocketBufferSize() != null && qPubSubProperty.getSocketBufferSize() > 0 ) {
			addParameter("socketBufferSize="+qPubSubProperty.getSocketBufferSize());
		}
		if(qPubSubProperty.getIoBufferSize() != null && qPubSubProperty.getIoBufferSize() > 0 ) {
			addParameter("ioBufferSize="+qPubSubProperty.getIoBufferSize());
		}		
		
		LOG.info(connectionUrl.toString());
		
		ActiveMQConnectionFactory activeMqConnectionFactory = new ActiveMQConnectionFactory(connectionUrl.toString());
		activeMqConnectionFactory.setUseAsyncSend(true);
		activeMqConnectionFactory.setOptimizeAcknowledge(true);
		nativeConnectionFactory = activeMqConnectionFactory;

		CachingConnectionFactory connectionFactory =  new CachingConnectionFactory(nativeConnectionFactory);
		connectionFactory.setSessionCacheSize(100);
		connectionFactory.setCacheConsumers(true);
		connectionFactory.setCacheProducers(true);
		return connectionFactory;
	}
	
	private void addParameter(String parameter) {
		if(isFirstParameter) {
			connectionUrl.append("&");
		} else {
			isFirstParameter=true;
			connectionUrl.append("?");
		}
		connectionUrl.append(parameter);
	}
}
