package com.v7.qpubsub.queue.routing;

import java.util.List;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.v7.qpubsub.queue.domain.PublisherConfig;
import com.v7.qpubsub.queue.domain.RoutingStrategy;
import com.v7.qpubsub.queue.subscribe.Subscriber;

@Component
public class RoundrobinRouter implements Router<Document> {

	private int lastProcessed=0; 
		
	public void route(Document message, List<Subscriber<Document>> subscribers) {
		int current = (lastProcessed + subscribers.size() ) % subscribers.size(); 
		Subscriber<Document> subscriber = subscribers.get(current);
		subscriber.send(message);
		lastProcessed++;
		return; 
	}

	@Override
	public RoutingStrategy getType() {
		return RoutingStrategy.ROUNDROBIN;
	}

	@Override
	public void setConfig(PublisherConfig config) {
		return; 
	}

}
