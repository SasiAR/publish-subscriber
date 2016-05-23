package com.v7.qpubsub.queue.subscribe;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.v7.qpubsub.queue.domain.SubscriberConfig;
import com.v7.qpubsub.queue.subscribe.SubcribeToAllSubscriber;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

@RunWith(MockitoJUnitRunner.class)
public class SubcribeToAllSubscribeTest {

	@Mock
	private JmsTemplate jmsTemplate;

	@Test
	public void test() {
		SubscriberConfig config = Mockito.mock(SubscriberConfig.class);
		Mockito.when(config.getQueueName()).thenReturn("QUEUE.NAME.1");
		Assert.assertTrue(new SubcribeToAllSubscriber(jmsTemplate,config).subscribe(null));
	}
}
