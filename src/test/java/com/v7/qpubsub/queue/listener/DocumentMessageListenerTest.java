package com.v7.qpubsub.queue.listener;

import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.w3c.dom.Document;

import com.google.common.collect.Lists;
import com.v7.qpubsub.app.event.QueueStopEvent;
import com.v7.qpubsub.app.event.QueueStopPublisher;
import com.v7.qpubsub.queue.exception.ProcessingException;
import com.v7.qpubsub.queue.filter.Filter;
import com.v7.qpubsub.queue.router.MockSubscriber;
import com.v7.qpubsub.queue.routing.PublishToAllRouter;
import com.v7.qpubsub.queue.routing.Router;
import com.v7.qpubsub.queue.transformer.EmptyTransformer;

@SuppressWarnings("unchecked")
public class DocumentMessageListenerTest {

	@Test
	public void testInvalidTextMessage() throws Exception {
		Filter<Document> filter = Mockito.mock(Filter.class);
		Router<Document> router = new PublishToAllRouter();
		String errorQueue = "ERROR.QUEUE";
		MockSubscriber sub1 = new MockSubscriber(true,"SAMPLE.QUEUE1");
		MockSubscriber sub2 = new MockSubscriber(true,"SAMPLE.QUEUE2");
		MockSubscriber sub3 = new MockSubscriber(true,"SAMPLE.QUEUE3");
		JmsTemplate jmsTemplate = Mockito.mock(JmsTemplate.class);
		QueueStopPublisher queueStopPublisher = Mockito.mock(QueueStopPublisher.class);

		ExtendedDocumentMessageListener messageListener =
				new ExtendedDocumentMessageListener(filter, router,
											Lists.newArrayList(sub1,sub2,sub3),
											errorQueue, jmsTemplate, new EmptyTransformer(),
											"PARENT.IN.QUEUE",queueStopPublisher,1);
		ActiveMQTextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("<root><child><child1>TEST</child1><child><root>");
		messageListener.onMessage(textMessage);

		Mockito.verify(jmsTemplate,Mockito.times(1)).send(Mockito.anyString(),(MessageCreator) Mockito.anyObject());
		Mockito.verify(queueStopPublisher, Mockito.never()).publishEvent((QueueStopEvent) Mockito.anyObject());
		Assert.assertFalse(sub1.sendCalled());
		Assert.assertFalse(sub2.sendCalled());
		Assert.assertFalse(sub3.sendCalled());
	}

	@Test
	public void testUnsupportedMessage() throws Exception {
		Filter<Document> filter = Mockito.mock(Filter.class);
		Router<Document> router = new PublishToAllRouter();
		String errorQueue = "ERROR.QUEUE";
		MockSubscriber sub1 = new MockSubscriber(true,"SAMPLE.QUEUE1");
		MockSubscriber sub2 = new MockSubscriber(true,"SAMPLE.QUEUE2");
		MockSubscriber sub3 = new MockSubscriber(true,"SAMPLE.QUEUE3");
		JmsTemplate jmsTemplate = Mockito.mock(JmsTemplate.class);
		QueueStopPublisher queueStopPublisher = Mockito.mock(QueueStopPublisher.class);

		ExtendedDocumentMessageListener messageListener =
				new ExtendedDocumentMessageListener(filter, router,
											Lists.newArrayList(sub1,sub2,sub3),
											errorQueue, jmsTemplate, new EmptyTransformer(),
											"PARENT.IN.QUEUE",queueStopPublisher,null);
		ActiveMQBlobMessage message = new ActiveMQBlobMessage();
		messageListener.onMessage(message);

		Mockito.verify(jmsTemplate,Mockito.times(1)).send(Mockito.anyString(),(MessageCreator) Mockito.anyObject());
		Mockito.verify(queueStopPublisher, Mockito.never()).publishEvent((QueueStopEvent) Mockito.anyObject());
		Assert.assertFalse(sub1.sendCalled());
		Assert.assertFalse(sub2.sendCalled());
		Assert.assertFalse(sub3.sendCalled());
	}

	@Test(expected=ProcessingException.class)
	public void testUnsupportedMessageAndErrorQueueException() throws Exception {
		Filter<Document> filter = Mockito.mock(Filter.class);
		Router<Document> router = new PublishToAllRouter();
		String errorQueue = "ERROR.QUEUE";
		MockSubscriber sub1 = new MockSubscriber(true,"SAMPLE.QUEUE1");
		MockSubscriber sub2 = new MockSubscriber(true,"SAMPLE.QUEUE2");
		MockSubscriber sub3 = new MockSubscriber(true,"SAMPLE.QUEUE3");
		JmsTemplate jmsTemplate = null;
		QueueStopPublisher queueStopPublisher = Mockito.mock(QueueStopPublisher.class);

		ExtendedDocumentMessageListener messageListener =
				new ExtendedDocumentMessageListener(filter, router,
											Lists.newArrayList(sub1,sub2,sub3),
											errorQueue, jmsTemplate, new EmptyTransformer(),
											"PARENT.IN.QUEUE",queueStopPublisher,2);
		messageListener.setQueueConfigName("name");
		ActiveMQBlobMessage message = new ActiveMQBlobMessage();
		messageListener.onMessage(message);

		Mockito.verify(jmsTemplate,Mockito.times(1)).send(Mockito.anyString(),(MessageCreator) Mockito.anyObject());
		Mockito.verify(queueStopPublisher, Mockito.times(1)).publishEvent((QueueStopEvent) Mockito.anyObject());
		Assert.assertFalse(sub1.sendCalled());
		Assert.assertFalse(sub2.sendCalled());
		Assert.assertFalse(sub3.sendCalled());
	}

