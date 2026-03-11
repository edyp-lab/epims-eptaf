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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class Destination {

	private static final Logger logger = LoggerFactory.getLogger(Destination.class);
	
	private String path;
	private File file;

	public Destination() {
		
	}
	
	public Destination(String path) {
		setPath(path);
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		file = new File(path);
		if (file.exists() && !file.isDirectory() && !file.canWrite())
			throw new IllegalArgumentException(path+" is not a writable folder destination");
		if (!file.exists()) {
			// search if parent exists and is writable. If so, then create directory
			File parent = file.getParentFile();
			if ((parent.exists() && parent.isDirectory() && parent.canWrite())) {
				if (file.mkdir()) {
          logger.info("Destination directory {} created", file.getAbsolutePath());
				} else {
					throw new IllegalArgumentException(parent.getAbsolutePath()+"cannot create directory "+path);					
				}
			} else {
				throw new IllegalArgumentException(parent.getAbsolutePath()+"Parent "+parent.getAbsolutePath()+" is not a writable folder : cannot create "+path);					
			}
		}
	}
	
	public File getFile() {
		return file;
	}
}
