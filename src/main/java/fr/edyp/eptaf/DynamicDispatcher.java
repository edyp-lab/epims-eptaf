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

import java.io.File;

import fr.edyp.epims.json.AcquisitionFileMessageJson;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


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
