package com.v7.qpubsub.queue.router;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;

import com.google.common.collect.Lists;
import com.v7.qpubsub.queue.domain.PublisherConfig;
import com.v7.qpubsub.queue.routing.Router;
import com.v7.qpubsub.queue.routing.XPathRouter;
import com.v7.qpubsub.queue.subscribe.Subscriber;
import com.v7.qpubsub.queue.util.XmlUtil;

@SuppressWarnings("unchecked")
public class XPathRouterTest {

	@Test
	public void testRouting() throws Exception {
		Document d = XmlUtil.buildDoc("<root><child><child><tradeId>7</tradeId></child></child></root>".getBytes());
		Subscriber<Document> subscriber1 = new MockSubscriber(true, "Queue1");
		Subscriber<Document> subscriber2 = new MockSubscriber(false,"Queue2");
		Subscriber<Document> subscriber3 = new MockSubscriber(true,"Queue3");

		List<Subscriber<Document>> subscribers =Lists.<Subscriber<Document>> newArrayList(subscriber1,subscriber2, subscriber3);

		Router<Document> router = new XPathRouter();
		PublisherConfig config = Mockito.mock(PublisherConfig.class);
		Mockito.when(config.getRoutingXPath()).thenReturn("/root/child/child/tradeId/text()");
		router.setConfig(config);

		((MockSubscriber) subscriber1).setSendCalled(false);
		((MockSubscriber) subscriber2).setSendCalled(false);
		((MockSubscriber) subscriber3).setSendCalled(false);
		router.route(d, subscribers);
		Assert.assertFalse(((MockSubscriber) subscriber1).sendCalled());
		Assert.assertTrue(((MockSubscriber) subscriber2).sendCalled());
		Assert.assertFalse(((MockSubscriber) subscriber3).sendCalled());
		
		Document d2 = XmlUtil.buildDoc("<root><child><child><tradeId>3</tradeId></child></child></root>".getBytes());
		((MockSubscriber) subscriber1).setSendCalled(false);
		((MockSubscriber) subscriber2).setSendCalled(false);
		((MockSubscriber) subscriber3).setSendCalled(false);
		router.route(d2, subscribers);
		Assert.assertTrue(((MockSubscriber) subscriber1).sendCalled());
		Assert.assertFalse(((MockSubscriber) subscriber2).sendCalled());
		Assert.assertFalse(((MockSubscriber) subscriber3).sendCalled());

		Document d3 = XmlUtil.buildDoc("<root><child><child><tradeId>14</tradeId></child></child></root>".getBytes());
		((MockSubscriber) subscriber1).setSendCalled(false);
		((MockSubscriber) subscriber2).setSendCalled(false);
		((MockSubscriber) subscriber3).setSendCalled(false);
		router.route(d3, subscribers);
		Assert.assertFalse(((MockSubscriber) subscriber1).sendCalled());
		Assert.assertFalse(((MockSubscriber) subscriber2).sendCalled());
		Assert.assertTrue(((MockSubscriber) subscriber3).sendCalled());
		
	}
	
	@Test
	public void testSameQueueRouter() throws Exception {
		Document d = XmlUtil.buildDoc("<root><child><child><tradeId>T12345</tradeId></child></child></root>".getBytes());
		Subscriber<Document> subscriber1 = new MockSubscriber(true, "Queue1");
		Subscriber<Document> subscriber2 = new MockSubscriber(false,"Queue2");
		Subscriber<Document> subscriber3 = new MockSubscriber(true,"Queue3");

		List<Subscriber<Document>> subscribers =Lists.<Subscriber<Document>> newArrayList(subscriber1,subscriber2, subscriber3);

		Router<Document> router = new XPathRouter();
		PublisherConfig config = Mockito.mock(PublisherConfig.class);
		Mockito.when(config.getRoutingXPath()).thenReturn("/root/child/child/tradeId/text()");
		router.setConfig(config);
		
		((MockSubscriber) subscriber1).setSendCalled(false);
		((MockSubscriber) subscriber2).setSendCalled(false);
		((MockSubscriber) subscriber3).setSendCalled(false);

		router.route(d, subscribers);
		Assert.assertFalse(((MockSubscriber) subscriber1).sendCalled());
		Assert.assertTrue(((MockSubscriber) subscriber2).sendCalled());
		Assert.assertFalse(((MockSubscriber) subscriber3).sendCalled());

		((MockSubscriber) subscriber1).setSendCalled(false);
		((MockSubscriber) subscriber2).setSendCalled(false);
		((MockSubscriber) subscriber3).setSendCalled(false);
		router.route(d, subscribers);
		Assert.assertFalse(((MockSubscriber) subscriber1).sendCalled());
		Assert.assertTrue(((MockSubscriber) subscriber2).sendCalled());
		Assert.assertFalse(((MockSubscriber) subscriber3).sendCalled());

		((MockSubscriber) subscriber1).setSendCalled(false);
		((MockSubscriber) subscriber2).setSendCalled(false);
		((MockSubscriber) subscriber3).setSendCalled(false);
		router.route(d, subscribers);
		Assert.assertFalse(((MockSubscriber) subscriber1).sendCalled());
		Assert.assertTrue(((MockSubscriber) subscriber2).sendCalled());
		Assert.assertFalse(((MockSubscriber) subscriber3).sendCalled());
				
	}
}
