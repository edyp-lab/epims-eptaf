package cea.edyp.eptaf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class Destination {

	private static Logger logger = LoggerFactory.getLogger(Destination.class);
	
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
					logger.info("Destination directory "+file.getAbsolutePath()+" created");
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
