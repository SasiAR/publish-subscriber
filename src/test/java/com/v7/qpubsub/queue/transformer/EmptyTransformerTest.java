package com.v7.qpubsub.queue.transformer;


import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import com.v7.qpubsub.queue.util.XmlUtil;

public class EmptyTransformerTest {

	@Test
	public void test() throws Exception {
		Transformer<Document> transformer = new EmptyTransformer();
		Document d = XmlUtil.buildDoc("<empty></empty>".getBytes());
		Assert.assertEquals(transformer.transform(d),d);
	}
}
