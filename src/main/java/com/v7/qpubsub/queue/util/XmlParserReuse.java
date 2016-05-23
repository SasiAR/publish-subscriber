package com.v7.qpubsub.queue.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.v7.qpubsub.queue.exception.InitializationException;

public class XmlParserReuse implements XmlParser {

	private DocumentBuilderFactory factory; 
	private DocumentBuilder builder;
	
	public XmlParserReuse() {
		initalize();
	}
	
	public synchronized Document buildDoc(byte[]  xml) throws Exception {
		return builder.parse(new InputSource(new BufferedInputStream(new ByteArrayInputStream(xml), 77056)));
	}
	
	private void initalize() {
		try {
			factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			factory.setFeature("http://xml.org/sax/features/namespaces", false);
			factory.setFeature("http://xml.org/sax/features/validation", false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new InitializationException(e);
		}
	}
	
	
}
