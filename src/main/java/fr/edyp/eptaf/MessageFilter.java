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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.edyp.epims.json.AcquisitionFileMessageJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


public class MessageFilter {

	private static Logger logger = LoggerFactory.getLogger(MessageFilter.class);
	
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
