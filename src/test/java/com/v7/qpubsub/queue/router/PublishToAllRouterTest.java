package com.v7.qpubsub.queue.router;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import com.google.common.collect.Lists;
import com.v7.qpubsub.queue.routing.PublishToAllRouter;
import com.v7.qpubsub.queue.routing.Router;
import com.v7.qpubsub.queue.subscribe.Subscriber;
import com.v7.qpubsub.queue.util.XmlUtil;

public class PublishToAllRouterTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() throws Exception {
		Document d = XmlUtil.buildDoc("<root><child><child><element>somevalue</element></child></child></root>".getBytes());
		Subscriber<Document> subscriber1 = new MockSubscriber(true, "Queue1");
		Subscriber<Document> subscriber2 = new MockSubscriber(false,"Queue2");
		Subscriber<Document> subscriber3 = new MockSubscriber(true,"Queue3");
		
		Router<Document> router = new PublishToAllRouter();
		List<Subscriber<Document>> subscribers =Lists.<Subscriber<Document>> newArrayList(subscriber1,subscriber2, subscriber3);
		
		Assert.assertFalse(((MockSubscriber)subscriber1).sendCalled());
		Assert.assertFalse(((MockSubscriber)subscriber2).sendCalled());
		Assert.assertFalse(((MockSubscriber)subscriber3).sendCalled());
		router.route(d, subscribers);
		Assert.assertTrue(((MockSubscriber)subscriber1).sendCalled());
		Assert.assertFalse(((MockSubscriber)subscriber2).sendCalled());
		Assert.assertTrue(((MockSubscriber)subscriber3).sendCalled());
	}

}
