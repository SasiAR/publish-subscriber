package com.v7.qpubsub.queue.listener;

import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.v7.qpubsub.app.event.QueueStopEvent;
import com.v7.qpubsub.app.event.QueueStopPublisher;
import com.v7.qpubsub.queue.exception.ProcessingException;
import com.v7.qpubsub.queue.filter.Filter;
import com.v7.qpubsub.queue.routing.Router;
import com.v7.qpubsub.queue.subscribe.Subscriber;
import com.v7.qpubsub.queue.transformer.Transformer;

public abstract class AbstractMessageListener<T> implements MessageListener {
	private List<Subscriber<T>> subscribers;
	private Router<T> router;
	private Filter<T> filter;
	private String errorQueue; 
	private JmsTemplate jmsTemplate;
	private Transformer<T> transformer; 
	private String queueConfigName;
	private QueueStopPublisher queueStopPublisher;
	private final boolean isFilterAvailable;
	private final boolean isTransformerAvailable; 

	private Logger LOG = LoggerFactory.getLogger(ExtendedDocumentMessageListener.class);

	public AbstractMessageListener(Filter<T> filter,Router<T> router, List<Subscriber<T>> subscribers, 
									String errorQueue, JmsTemplate jmsTemplate, 
									Transformer<T> transformer, String queueName, QueueStopPublisher queueStopPublisher, Integer consumerCount) {
		this.filter = filter;
		this.router = router;
		this.subscribers = subscribers;
		this.errorQueue = errorQueue; 
		this.jmsTemplate = jmsTemplate;
		this.transformer = transformer; 
		this.queueStopPublisher = queueStopPublisher; 
		this.isFilterAvailable = filter !=null;
		this.isTransformerAvailable = transformer != null; 
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
			
			T msg = getMsg(content);

			if (isFilterAvailable && filter.filter(msg)) {
				LOG.info("message filtered");
				return;
			}
			
			if(isTransformerAvailable) {
				msg = transformer.transform(msg);
			}
			
			router.route(msg, subscribers);

		} catch (Exception e) {
			LOG.error(ExceptionUtils.getStackTrace(e));
			sendToErrorQueue(message);
		}
	}

	public abstract T getMsg(byte[] content) throws Exception;
	
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
