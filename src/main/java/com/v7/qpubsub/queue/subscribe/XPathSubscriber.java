package com.v7.qpubsub.queue.subscribe;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.v7.qpubsub.queue.domain.SubscribeType;
import com.v7.qpubsub.queue.domain.SubscriberConfig;
import com.v7.qpubsub.queue.exception.InitializationException;
import com.v7.qpubsub.queue.exception.ProcessingException;
import com.v7.qpubsub.queue.util.XmlUtil;

@Component
public class XPathSubscriber extends AbstractDocumentSubscriber {

	private XPathExpression subscriberXpath; 
	private XPathExpression filterXPath;
	private boolean isSubscriber;
	private boolean isFilter;
	private Logger LOG = LoggerFactory.getLogger(XPathSubscriber.class);

	public XPathSubscriber(){};
	
	public XPathSubscriber(JmsTemplate jmsTemplate, SubscriberConfig subscriberConfig)  {
		super(jmsTemplate,subscriberConfig);
		try {
			subscriberXpath = XmlUtil.buildValidateXPath(subscriberConfig.getSubscribeXPath()); 
			filterXPath = XmlUtil.buildValidateXPath(subscriberConfig.getFilterXpath());
			isSubscriber = subscriberXpath != null;
			isFilter = filterXPath != null;
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getStackTrace(e));
			throw new InitializationException(e);
		}
	}

	public boolean subscribe(Document message) {
		try {
			
			if(isSubscriber && !isFilter) {
				return Boolean.valueOf(subscriberXpath.evaluate(message));
			}
			
			if(isFilter && !isSubscriber) {
				return !Boolean.valueOf(filterXPath.evaluate(message)); 
			}
			
			if(isSubscriber && isFilter) {
				return Boolean.valueOf(subscriberXpath.evaluate(message)) && 
						(!Boolean.valueOf(filterXPath.evaluate(message)));
			}
			
			return true; 
			
		} catch (XPathExpressionException e) {
			LOG.error(ExceptionUtils.getStackTrace(e));
			throw new ProcessingException(e); 
		}
	}

	@Override
	public SubscribeType getType() {
		return SubscribeType.XPATH;
	}
	
	@Override
	public Subscriber<Document> newInstance(JmsTemplate jmsTemplate,SubscriberConfig subscriberConfig) {
		return new XPathSubscriber(jmsTemplate, subscriberConfig);
	}
}
