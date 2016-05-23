package com.v7.qpubsub.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.v7.qpubsub.app.container.QPubSubContainer;
import com.v7.qpubsub.app.domain.QueueStats;
import com.v7.qpubsub.queue.domain.QPubSubConfig;

@RestController
@RequestMapping("/qpubsub/")
public class QPubSubController implements QPubSubControllerRole {

	@Autowired
	@Value("${environment}")
	private String env;
	
	@Autowired
	private QPubSubContainer qpubSubContainer;
	
	@RequestMapping("/env")
	@ResponseBody
	public String getEnv() {
		return "\""+env+"\"";
	}

	@Override
	@RequestMapping("/queueconfigs")
	@ResponseBody
	public Map<String,QPubSubConfig> getQueueConfig() {
		return qpubSubContainer.getConfig();
	}

	@Override
	@RequestMapping("/queuestats")
	@ResponseBody
	public List<QueueStats> getQueueStats() {
		List<QueueStats> stats = new ArrayList<>();
		for(String queueConfigName: qpubSubContainer.getConfig().keySet()) {
			stats.add(new QueueStats(queueConfigName,qpubSubContainer.getStatus(queueConfigName)));
		}
		return stats;
	}

	@Override
	public boolean stopQueueIoc(String queueConfigName) {
		return qpubSubContainer.stopIocContainer(queueConfigName);
	}

	@Override
	public boolean startQueueIoc(String queueConfigName) {
		return qpubSubContainer.startIocContainer(queueConfigName);
	}
	
}
