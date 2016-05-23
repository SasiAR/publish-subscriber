package com.v7.qpubsub.queue.routing;

import java.util.List;

import com.v7.qpubsub.queue.domain.PublisherConfig;
import com.v7.qpubsub.queue.domain.RoutingStrategy;
import com.v7.qpubsub.queue.subscribe.Subscriber;

public interface Router<T> {
	public void route(T message, List<Subscriber<T>> subscribers);
	
	public RoutingStrategy getType();
	
	public void setConfig(PublisherConfig config);
	
	default public boolean isApplicable(RoutingStrategy routingType) {
		return routingType == getType();
	}
}