	@Test(expected=ProcessingException.class)
	public void testInvalidErrorQueue() throws Exception {
		Filter<Document> filter = Mockito.mock(Filter.class);
		Router<Document> router = new PublishToAllRouter();
		String errorQueue = "ERROR.QUEUE";
		MockSubscriber sub1 = new MockSubscriber(true,"SAMPLE.QUEUE1");
		MockSubscriber sub2 = new MockSubscriber(true,"SAMPLE.QUEUE2");
		MockSubscriber sub3 = new MockSubscriber(true,"SAMPLE.QUEUE3");
		JmsTemplate jmsTemplate = null;
		QueueStopPublisher queueStopPublisher = Mockito.mock(QueueStopPublisher.class);

		ExtendedDocumentMessageListener messageListener =
				new ExtendedDocumentMessageListener(filter, router,
											Lists.newArrayList(sub1,sub2,sub3),
											errorQueue, jmsTemplate, new EmptyTransformer(),
											"PARENT.IN.QUEUE",queueStopPublisher,1);
		messageListener.setQueueConfigName("sampleConfig");

		ActiveMQTextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("<root><child><child1>TEST</child1><child><root>");
		messageListener.onMessage(textMessage);

		Mockito.verify(queueStopPublisher, Mockito.times(1)).publishEvent((QueueStopEvent) Mockito.anyObject());
		Assert.assertFalse(sub1.sendCalled());
		Assert.assertFalse(sub2.sendCalled());
		Assert.assertFalse(sub3.sendCalled());
	}

	@Test
	public void testInvalidXmlMessage() throws Exception {
		Filter<Document> filter = Mockito.mock(Filter.class);
		Router<Document> router = new PublishToAllRouter();
		String errorQueue = "ERROR.QUEUE";
		MockSubscriber sub1 = new MockSubscriber(true,"SAMPLE.QUEUE1");
		MockSubscriber sub2 = new MockSubscriber(true,"SAMPLE.QUEUE2");
		MockSubscriber sub3 = new MockSubscriber(true,"SAMPLE.QUEUE3");
		JmsTemplate jmsTemplate = Mockito.mock(JmsTemplate.class);
		QueueStopPublisher queueStopPublisher = Mockito.mock(QueueStopPublisher.class);

		ExtendedDocumentMessageListener messageListener =
				new ExtendedDocumentMessageListener(filter, router,
											Lists.newArrayList(sub1,sub2,sub3),
											errorQueue, jmsTemplate, new EmptyTransformer(),
											"PARENT.IN.QUEUE",queueStopPublisher,0);
		ActiveMQTextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("<root><child><child1>TEST<child1></child></root>");
		messageListener.onMessage(textMessage);

		Mockito.verify(jmsTemplate,Mockito.times(1)).send(Mockito.anyString(),(MessageCreator) Mockito.anyObject());
		Mockito.verify(queueStopPublisher, Mockito.never()).publishEvent((QueueStopEvent) Mockito.anyObject());
		Assert.assertFalse(sub1.sendCalled());
		Assert.assertFalse(sub2.sendCalled());
		Assert.assertFalse(sub3.sendCalled());
	}

	@Test
	public void testSuccess() throws Exception {
		Filter<Document> filter = Mockito.mock(Filter.class);
		Router<Document> router = new PublishToAllRouter();
		String errorQueue = "ERROR.QUEUE";
		MockSubscriber sub1 = new MockSubscriber(true,"SAMPLE.QUEUE1");
		MockSubscriber sub2 = new MockSubscriber(true,"SAMPLE.QUEUE2");
		MockSubscriber sub3 = new MockSubscriber(true,"SAMPLE.QUEUE3");
		JmsTemplate jmsTemplate = Mockito.mock(JmsTemplate.class);
		QueueStopPublisher queueStopPublisher = Mockito.mock(QueueStopPublisher.class);

		ExtendedDocumentMessageListener messageListener =
				new ExtendedDocumentMessageListener(filter, router,
											Lists.newArrayList(sub1,sub2,sub3),
											errorQueue, jmsTemplate, new EmptyTransformer(),
											"PARENT.IN.QUEUE",queueStopPublisher,0);
		ActiveMQBytesMessage bytesMessage = new ActiveMQBytesMessage();
		bytesMessage.writeBytes("<root><child><child1>TEST</child1></child></root>".getBytes());
		bytesMessage.reset();
		messageListener.onMessage(bytesMessage);

		Mockito.verify(jmsTemplate,Mockito.never()).send(Mockito.anyString(),(MessageCreator) Mockito.anyObject());
		Mockito.verify(queueStopPublisher, Mockito.never()).publishEvent((QueueStopEvent) Mockito.anyObject());
		Assert.assertTrue(sub1.sendCalled());
		Assert.assertTrue(sub2.sendCalled());
		Assert.assertTrue(sub3.sendCalled());
	}
}
