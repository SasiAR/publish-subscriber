package com.v7.qpubsub.queue.subscribe;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.springframework.jms.core.JmsTemplate;
import org.w3c.dom.Document;

import com.v7.qpubsub.queue.domain.SubscriberConfig;
import com.v7.qpubsub.queue.exception.InitializationException;
import com.v7.qpubsub.queue.exception.ProcessingException;
import com.v7.qpubsub.queue.subscribe.Subscriber;
import com.v7.qpubsub.queue.subscribe.XPathSubscriber;
import com.v7.qpubsub.queue.util.XmlUtil;

@RunWith(MockitoJUnitRunner.class)
public class XPathSubscriberTest {

	@Mock
	private JmsTemplate jmsTemplate;

	@Test
	public void testNoXPath() {
		SubscriberConfig config = Mockito.mock(SubscriberConfig.class);
		Mockito.when(config.getSubscribeXPath()).thenReturn(null);
		Mockito.when(config.getQueueName()).thenReturn("QUEUE.NAME.1");
		new XPathSubscriber(jmsTemplate,config);
	}


	@Test(expected=InitializationException.class)
	public void testIncorrectXPath() {
		SubscriberConfig config = Mockito.mock(SubscriberConfig.class);
		Mockito.when(config.getSubscribeXPath()).thenReturn("//child/testNode==1");
		Mockito.when(config.getQueueName()).thenReturn("QUEUE.NAME.1");
		new XPathSubscriber(jmsTemplate,config);
	}

	@Test
	public void testSucessInitialization() {
		SubscriberConfig config = Mockito.mock(SubscriberConfig.class);
		Mockito.when(config.getSubscribeXPath()).thenReturn("/root/child/child/element/text()='somevalue'");
		Mockito.when(config.getQueueName()).thenReturn("QUEUE.NAME.1");
		new XPathSubscriber(jmsTemplate,config);
	}

	public void testProcessingNull() throws Exception {
		SubscriberConfig config = Mockito.mock(SubscriberConfig.class);
		Mockito.when(config.getSubscribeXPath()).thenReturn("/root/child/child/element/text()='somevalue'");
		Mockito.when(config.getQueueName()).thenReturn("QUEUE.NAME.1");
		Subscriber<Document> subscriber = new XPathSubscriber(jmsTemplate,config);
		Assert.assertFalse(subscriber.subscribe(null));
	}

	@Test
	public void testDonotSubscribeBySubscribePath() throws Exception {
		SubscriberConfig config = Mockito.mock(SubscriberConfig.class);
		Mockito.when(config.getSubscribeXPath()).thenReturn("/root/child/child/element/text()='somevalue'");
		Mockito.when(config.getQueueName()).thenReturn("QUEUE.NAME.1");
		Document d =XmlUtil.buildDoc("<root><child><child><element>samevalue</element></child></child></root>".getBytes());
		Subscriber<Document> subscriber = new XPathSubscriber(jmsTemplate,config);
		Assert.assertFalse(subscriber.subscribe(d));
	}

	@Test
	public void testSubscribeBySubscribePath() throws Exception {
		SubscriberConfig config = Mockito.mock(SubscriberConfig.class);
		Mockito.when(config.getSubscribeXPath()).thenReturn("/root/child/child/element/text()='somevalue'");
		Mockito.when(config.getQueueName()).thenReturn("QUEUE.NAME.1");
		Subscriber<Document> subscriber = new XPathSubscriber(jmsTemplate,config);
		Document d =XmlUtil.buildDoc("<root><child><child><element>somevalue</element></child></child></root>".getBytes());
		Assert.assertTrue(subscriber.subscribe(d));
	}

	@Test
	public void testDonotSubscribeByFilterPath() throws Exception {
		SubscriberConfig config = Mockito.mock(SubscriberConfig.class);
		Mockito.when(config.getFilterXpath()).thenReturn("/root/child/child/element/text()='samevalue'");
		Mockito.when(config.getSubscribeXPath()).thenReturn(null);
		Mockito.when(config.getQueueName()).thenReturn("QUEUE.NAME.1");
		Document d =XmlUtil.buildDoc("<root><child><child><element>samevalue</element></child></child></root>".getBytes());
		Subscriber<Document> subscriber = new XPathSubscriber(jmsTemplate,config);
		Assert.assertFalse(subscriber.subscribe(d));
	}

	@Test
	public void testSubscribeByFilterPath() throws Exception {
		SubscriberConfig config = Mockito.mock(SubscriberConfig.class);
		Mockito.when(config.getSubscribeXPath()).thenReturn(null);
		Mockito.when(config.getFilterXpath()).thenReturn("/root/child/child/element/text()='samevalue'");
		Mockito.when(config.getQueueName()).thenReturn("QUEUE.NAME.1");
		Subscriber<Document> subscriber = new XPathSubscriber(jmsTemplate,config);
		Document d =XmlUtil.buildDoc("<root><child><child><element>somevalue</element></child></child></root>".getBytes());
		Assert.assertTrue(subscriber.subscribe(d));
	}
}
