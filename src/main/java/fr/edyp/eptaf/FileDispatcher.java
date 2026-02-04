package fr.edyp.eptaf;

import fr.edyp.epims.json.AcquisitionFileMessageJson;

public interface FileDispatcher {

	boolean acceptMessage(AcquisitionFileMessageJson message);
	
	Destination getDestination(AcquisitionFileMessageJson message);
	
	String[] getConfiguration();
}
