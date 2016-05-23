package com.v7.qpubsub.app.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@Qualifier("connectionFactoryCreator")
@Profile({"dev","qa","uat","prod"})
public class ConnectionFactoryCreator implements IConnectionFactoryCreator {

	@Autowired
	private QPubSubProperty qPubSubProperty; 
	
	public ConnectionFactory create() {
		return createBasedOnEnv();
	}
	
	private ConnectionFactory createBasedOnEnv() {
		ConnectionFactory nativeConnectionFactory = null;
		ActiveMQConnectionFactory activeMqConnectionFactory = 
				new ActiveMQConnectionFactory("tcp://"+qPubSubProperty.getMqHost()+":"+qPubSubProperty.getMqPort()+
						"?jms.prefetchPolicy.queuePrefetch=0"
						+ "&socketBufferSize=0&ioBufferSize=0");
		activeMqConnectionFactory.setUseAsyncSend(true);
		activeMqConnectionFactory.setOptimizeAcknowledge(true);
		activeMqConnectionFactory.setUseCompression(true);
		nativeConnectionFactory = activeMqConnectionFactory;

		CachingConnectionFactory connectionFactory =  new CachingConnectionFactory(nativeConnectionFactory);
		connectionFactory.setSessionCacheSize(100);
		connectionFactory.setCacheConsumers(true);
		connectionFactory.setCacheProducers(true);
		return connectionFactory;
	}
}
