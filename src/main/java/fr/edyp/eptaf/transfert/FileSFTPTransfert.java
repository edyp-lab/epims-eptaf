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
			if(config.getAuthMode().equals(FTPConfiguration.AuthMode.KEY_MODE)) {
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
