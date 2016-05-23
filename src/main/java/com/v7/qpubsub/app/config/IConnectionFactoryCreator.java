package com.v7.qpubsub.app.config;

import javax.jms.ConnectionFactory;

public interface IConnectionFactoryCreator {

	public ConnectionFactory create();
}
