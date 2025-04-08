package cea.edyp.eptaf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.edyp.epims.json.AcquisitionFileMessageJson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

//import cea.edyp.communication.model.AcquisitionFileMessage;

public class MessageFilter {

	private static Log logger = LogFactory.getLog(MessageFilter.class);
	
	private Map<String, String> properties;
	
	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public boolean acceptMessage(AcquisitionFileMessageJson message) {
		BeanWrapper bw = new BeanWrapperImpl(message);
		boolean accepted = true;

		if (! bw.isReadableProperty("acquisitionFileDescriptor")) {
			System.out.println("KO1");
		}
		if (! bw.isReadableProperty("acquisitionFileDescriptor.acquisition")) {
			System.out.println("KO2");
		}
		if (! bw.isReadableProperty("acquisitionFileDescriptor.acquisition.acquisitionJson")) {
			System.out.println("KO3");
		}
		if (! bw.isReadableProperty("acquisitionFileDescriptor.acquisition.acquisitionJson.instrumentName")) {
			System.out.println("KO4");
		}

		for (String property : properties.keySet()) {
			if(!bw.isReadableProperty(property)){
				String endMsg = "";
				if(message.getAcquisitionFileDescriptor() != null )
					endMsg = "for "+message.getAcquisitionFileDescriptor().getFileName();
				logger.info(property+" is not reachable in current message "+endMsg);
				accepted = false;
				break;
			}
			Object propValue = bw.getPropertyValue(property);
			String value = (propValue!=null ? propValue.toString() : "");
			Pattern pattern = Pattern.compile(properties.get(property));
			Matcher matcher = pattern.matcher(value);
			accepted = accepted && matcher.find();
			logger.info(property+" = "+value+" test matching to : "+pattern.pattern());
		}
		return accepted;
	}
	
	public String[] getConfiguration() {
		List<String> result = new ArrayList<String>();
		for (Map.Entry<String,String> e : getProperties().entrySet()) {			
			 result.add("property "+e.getKey()+" must match '"+e.getValue()+"'");
		}
		return result.toArray(new String[result.size()]);
	}
	
}
