package com.v7.qpubsub.app.container;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import com.v7.qpubsub.app.config.IConnectionFactoryCreator;
import com.v7.qpubsub.app.domain.Status;
import com.v7.qpubsub.app.event.QueueStopPublisher;
import com.v7.qpubsub.app.validator.QPubSubConfigValidator;
import com.v7.qpubsub.queue.domain.AppQPubSubConfig;
import com.v7.qpubsub.queue.domain.QPubSubConfig;
import com.v7.qpubsub.queue.exception.InitializationException;
import com.v7.qpubsub.queue.exception.ProcessingException;

@Component
public class QPubSubContainer {

	private Logger LOG = LoggerFactory.getLogger(QPubSubContainer.class);
	
	@Autowired
	@Qualifier("appQPubSubConfig")
	private AppQPubSubConfig appQPubSubConfig;
	
	@Autowired
	private IConnectionFactoryCreator connectionFactoryCreator;
	
	@Autowired
	private QueueStopPublisher queueStopPublisher;
	
	@Autowired 
	private QPubSubConfigValidator qPubSubConfigValidator;

	private Map<String, ClassPathXmlApplicationContext> iocContainerMap = new HashMap<>();
	private Map<String, QPubSubConfig> configMap = new HashMap<>();
	
	public void startContainer() {
		for(QPubSubConfig config: appQPubSubConfig.getqPubSubConfigs()) {
			if(qPubSubConfigValidator.validate(config)) {
				createSpringIocContainer(config);
			} else {
				stopAllIocContainer();
				throw new InitializationException("config is not valid");
			}
		}
	}
	
	private void createSpringIocContainer(QPubSubConfig config) {
		if(configMap.containsKey(config.getqPubSubConfigName())) {
			throw new ProcessingException("duplicate config :" + config.getqPubSubConfigName());
		}
		ClassPathXmlApplicationContext parentApplicationContext = new ClassPathXmlApplicationContext();
		parentApplicationContext.refresh();
		
		parentApplicationContext.getBeanFactory().registerSingleton("pubSubConfig", config);
		parentApplicationContext.getBeanFactory().registerSingleton("connectionFactory", connectionFactoryCreator.create());
		parentApplicationContext.getBeanFactory().registerSingleton("rootContext", parentApplicationContext);
		parentApplicationContext.getBeanFactory().registerSingleton("queueStopPublisher", queueStopPublisher);
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(parentApplicationContext);
		
		applicationContext.setConfigLocation("qpubsub-config.xml");
		applicationContext.refresh();
		configMap.put(config.getqPubSubConfigName(), config);
		iocContainerMap.put(config.getqPubSubConfigName(),  applicationContext);
		LOG.info("started queue config " + config.getqPubSubConfigName());
	}
	
	public void stopAllIocContainer() {
		for(ClassPathXmlApplicationContext applicationContext: iocContainerMap.values()) {	
			applicationContext.getBean(MessageListenerContainer.class).stop();
			applicationContext.stop();
		}
	}

	public Map<String, QPubSubConfig> getConfig() {
		return configMap;
	}
	
	public boolean stopIocContainer(String queueConfigName){
		LOG.info("stop queue config " + queueConfigName);
		iocContainerMap.get(queueConfigName).stop();
		return true;
	}
	
	public boolean startIocContainer(String queueConfigName){
		LOG.info("start queue config " + queueConfigName);
		iocContainerMap.get(queueConfigName).start();
		return true;
	}
	
	public Status getStatus(String queueConfigName) {
		if(iocContainerMap.get(queueConfigName).isRunning()) {
			return Status.RUNNING;
		}
		return Status.STOPPED;
	}
}
