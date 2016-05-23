package com.v7.qpubsub.app.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.v7.qpubsub.queue.domain.AppConfigFile;
import com.v7.qpubsub.queue.domain.AppQPubSubConfig;
import com.v7.qpubsub.queue.domain.QPubSubConfig;

@Configuration
@DependsOn({"propertyLoader","qPubSubProperty"})
public class ConfigReader {

	@Autowired
	@Value("#{qPubSubProperty.getConfigLocation()}")
	private String configLocation;
	
	@Bean(name="appQPubSubConfig")
	public AppQPubSubConfig createAppCofig() throws Exception {
		return loadConfig(configLocation);
	}
	
	public static AppQPubSubConfig loadConfig(String configFile) throws Exception  {
		AppConfigFile file = parseJson(AppConfigFile.class,configFile); 
		List<QPubSubConfig> qPubSubConfig = new ArrayList<QPubSubConfig>();
		for( String f: file.getConfigFiles()) {
			qPubSubConfig.addAll(parseJson(AppQPubSubConfig.class,f).getqPubSubConfigs());
		}
		return new AppQPubSubConfig(qPubSubConfig);
	}
	
	public static <T> T parseJson(Class<T> t, String fileName) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.ALLOW_COMMENTS, true);
		return mapper.readValue(new File(fileName), t);
	}

}
