package cea.edyp.eptaf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import cea.edyp.communication.model.AcquisitionFileMessage;
import fr.edyp.epims.json.AcquisitionFileMessageJson;

public class FilterDispatcher extends MessageFilter implements FileDispatcher {
	
	protected Destination destination;
	
	public String getDestinationPath() {
		return destination.getPath();
	}
	public void setDestinationPath(String path) {
		destination = new Destination();
		destination.setPath(path);
	}

	public Destination getDestination(AcquisitionFileMessageJson message) {
		return destination;
	}
	
	@Override
	public String[] getConfiguration() {
		String[] filters = super.getConfiguration();
		List<String> config = new ArrayList<String>();
		config.add("destination path = "+getDestinationPath());
		config.addAll(Arrays.asList(filters));
		return config.toArray(new String[config.size()]);
	}

	
}
