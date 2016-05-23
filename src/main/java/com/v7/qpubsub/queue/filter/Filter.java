package com.v7.qpubsub.queue.filter;

import com.v7.qpubsub.queue.domain.FilterType;
import com.v7.qpubsub.queue.domain.PublisherConfig;

public interface Filter<T> {

	public boolean filter(T message);
	
	public  FilterType getType();
	
	public void setConfig(PublisherConfig config);

	default public boolean isFilterApplicable(FilterType filterType) {
		return getType() == filterType; 
	}

}
