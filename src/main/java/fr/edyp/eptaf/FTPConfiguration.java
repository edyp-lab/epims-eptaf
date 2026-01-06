package fr.edyp.eptaf;

public class FTPConfiguration {

	private String host;
	private Integer port;
	private String login;
	private String password;
	private TransfertMode mode;
	
	public enum TransfertMode { FTP_MODE, SFTP_MODE };
			
	public String getHost() {
		return host;
	}
	public Integer getPort() {
		return port;
	}

	public void setHost(String host) {
		if(host!=null && host.contains(":")){
			 String[] values = host.split(":");
			 this.host =values[0];
			 try {
				 this.port = Integer.parseInt(values[1]);
			 } catch (NumberFormatException ignore) {
			 }
		} else
			this.host = host;
	}

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public TransfertMode getMode() {
		if(mode==null)
			mode = TransfertMode.FTP_MODE;
		return mode;
	}
	public void setMode(TransfertMode mode) {
		this.mode = mode;
	}
		
}
