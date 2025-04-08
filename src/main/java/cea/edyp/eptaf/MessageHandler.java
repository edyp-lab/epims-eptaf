package cea.edyp.eptaf;

import java.util.ArrayList;
import java.util.List;

import fr.edyp.epims.json.AcquisitionFileMessageJson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

//import cea.edyp.communication.model.AcquisitionFileMessage;
import cea.edyp.eptaf.transfert.FileTransfert;

public class MessageHandler {

	private static MessageHandler instance;
	private static Log logger = LogFactory.getLog(MessageHandler.class);

	private FTPConfiguration ftpConfig;
	private FileTransfert transfert;
	private Destination defaultDestination;
	private MessageFilter messageFilter;
	private List<FileDispatcher> dispatchers;

	public static MessageHandler getInstance() {
		if (instance == null) {
			instance = new MessageHandler();
		}
		return instance;
	}

	public FTPConfiguration getFtpConfig() {
		return ftpConfig;
	}

	public void setFtpConfig(FTPConfiguration ftpConfig) {
		this.ftpConfig = ftpConfig;
		this.transfert = new FileTransfert(ftpConfig);
	}

	public Destination getDefaultDestination() {
		return defaultDestination;
	}

	public void setDefaultDestination(Destination defaultDestination) {
		this.defaultDestination = defaultDestination;
	}

	public MessageFilter getMessageFilter() {
		return messageFilter;
	}

	public void setMessageFilter(MessageFilter messageFilter) {
		this.messageFilter = messageFilter;
	}

	public void processMessage(AcquisitionFileMessageJson message) {
		if (messageFilter.acceptMessage(message)) {
			BeanWrapper bw = new BeanWrapperImpl(message);
			String acqFileName = message.getAcquisitionFileDescriptor().getFileName();
			String acqPath = message.getAcquisitionFileDescriptor().getPath(); //JPM.TODO null ??? (String) bw.getPropertyValue("acquisitionFileDescriptor.path");

			List<Destination> destinations = lookupDestinations(message);
			for (Destination d : destinations) {
				transfert.startTransfert(acqPath.toString(), acqFileName, d.getFile());
			}
		} else {
			logger.info("message rejected");
		}
	}

	private List<Destination> lookupDestinations(AcquisitionFileMessageJson message) {
		List<Destination> result = new ArrayList<Destination>();
		if (dispatchers != null) {
			for (FileDispatcher d : dispatchers) {
				if (d.acceptMessage(message)) {
					result.add(d.getDestination(message));
				}
			}
		}
		if (result.isEmpty()) {
			result.add(getDefaultDestination());
		}
		return result;
	}

	public List<FileDispatcher> getDispatchers() {
		return dispatchers;
	}

	public void setDispatchers(List<FileDispatcher> dispatchers) {
		this.dispatchers = dispatchers;
	}
}
