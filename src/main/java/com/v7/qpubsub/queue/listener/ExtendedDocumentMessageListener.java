package com.v7.qpubsub.queue.listener;

import java.util.List;

import org.springframework.jms.core.JmsTemplate;
import org.w3c.dom.Document;

import com.v7.qpubsub.app.event.QueueStopPublisher;
import com.v7.qpubsub.queue.filter.Filter;
import com.v7.qpubsub.queue.routing.Router;
import com.v7.qpubsub.queue.subscribe.Subscriber;
import com.v7.qpubsub.queue.transformer.Transformer;
import com.v7.qpubsub.queue.util.XmlParser;
import com.v7.qpubsub.queue.util.XmlParserReuse;

public class ExtendedDocumentMessageListener extends  AbstractMessageListener<Document> {

	private XmlParser xmlParser; 
	
	public ExtendedDocumentMessageListener(Filter<Document> filter,Router<Document> router, List<Subscriber<Document>> subscribers, 
									String errorQueue, JmsTemplate jmsTemplate, 
									Transformer<Document> transformer, String queueName, QueueStopPublisher queueStopPublisher, Integer consumerCount) {
		super(filter,router,subscribers,errorQueue,jmsTemplate,transformer,queueName,queueStopPublisher, consumerCount);
		this.xmlParser = new XmlParserReuse();
	}

	@Override
	public Document getMsg(byte[] content) throws Exception {
		return xmlParser.buildDoc(content);
	}

	
}
