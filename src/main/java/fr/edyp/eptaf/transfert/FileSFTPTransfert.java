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
package fr.edyp.eptaf.transfert;

import java.io.File;
import java.io.IOException;

import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import fr.edyp.eptaf.FTPConfiguration;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.xfer.FileSystemFile;
import net.schmizz.sshj.xfer.LocalDestFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSFTPTransfert implements IFileTransferer {

	private final FTPConfiguration config;
	private static final Logger logger = LoggerFactory.getLogger(FileSFTPTransfert.class);
		
	public FileSFTPTransfert(FTPConfiguration cfg) {
		config =  cfg;
	}


	public void startTransfert(String path, String fileName, File destination) {
    logger.info("SFTP -- File '{}' ({}) will be transferred to {}", fileName, path, destination.getAbsolutePath());
		SSHClient client = null;
		SFTPClient sftClient = null;
		try {
			client = new SSHClient();
			client.addHostKeyVerifier(new PromiscuousVerifier()); // Do not check who is behind the server.
			if(config.getPort() != null) {
				client.connect(config.getHost(), config.getPort());
			} else {
				client.connect(config.getHost());
			}
			if(config.getAuthMode().equals(FTPConfiguration.AuthMode.PASSWD_MODE)) {
				client.authPassword(config.getLogin(), config.getPassword());
			} else {
				String keyPath = config.getKeyPath();
				client.authPublickey(config.getLogin(), keyPath);
			}
			sftClient = client.newSFTPClient();
			logger.debug("SFTP -- Create SFTP Client ");
			String rootPath = config.getEpimsRoot();
			StringBuilder sb = new StringBuilder(rootPath);
			sb.append(path).append("/").append(fileName);
			LocalDestFile destFile = new FileSystemFile(destination);
      logger.debug("SFTP -- get: {}", sb);
			sftClient.get(sb.toString(), destFile);			
		} catch (IOException e) {
			e.printStackTrace();
		} finally  {
			if(sftClient != null){
				try {
					sftClient.close();
					logger.debug("SFTP -- SFTP Client closed");
				} catch (IOException e) {
					logger.error("Unable to close SFTP Client" ,e);
				}
			}
			if(client != null){
				try {
					client.close();
					logger.debug("SFTP -- SSH Client closed");
				} catch (IOException e) {
					logger.error("Unable to close SSH Client" ,e);
				}
			}
		}
				
	}
}
