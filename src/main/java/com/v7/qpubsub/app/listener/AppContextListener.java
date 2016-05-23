package com.v7.qpubsub.app.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.v7.qpubsub.app.container.QPubSubContainer;

@Component
public class AppContextListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		QPubSubContainer container = event.getApplicationContext().getBean(QPubSubContainer.class);
		container.startContainer();
	}

}
