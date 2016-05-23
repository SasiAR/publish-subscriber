package com.v7.qpubsub.queue.routing;

import java.util.List;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.v7.qpubsub.queue.domain.PublisherConfig;
import com.v7.qpubsub.queue.domain.RoutingStrategy;
import com.v7.qpubsub.queue.exception.InitializationException;
import com.v7.qpubsub.queue.exception.ProcessingException;
import com.v7.qpubsub.queue.subscribe.Subscriber;
import com.v7.qpubsub.queue.util.XmlUtil;

@Component
public class XPathRouter implements Router<Document> {

	private XPathExpression xpath;
	private Logger LOG = LoggerFactory.getLogger(XPathRouter.class);
	
	public void route(Document message, List<Subscriber<Document>> subscribers) {
		try {
			String value = xpath.evaluate(message);
			int subscriberNumber = 0; 
			if(value == null) {
				LOG.warn("XPath value is empty, message is always be sent to subscriber 1");
			} else {
				subscriberNumber = (Math.abs(value.hashCode()) + subscribers.size()) % subscribers.size();
			}
			Subscriber<Document> subscriber = subscribers.get(subscriberNumber);
			subscriber.send(message);
		} catch (XPathExpressionException e) {
			LOG.error(ExceptionUtils.getStackTrace(e));
			throw new ProcessingException(e); 
		}

	}

	@Override
	public RoutingStrategy getType() {
		return RoutingStrategy.XPATH;
	}

	@Override
	public void setConfig(PublisherConfig config) {
		try {
			this.xpath = XmlUtil.compile(config.getRoutingXPath());
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getStackTrace(e));
			throw new InitializationException(e);
		}
	}

}
