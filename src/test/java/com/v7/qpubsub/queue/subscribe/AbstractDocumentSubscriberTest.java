package com.v7.qpubsub.queue.subscribe;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.BytesMessage;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.w3c.dom.Document;

import com.v7.qpubsub.queue.domain.SubscribeType;
import com.v7.qpubsub.queue.domain.SubscriberConfig;
import com.v7.qpubsub.queue.util.XmlUtil;

public class AbstractDocumentSubscriberTest {

	private MockJmsTemplate jmsTemplate = new MockJmsTemplate();
	private final String xml = "<root><child></child></root>";

	@Test
	public void testProcess() throws Exception {
		test(xml);
	}

	private void test(String xml) throws Exception {
		Document document = XmlUtil.buildDoc(xml.getBytes());
		SubscriberConfig subscriberConfig=Mockito.mock(SubscriberConfig.class);
		Mockito.when(subscriberConfig.getQueueName()).thenReturn("QUEUE.NAME");
		Subscriber<Document> subscriber = new AbstractDocumentSubscriber(jmsTemplate, subscriberConfig) {
			@Override
			public boolean subscribe(Document message) {
				return true;
			}

			@Override
			public SubscribeType getType() {
				return SubscribeType.ALL;
			}

			@Override
			public Subscriber<Document> newInstance(JmsTemplate jmsTemplate,SubscriberConfig subscriberConfig) {
				return this;
			}
		};
		subscriber.send(document);
		Message m = jmsTemplate.getMessage();

		Assert.assertTrue(m instanceof BytesMessage);
		Assert.assertEquals(0,((BytesMessage)m).getBodyLength());
	}

	private class MockJmsTemplate extends JmsTemplate {

		private Message message;

		@Override
		public void send(final String destination, final MessageCreator messageCreator) throws JmsException {
			try {
				TextMessage tMsg = Mockito.mock(TextMessage.class);
				BytesMessage bMsg = Mockito.mock(BytesMessage.class);
				Mockito.when(tMsg.getText()).thenReturn(xml);

				Session session = Mockito.mock(Session.class);
				Mockito.when(session.createTextMessage(Mockito.anyString())).thenReturn(tMsg);
				Mockito.when(session.createBytesMessage()).thenReturn(bMsg);

				message = messageCreator.createMessage(session);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

		public Message getMessage() {
			return message;
		}
	}
}
