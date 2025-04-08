package cea.edyp.eptaf;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import fr.edyp.epims.json.AcquisitionFileMessageJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import cea.edyp.communication.model.AcquisitionFileMessage;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


import com.fasterxml.jackson.databind.ObjectMapper;

public class TopicListener implements MessageListener {

	private Log logger = LogFactory.getLog(TopicListener.class);
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
