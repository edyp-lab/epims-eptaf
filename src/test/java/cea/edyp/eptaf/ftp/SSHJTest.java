package cea.edyp.eptaf.ftp;

import java.io.IOException;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.xfer.FileSystemFile;
import net.schmizz.sshj.xfer.LocalDestFile;

public class SSHJTest {
	
	String host = "epims";
	String user = "pims";
	String password = "pims";
	
	SSHClient client;
	SFTPClient sftClient;
	public SSHJTest() {
		client = new SSHClient();
		try {
			client.loadKnownHosts();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setHost(String newHost){
		host = newHost;
	}
	
	public void connect(){
		try {
			client.connect(host);
			client.authPassword(user, password);
			sftClient = client.newSFTPClient();          
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void download(String path, String file, String localPath) {
		try {
			
			StringBuilder sb = new StringBuilder("epims_repo/");
			sb.append(path).append("/").append(file);
			LocalDestFile destFile = new FileSystemFile(localPath);
			sftClient.get(sb.toString(), destFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	
	public static void main(String[] args){
		SSHJTest test =  new SSHJTest();
		test.connect();
		test.download("<PATH>", "toto.txt", "C:/temp/Vero");
	}
}
