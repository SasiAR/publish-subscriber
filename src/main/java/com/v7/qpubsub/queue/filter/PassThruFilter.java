package com.v7.qpubsub.queue.filter;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.v7.qpubsub.queue.domain.FilterType;
import com.v7.qpubsub.queue.domain.PublisherConfig;

@Component
public class PassThruFilter implements Filter<Document> {

	public boolean filter(Document doc) {
		return false;
	}

	@Override
	public FilterType getType() {
		return FilterType.PASSTHRU;
	}

	@Override
	public void setConfig(PublisherConfig config) {
		return; 
	}

}
