package com.v7.qpubsub.queue.filter;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.v7.qpubsub.queue.domain.FilterType;
import com.v7.qpubsub.queue.domain.PublisherConfig;
import com.v7.qpubsub.queue.exception.InitializationException;
import com.v7.qpubsub.queue.exception.ProcessingException;
import com.v7.qpubsub.queue.util.XmlUtil;

@Component
public class XPathFilter implements Filter<Document> {

	private XPathExpression xpath; 
	private Logger LOG = LoggerFactory.getLogger(XPathFilter.class);

	public boolean filter(Document message)  {
		try {
			return Boolean.valueOf(xpath.evaluate(message));
		} catch (XPathExpressionException e) {
			LOG.error(ExceptionUtils.getStackTrace(e));
			throw new ProcessingException(e); 
		}
	}

	@Override
	public FilterType getType() {
		return FilterType.XPATH;
	}

	@Override
	public void setConfig(PublisherConfig config) {
		try {
			this.xpath = XmlUtil.compile(config.getFilterXPath());
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getStackTrace(e));
			throw new InitializationException(e);
		}
	}

}
