package com.v7.qpubsub.integration.base;

import java.util.List;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import com.google.common.collect.Lists;

public class AbstractActiveMqBroker {

	private static ActiveMQConnectionFactory connectionFactory; 
	private static BrokerService brokerService; 
	private static Connection connection;

	public static void main(String[] args) throws Exception  {
		new AbstractActiveMqBroker();
		System.exit(0);
	}
	
	public AbstractActiveMqBroker(String ...queues) throws Exception {
		System.out.println("starting broker");
		connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
		connection = connectionFactory.createConnection();
		connection.start();
		createQueue(queues);
	}
	
	public static void createQueue(String...queues) throws Exception {
		Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
		for(String queue: queues) {
			session.createQueue(queue);
		}
	}
	
	public static ActiveMQConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}
	
	public void postMessage(String queueName, String message, int times) throws JMSException {
		Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(queueName);
		MessageProducer producer = session.createProducer(queue);
		System.out.println("starting to add message to queue " + queueName);
		for(int i =0; i < times; i++) {
			producer.send(session.createTextMessage(message.replace("{{times}}", Integer.toString(i))));
		}
		System.out.println("done posting messages");
		producer.close();
		session.close();
	}
	
	public List<Message> getAllMessages(String queue) throws Exception {
		System.out.println("getting all messages for " + queue);
		Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(queue);
		MessageConsumer consumer = session.createConsumer(destination);
		List<Message> messages = Lists.<Message>newArrayList();
		Message m = null;
		while((m = consumer.receiveNoWait()) != null) {
			messages.add(m);
		}
		return messages;
	}
	
	public static void stopBroker() throws Exception {
		connection.close();
	}
}
