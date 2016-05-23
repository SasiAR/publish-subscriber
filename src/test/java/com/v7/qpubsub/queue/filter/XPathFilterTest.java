package com.v7.qpubsub.queue.filter;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;

import com.v7.qpubsub.queue.domain.PublisherConfig;
import com.v7.qpubsub.queue.exception.InitializationException;
import com.v7.qpubsub.queue.exception.ProcessingException;
import com.v7.qpubsub.queue.filter.Filter;
import com.v7.qpubsub.queue.filter.XPathFilter;
import com.v7.qpubsub.queue.util.XmlUtil;

public class XPathFilterTest {

	@Test(expected=InitializationException.class)
	public void testInitializationException() {
		Filter<Document> filter= new XPathFilter();
		PublisherConfig config = Mockito.mock(PublisherConfig.class);
		Mockito.when(config.getFilterXPath()).thenReturn(null);
		filter.setConfig(config);
	}
	

	@Test(expected=InitializationException.class)
	public void testIncorrectXPath() {
		Filter<Document> filter=new XPathFilter(); 
		PublisherConfig config = Mockito.mock(PublisherConfig.class);
		Mockito.when(config.getFilterXPath()).thenReturn("//child/testNode==1");
		filter.setConfig(config);
	}

	@Test
	public void testSucessInitialization() {
		Filter<Document> filter=new XPathFilter();
		PublisherConfig config = Mockito.mock(PublisherConfig.class);
		Mockito.when(config.getFilterXPath()).thenReturn("/root/child/child/element/text()='somevalue'");
		filter.setConfig(config);		
	}
	
	@Test
	public void testProcessingNull() throws Exception {
		Filter<Document> filter = new XPathFilter();
		PublisherConfig config = Mockito.mock(PublisherConfig.class);
		Mockito.when(config.getFilterXPath()).thenReturn("/root/child/child/element/text()='somevalue'");
		filter.setConfig(config);		
		Assert.assertFalse(filter.filter(null));
	}

	@Test
	public void testFilterFalse() throws Exception {
		Filter<Document> filter = new XPathFilter();
		PublisherConfig config = Mockito.mock(PublisherConfig.class);
		Mockito.when(config.getFilterXPath()).thenReturn("/root/child/child/element/text()='somevalue'");
		filter.setConfig(config);		
		Document d =XmlUtil.buildDoc("<root><child><child><element>samevalue</element></child></child></root>".getBytes());
		Assert.assertFalse(filter.filter(d));
	}

	@Test
	public void testFilterTrue() throws Exception {
		Filter<Document> filter = new XPathFilter();
		PublisherConfig config = Mockito.mock(PublisherConfig.class);
		Mockito.when(config.getFilterXPath()).thenReturn("/root/child/child/element/text()='somevalue'");
		filter.setConfig(config);
		Document d =XmlUtil.buildDoc("<root><child><child><element>somevalue</element></child></child></root>".getBytes());
		Assert.assertTrue(filter.filter(d));
	}
}
