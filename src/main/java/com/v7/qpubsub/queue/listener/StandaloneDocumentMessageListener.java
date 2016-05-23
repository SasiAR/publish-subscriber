package com.v7.qpubsub.queue.listener;

import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import com.v7.qpubsub.app.event.QueueStopEvent;
import com.v7.qpubsub.app.event.QueueStopPublisher;
import com.v7.qpubsub.queue.exception.ProcessingException;
import com.v7.qpubsub.queue.filter.Filter;
import com.v7.qpubsub.queue.routing.Router;
import com.v7.qpubsub.queue.subscribe.Subscriber;
import com.v7.qpubsub.queue.transformer.Transformer;
import com.v7.qpubsub.queue.util.XmlParser;
import com.v7.qpubsub.queue.util.XmlParserReuse;

public class StandaloneDocumentMessageListener implements MessageListener {

	private List<Subscriber<Document>> subscribers;
	private Router<Document> router;
	private Filter<Document> filter;
	private String errorQueue; 
	private JmsTemplate jmsTemplate;
	private Transformer<Document> transformer; 
	private String queueConfigName;
	private QueueStopPublisher queueStopPublisher;
	private XmlParser xmlParser; 
	
	private Logger LOG = LoggerFactory.getLogger(StandaloneDocumentMessageListener.class);

	public StandaloneDocumentMessageListener(Filter<Document> filter,Router<Document> router, List<Subscriber<Document>> subscribers, 
									String errorQueue, JmsTemplate jmsTemplate, 
									Transformer<Document> transformer, String queueName, QueueStopPublisher queueStopPublisher, Integer consumerCount) {
		this.filter = filter;
		this.router = router;
		this.subscribers = subscribers;
		this.errorQueue = errorQueue; 
		this.jmsTemplate = jmsTemplate;
		this.transformer = transformer; 
		this.queueStopPublisher = queueStopPublisher; 
		this.xmlParser = new XmlParserReuse();
		
		MDC.put("parentqueue", queueName);
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public void onMessage(Message message) {
		try {
			BytesMessage byteMsg = ((BytesMessage) message);
			byte [] content=null;
			int length = (int)byteMsg.getBodyLength();	
			content = new byte [length];
			byteMsg.readBytes(content);
			
			Document doc = xmlParser.buildDoc(content);

			if (filter != null && filter.filter(doc)) {
				LOG.info("message filtered");
				return;
			}

			if(transformer != null) {
				doc = transformer.transform(doc);
			}

			router.route(doc, subscribers);
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getStackTrace(e));
			sendToErrorQueue(message);
		}
		
	}
	
	private void sendToErrorQueue(Message errorMessage) {
		try {
			LOG.error("sending the message to error Queue " + errorQueue);
			jmsTemplate.send(errorQueue, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return errorMessage;
			}});
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getStackTrace(e));
			LOG.error("stopping the listener for queue " + queueConfigName);
			queueStopPublisher.publishEvent(new QueueStopEvent(queueConfigName));
			throw new ProcessingException(e);
		}
	}

	public void setQueueConfigName(String queueConfigName) {
		this.queueConfigName = queueConfigName; 
	}
	
}
