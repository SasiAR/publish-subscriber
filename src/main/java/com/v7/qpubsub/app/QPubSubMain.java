package com.v7.qpubsub.app;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.system.ApplicationPidListener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

import com.v7.qpubsub.app.container.QPubSubContainer;
import com.v7.qpubsub.queue.domain.InputOption;
import com.v7.qpubsub.queue.exception.ProcessingException;

@EnableAutoConfiguration
@ComponentScan
public class QPubSubMain {

	private static Logger LOG = LoggerFactory.getLogger(QPubSubMain.class);
	
	private static ConfigurableApplicationContext configurableApplicationContext; 
	
	public static void main(String[] args) {
		InputOption inputOption = new InputOption();
		CmdLineParser cmdLineParser = new CmdLineParser(inputOption);
		try {
			cmdLineParser.parseArgument(args);
			System.setProperty("configLocation",inputOption.getConfigLocation());
			System.setProperty("environment",inputOption.getEnv());
		} catch (CmdLineException e) {
			LOG.error(ExceptionUtils.getStackTrace(e));
			throw new ProcessingException(e);
		}
		
		SpringApplication application = new SpringApplication (QPubSubMain.class);
		application.addListeners(new ApplicationPidListener("qpubsub-app.pid"));
		ConfigurableEnvironment env = new StandardEnvironment();
		env.setActiveProfiles(inputOption.getEnv());
		application.setEnvironment(env);
		configurableApplicationContext = application.run(args);
	}
	
	public static void stop() {
		configurableApplicationContext.getBean(QPubSubContainer.class).stopAllIocContainer();
		configurableApplicationContext.close();
		
	}
}
