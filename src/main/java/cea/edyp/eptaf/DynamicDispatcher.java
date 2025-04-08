package cea.edyp.eptaf;

import java.io.File;

import fr.edyp.epims.json.AcquisitionFileMessageJson;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

//import cea.edyp.communication.model.AcquisitionFileMessage;

public class DynamicDispatcher extends FilterDispatcher {
	
	private String folderName;
	
	@Override
	public Destination getDestination(AcquisitionFileMessageJson message) {
		BeanWrapper bw = new BeanWrapperImpl(message);
		File path = new File(destination.getPath());
		if(bw.isReadableProperty(folderName)){
			String value = (String)bw.getPropertyValue(folderName);
			path = new File(destination.getPath(), value);
		}
		return new Destination(path.getAbsolutePath());
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

}
