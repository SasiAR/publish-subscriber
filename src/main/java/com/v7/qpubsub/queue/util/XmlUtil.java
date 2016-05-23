package com.v7.qpubsub.queue.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import com.google.common.base.Strings;
import com.v7.qpubsub.queue.exception.InitializationException;

public class XmlUtil {

	private static XPath xpath = XPathFactory.newInstance().newXPath();
	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
	private static Transformer transformer;
	
	static {
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new InitializationException(e);
		}
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); 
	}
	
	public static XPathExpression compile(String path) throws XPathExpressionException {
		return xpath.compile(path);
	}
	
	public static Document buildDoc(byte[] xml) throws Exception {
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new BufferedInputStream(new ByteArrayInputStream(xml))));
	}
	
	public static byte[] getContent(Document doc) throws Exception  {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(new BufferedOutputStream(writer,65536)));
        writer.flush();
        return writer.toByteArray();
	}
	
	public static XPathExpression buildValidateXPath(String xpath) throws XPathExpressionException {
		if(Strings.isNullOrEmpty(xpath)) {
			return null;
		}
		return compile(xpath);
	}
	
	public static String toString(Document d) {
	   DOMImplementationLS domImplementation = (DOMImplementationLS) d.getImplementation();
	   LSSerializer lsSerializer = domImplementation.createLSSerializer();
	   return lsSerializer.writeToString(d);  
	}
	
}
