package com.v7.qpubsub.queue.util;

import org.w3c.dom.Document;

public interface XmlParser {
	public Document buildDoc(byte[]  xml) throws Exception;	
}
