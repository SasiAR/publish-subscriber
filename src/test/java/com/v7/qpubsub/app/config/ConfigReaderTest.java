package com.v7.qpubsub.app.config;

import org.junit.Assert;
import org.junit.Test;

import com.v7.qpubsub.app.config.ConfigReader;
import com.v7.qpubsub.queue.domain.AppQPubSubConfig;

public class ConfigReaderTest {

	@Test
	public void test() throws Exception {
		AppQPubSubConfig config = ConfigReader.loadConfig("src/test/java/com/v7/qpubsub/app/config/config_files.json");
		Assert.assertEquals("sample",config.getqPubSubConfigs().get(0).getqPubSubConfigName());
		Assert.assertEquals("sample2",config.getqPubSubConfigs().get(1).getqPubSubConfigName());
		Assert.assertEquals("sample3",config.getqPubSubConfigs().get(2).getqPubSubConfigName());
	}
}
