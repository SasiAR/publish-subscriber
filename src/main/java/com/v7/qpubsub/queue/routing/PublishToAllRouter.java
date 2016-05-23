package com.v7.qpubsub.queue.routing;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.v7.qpubsub.queue.domain.PublisherConfig;
import com.v7.qpubsub.queue.domain.RoutingStrategy;
import com.v7.qpubsub.queue.subscribe.Subscriber;

public class PublishToAllRouter implements Router<Document>{

	private Logger LOG = LoggerFactory.getLogger(PublishToAllRouter.class);

	@Override
	public void route(Document message, List<Subscriber<Document>> subscribers) {
		for(Subscriber<Document> subscriber: subscribers) {
			if (subscriber.subscribe(message)) {
				subscriber.send(message);
			} else {
				LOG.info("message not subscribed by " + subscriber.getQueueName());
			}
		}
	}

	@Override
	public RoutingStrategy getType() {
		return RoutingStrategy.PUBLISH_TO_ALL;
	}

	@Override
	public void setConfig(PublisherConfig config) {
		return; 
	}

}
