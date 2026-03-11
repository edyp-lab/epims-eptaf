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
package fr.edyp.eptaf.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.xfer.FileSystemFile;
import net.schmizz.sshj.xfer.LocalDestFile;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSHJTest {
	
	String host = "edyp";
	String user = "pims";
	String password = "pims";
	private static Logger logger = LoggerFactory.getLogger(SSHJTest.class);

	SSHClient client;
	SFTPClient sftClient;
	FTPClient ftpClient;
	boolean testSSHMode;
	public SSHJTest(boolean sshMode) {
		testSSHMode = sshMode;
		if(testSSHMode) {
			client = new SSHClient();
			try {
				client.loadKnownHosts();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			ftpClient = new FTPClient();
		}
	}
	
	public void setHost(String newHost){
		host = newHost;
	}
	
	public void connect(){
		try {
			if(testSSHMode) {
				client.connect(host);
				client.authPassword(user, password);
				sftClient = client.newSFTPClient();
				logger.debug("SFTP -- Create SFTP Client ");
			} else {
				ftpClient.connect(host);
				ftpClient.enterLocalPassiveMode();
				ftpClient.login(user, password);
				logger.debug("FTP -- Connected to server : "+ftpClient.getReplyString());
				int reply = ftpClient.getReplyCode();
				if(!FTPReply.isPositiveCompletion(reply)) {
					ftpClient.disconnect();
					logger.error("FTP -- server refused connection.");
					return;
				}
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void download(String path, String file, String localPath) {
		try {
			
			StringBuilder sb = new StringBuilder("epims_repo/");
			sb.append(path).append("/").append(file);
			if(testSSHMode) {
				LocalDestFile destFile = new FileSystemFile(localPath);
				sftClient.get(sb.toString(), destFile);
			} else {
				ftpClient.changeWorkingDirectory(path);
				FTPFile[] files = ftpClient.listFiles();
				boolean success = false;
				for (FTPFile f : files) {
					if (f.getName().equals(file)) {
						File localFile = new File(localPath, file);
						FileOutputStream fos = new FileOutputStream(localFile);
						ftpClient.retrieveFile(file, fos);
						fos.close();
						logger.debug("FTP -- "+file+" into local file "+localFile);
						break;
					}
				}
				ftpClient.logout();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
	
	public static void main(String[] args){
		SSHJTest test =  new SSHJTest(true);
		test.connect();
		test.download("<PATH>", "toto.txt", "C:/temp/Vero");
	}
}
