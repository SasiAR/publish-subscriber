package com.v7.qpubsub.queue.filter;

import org.junit.Assert;
import org.junit.Test;

import com.v7.qpubsub.queue.filter.PassThruFilter;

public class PassThruFilterTest {

	@Test
	public void test() {
		Assert.assertFalse(new PassThruFilter().filter(null));
	}
}
