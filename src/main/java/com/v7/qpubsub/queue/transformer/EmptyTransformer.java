package com.v7.qpubsub.queue.transformer;

import org.w3c.dom.Document;

public class EmptyTransformer implements Transformer<Document> {

	@Override
	public Document transform(Document message) {
		return message;
	}

}
