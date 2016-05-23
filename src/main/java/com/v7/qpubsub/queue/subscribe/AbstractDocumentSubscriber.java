package com.v7.qpubsub.queue.subscribe;

import java.util.Optional;

import javax.jms.BytesMessage;
import javax.jms.Session;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.w3c.dom.Document;

import com.google.common.base.Strings;
import com.v7.qpubsub.queue.domain.PriorityConfig;
import com.v7.qpubsub.queue.domain.SubscriberConfig;
import com.v7.qpubsub.queue.exception.InitializationException;
import com.v7.qpubsub.queue.exception.ProcessingException;
import com.v7.qpubsub.queue.transformer.Transformer;
import com.v7.qpubsub.queue.util.XmlUtil;

@SuppressWarnings("unchecked")
public abstract class AbstractDocumentSubscriber implements Subscriber<Document> {

	private final JmsTemplate jmsTemplate;
	private final SubscriberConfig subscriberConfig;
	private final Transformer<Document> transformer;

	private Logger LOG = LoggerFactory.getLogger(AbstractDocumentSubscriber.class);

	public AbstractDocumentSubscriber() {
		this.subscriberConfig = null;
		this.jmsTemplate = null;
		this.transformer=null;
	};

	public AbstractDocumentSubscriber(JmsTemplate jmsTemplate, SubscriberConfig subscriberConfig) {
		this.jmsTemplate = jmsTemplate;
		this.subscriberConfig = subscriberConfig;
		if (!Strings.isNullOrEmpty(subscriberConfig.getTransformerClass())) {
			try {
				this.transformer = (Transformer<Document>)Class.forName(subscriberConfig.getTransformerClass()).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new InitializationException("unable to instantiate transformer " + subscriberConfig.getTransformerClass());
			}
		} else {
			this.transformer = null;
		}
		jmsTemplate.setDefaultDestinationName(subscriberConfig.getQueueName());
	}

	@Override
	public void send(final Document document) {
		final Document transformerDoc;
		if(transformer == null) {
			 transformerDoc = document;
		} else {
			transformerDoc = transformer.transform(document);
		}

		jmsTemplate.send((Session session) -> {
			try {
				BytesMessage message = session.createBytesMessage();
				message.writeBytes(XmlUtil.getContent(transformerDoc));
				int priority = getPriority(transformerDoc);
				if(priority > 0) {
					LOG.info("creating message with priority " + priority);
					message.setJMSPriority(priority);
				}
				LOG.info(String.format("sending message to queue  %s ", getQueueName()));
				return message;
			} catch (Exception e) {
				LOG.error(ExceptionUtils.getStackTrace(e));
				throw new ProcessingException(e);
			}
		});
	}

	@Override
	public int getPriority(Document message) {
		if (subscriberConfig.getPriorities() == null ) {
			return -1;
		}

		Optional<PriorityConfig> firstmatch = subscriberConfig.getPriorities().stream().filter(p -> isMatch(message,p.getXpath())).findFirst();
		if(firstmatch.isPresent()) {
			return firstmatch.get().getValue();
		}

		return -1;
	}

	private boolean isMatch(Document message, String xpath)  {
		try {
			return Boolean.valueOf(XmlUtil.compile(xpath).evaluate(message));
		} catch (XPathExpressionException e) {
			LOG.error(ExceptionUtils.getStackTrace(e));
			throw new ProcessingException(e);
		}
	}

	@Override
	public String getQueueName() {
		return subscriberConfig.getQueueName();
	}

}
