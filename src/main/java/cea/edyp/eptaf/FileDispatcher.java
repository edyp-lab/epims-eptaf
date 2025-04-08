package cea.edyp.eptaf;

//import cea.edyp.communication.model.AcquisitionFileMessage;
import fr.edyp.epims.json.AcquisitionFileMessageJson;

public interface FileDispatcher {

	public boolean acceptMessage(AcquisitionFileMessageJson message);
	
	public Destination getDestination(AcquisitionFileMessageJson message);
	
	public String[] getConfiguration();
}
