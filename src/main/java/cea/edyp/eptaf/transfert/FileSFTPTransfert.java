package cea.edyp.eptaf.transfert;

import java.io.File;
import java.io.IOException;

import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cea.edyp.eptaf.FTPConfiguration;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.xfer.FileSystemFile;
import net.schmizz.sshj.xfer.LocalDestFile;

public class FileSFTPTransfert implements IFileTransferer {

	private FTPConfiguration config;
	private static Log logger = LogFactory.getLog(FileSFTPTransfert.class);
		
	public FileSFTPTransfert(FTPConfiguration cfg) {
		config =  cfg;
	}


	public void startTransfert(String path, String fileName, File destination) {
		logger.info("SFTP -- File '"+fileName+"' ("+path.toString()+") will be transfered to "+destination.getAbsolutePath());
		SSHClient client = null;
		SFTPClient sftClient = null;
		try {
			client = new SSHClient();
			client.addHostKeyVerifier(new PromiscuousVerifier()); // Do not check who is behind server.
			client.connect(config.getHost());
			client.authPassword(config.getLogin(), config.getPassword());
			sftClient = client.newSFTPClient();
			logger.debug("SFTP -- Create SFTP Client ");
			StringBuilder sb = new StringBuilder("epims_repo/repository/"); //VDS TODO: REPLACE by config param : pims_root...
			sb.append(path).append("/").append(fileName);
			LocalDestFile destFile = new FileSystemFile(destination);
			logger.debug("SFTP -- get: "+sb.toString());
			sftClient.get(sb.toString(), destFile);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
