package com.v7.qpubsub.app.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.stereotype.Component;

import com.v7.qpubsub.integration.base.AbstractActiveMqBroker;

@Component
@Qualifier("connectionFactoryCreator")
@Profile("test")
public class TestConnectionFactoryCreator implements IConnectionFactoryCreator {

	@Autowired
	private QPubSubProperty qPubSubProperty; 
	
	public ConnectionFactory create() {
		return createBasedOnEnv();
	}
	
	private ConnectionFactory createBasedOnEnv() {
		ActiveMQConnectionFactory activeMqConnectionFactory = AbstractActiveMqBroker.getConnectionFactory();
		activeMqConnectionFactory.setUseAsyncSend(true);

		CachingConnectionFactory connectionFactory =  new CachingConnectionFactory(activeMqConnectionFactory);
		connectionFactory.setSessionCacheSize(100);
		connectionFactory.setCacheConsumers(true);
		connectionFactory.setCacheProducers(true);
		
		return connectionFactory;
	}
}
