package com.v7.qpubsub.queue.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import com.v7.qpubsub.queue.exception.InitializationException;

public class XmlParserThreadSafe implements XmlParser {

	private DocumentBuilderFactory factory;

	public XmlParserThreadSafe() {
		initalize();
	}

	public Document buildDoc(byte[] xml) throws Exception {
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new BufferedInputStream(new ByteArrayInputStream(xml), 65536));
	}

	private void initalize() {
		try {
			factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			factory.setFeature("http://xml.org/sax/features/namespaces", false);
			factory.setFeature("http://xml.org/sax/features/validation", false);
			factory.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
					false);
			factory.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-external-dtd",
					false);
		} catch (ParserConfigurationException e) {
			throw new InitializationException(e);
		}
	}

}
