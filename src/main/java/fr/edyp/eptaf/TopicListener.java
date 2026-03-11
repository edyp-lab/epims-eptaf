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

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

import fr.edyp.epims.json.AcquisitionFileMessageJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopicListener implements MessageListener {

	private Logger logger = LoggerFactory.getLogger(TopicListener.class);
	private XStream xstream;
	
	public TopicListener() {
		xstream = new XStream(new DomDriver());
	}
	
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {

			try {
				logger.debug(" Process Message --- START ---------------- ");
				logger.debug(((TextMessage) message).getText());
				logger.debug(" Process Message --- END ------------------ ");

				TextMessage textMessage = (TextMessage) message;

				String json = textMessage.getText();

				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				AcquisitionFileMessageJson acquisitionFileMessageJson = mapper.readValue(json, AcquisitionFileMessageJson.class);


				//AcquisitionFileMessage acqMessage = (AcquisitionFileMessage)xstream.fromXML(((TextMessage) message).getText());
				MessageHandler handler = MessageHandler.getInstance();
				if (handler != null) {
					handler.processMessage(acquisitionFileMessageJson);
				} else {
					logger.error("No handler to process this message");
				}
			} catch (JMSException | JsonProcessingException ex) {
				throw new RuntimeException(ex);
			} 
		} else {
			throw new IllegalArgumentException("Message must be of type TextMessage");
		}
	}
}
