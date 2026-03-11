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

import java.util.ArrayList;
import java.util.List;

import fr.edyp.epims.json.AcquisitionFileMessageJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.edyp.eptaf.transfert.FileTransfert;

public class MessageHandler {

	private static MessageHandler instance;
	private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

	private FTPConfiguration ftpConfig;
	private FileTransfert transfer;
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
		this.transfer = new FileTransfert(ftpConfig);
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
			String acqFileName = message.getAcquisitionFileDescriptor().getFileName();
			String acqPath = message.getAcquisitionFileDescriptor().getPath();

			List<Destination> destinations = lookupDestinations(message);
			for (Destination d : destinations) {
				transfer.startTransfert(acqPath, acqFileName, d.getFile());
			}
		} else {
			logger.info("message rejected");
		}
	}

	private List<Destination> lookupDestinations(AcquisitionFileMessageJson message) {
		List<Destination> result = new ArrayList<>();
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
