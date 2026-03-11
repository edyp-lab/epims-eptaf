/*
 * Copyright (C) 2021
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the CeCILL FREE SOFTWARE LICENSE AGREEMENT
 * ; either version 2.1 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * CeCILL License V2.1 for more details.
 *
 * You should have received a copy of the CeCILL License
 * along with this program;
 * If not, see <http://www.cecill.info/licences/Licence_CeCILL_V2.1-en.html>.
 */
package fr.edyp.eptaf;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jndi.JndiTemplate;

import org.springframework.boot.SpringApplication;

import javax.swing.*;

public class TransfertApp {

	private static Logger logger;
	
	private static void logConfiguration(BeanFactory context) {
		JndiTemplate jndiTemplate = (JndiTemplate)context.getBean("jndiTemplate");
		logger.info("Config -- Jms URL Provider = "+jndiTemplate.getEnvironment().get("java.naming.provider.url"));
		FTPConfiguration config = (FTPConfiguration)context.getBean("ftpConfiguration");
		logger.info("Config -- FTP Host = "+config.getHost());
		logger.info("Config -- FTP Port = "+config.getPort());
		logger.info("Config -- FTP Login = "+config.getLogin());
		logger.info("Config -- FTP Auth Mode = "+config.getAuthMode());
		if(config.getAuthMode().equals(FTPConfiguration.AuthMode.PASSWD_MODE))
			logger.info("Config -- FTP Password = "+config.getPassword());
		else
			logger.info("Config -- FTP Key Path = "+config.getKeyPath());

		Destination d = (Destination)context.getBean("defaultDestination");
		logger.info("Config -- Default destination for files = "+d.getPath());
		MessageHandler handler = MessageHandler.getInstance();
		logger.info("Config -- Message Handler configuration : ");
		logger.info("Config -- -- Default destination : "+handler.getDefaultDestination().getPath());
		logger.info("Config -- -- Message filter : ");		
		for (String s : handler.getMessageFilter().getConfiguration()) {
			logger.info("Config -- -- -- "+s);
		}
		logger.info("Config -- -- Dispatchers : ");
		for(FileDispatcher fd : handler.getDispatchers()) {
			logger.info("Config -- -- Filter : ");
			for (String s : fd.getConfiguration()) {
				logger.info("Config -- -- -- "+s);
			}
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

//		System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "logback.xml");
		logger = LoggerFactory.getLogger("TransfertApp");

		// Force non-headless deterministically (don’t rely on timing/prints)
		System.setProperty("java.awt.headless", "false");

		FileSystemXmlApplicationContext appContext = new FileSystemXmlApplicationContext(new String[] { "./conf/spring-eptaf.xml", "./conf/configuration.xml" });


		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		context.getLogger(SpringApplication.class).setLevel(Level.valueOf("warn"));

		SpringApplication.run(TransfertApp.class, args);
		logConfiguration(appContext);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				MainFrame frame = new MainFrame();
				frame.setVisible(true);
			}
		});


	}

}
