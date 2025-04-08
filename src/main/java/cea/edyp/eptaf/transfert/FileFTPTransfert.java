package cea.edyp.eptaf.transfert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import cea.edyp.eptaf.FTPConfiguration;

public class FileFTPTransfert implements IFileTransferer {
	
	private FTPConfiguration config;
	private static Log logger = LogFactory.getLog(FileFTPTransfert.class);
	
	
	public FileFTPTransfert(FTPConfiguration cfg) {
		config =  cfg;
	}
	

	public void startTransfert(String path, String fileName, File destination) {
		logger.info("FTP -- File '"+fileName+"' ("+path.toString()+") will be transfered to "+destination.getAbsolutePath());
		FTPClient ftp = new FTPClient();
	    try {
	      int reply;
	      ftp.connect(config.getHost());
	      ftp.enterLocalPassiveMode();
	      ftp.login(config.getLogin(), config.getPassword());
	      logger.debug("FTP -- Connected to server : "+ftp.getReplyString());
	      // After connection attempt, you should check the reply code to verify success.
	      reply = ftp.getReplyCode();
	      if(!FTPReply.isPositiveCompletion(reply)) {
	        ftp.disconnect();
	        logger.error("FTP -- server refused connection.");
	        return;
	      }
	      ftp.setFileType(FTP.BINARY_FILE_TYPE);
	      long start = System.currentTimeMillis();
	      ftp.changeWorkingDirectory(path);
	      logger.debug("FTP --  current directory : "+ftp.printWorkingDirectory());
	      FTPFile[] files = ftp.listFiles();
	      boolean success = false;
	      for (FTPFile f : files) {
	    	  if (f.getName().equals(fileName)) {
	    		  success = retrieveFile(destination, f, ftp);
	    		  break;
	    	  }
	      }    
	      if (success) {
	      	
	    	  long stop = System.currentTimeMillis();
	    	  logger.debug("FTP -- transfer duration = "+((stop-start)/1000.0)+" s.");
	    	  
	    	  //test if Zip file and unzip if it's the case.
	        boolean isZip = false;
	        int dotIndex = fileName.lastIndexOf('.');
	        if(dotIndex != -1){
	        	String extension = fileName.substring(dotIndex+1);
	        	if (extension != null && extension.equalsIgnoreCase("zip"))
	        		isZip = true;
	        }

	    	  File retrievedFile = new File(destination,fileName);
	    	  if(isZip && retrievedFile.exists() && !retrievedFile.isDirectory()){
	    	  	 logger.info("FTP -- unzip file  "+retrievedFile.getAbsolutePath());
	    	  	 unzipFile(retrievedFile);
	    	  }
	    	  
	      } else {
	      	logger.error("FTP -- File '"+fileName+"' ("+path.toString()+") not found.");
	      }
	      ftp.logout();
	    } catch(IOException e) {
	      logger.error(e);
	    } finally {
	      if(ftp.isConnected()) {
	        try {
	          ftp.disconnect();
	        } catch(IOException ioe) {
	          logger.error(ioe);
	        }
	      }
	    }
	}
	

	protected void unzipFile(File zipFile) throws IOException{
		byte[] buffer = new byte[1024];		 
		File parentDir = zipFile.getParentFile();
		ZipInputStream zis = null;
		try{
 
			//get the zip file content
    		zis = new ZipInputStream(new FileInputStream(zipFile));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();
 
    	while(ze!=null){
 
    		String fileName = ze.getName();
    		File newFile = new File(parentDir, fileName);
    		logger.debug("file unzip : "+ newFile.getAbsoluteFile());
 
    		FileOutputStream fos = new FileOutputStream(newFile);             
 
    		int len;
    		while ((len = zis.read(buffer)) > 0) {
    			fos.write(buffer, 0, len);
    		}
 
    		fos.close();   
    		ze = zis.getNextEntry();
    	}    	   
 
    	zis.closeEntry();
    	zis.close();
    	
    	//Try to delete zip file.
    	zipFile.delete();
    	
    } catch(ZipException ze){
    	if(zis != null ){
    		try {
        	zis.closeEntry();
    		} catch(IOException ex){
    			logger.error(ex);
    		}
    		try {
    			zis.close();
    		} catch(IOException ex){
    			logger.error(ex);
    		}
    	}
    	throw ze;
    } catch(IOException ex){
    	if(zis != null ){
    		try {
        	zis.closeEntry();
    		} catch(IOException ex1){
    			logger.error(ex);
    		}
    		try {
    			zis.close();
    		} catch(IOException ex1){
    			logger.error(ex);
    		}
    	}
    	throw ex;
    }
	}
	
	protected boolean retrieveFile(File localPath, FTPFile file, FTPClient ftp) throws IOException {
		if (file.getType() == FTPFile.DIRECTORY_TYPE) {
			boolean success = true; 
			// create local directory
			File dir = new File(localPath, file.getName());
			dir.mkdir();
			// change working directory
			ftp.changeWorkingDirectory(file.getName());
			// list and transfer directory content
			FTPFile[] files = ftp.listFiles();
		    for (FTPFile f : files) {
		    	if (! (f.getName().equals(".") || f.getName().equals(".."))) {
		    		success = success && retrieveFile(dir, f, ftp);	    		  
		    	}
		    }
		    ftp.changeToParentDirectory();
		    return success;
		} else {
			File localFile = new File(localPath, file.getName());
			FileOutputStream fos = new FileOutputStream(localFile);
	    	ftp.retrieveFile(file.getName(), fos);	    		  
	    	fos.close();
	    	logger.debug("FTP -- "+file.getName()+" into local file "+localFile);
	    	return true;
		}		
	}
	
}
