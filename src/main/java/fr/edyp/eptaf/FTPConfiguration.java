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

public class FTPConfiguration {

	private String host;
	private Integer port;
	private String login;
	private String password;
	private String keyPath;
	private AuthMode authMode;
	private TransfertMode mode;
	private String epimsRoot;

	public enum TransfertMode { FTP_MODE, SFTP_MODE };
	public enum AuthMode { KEY_MODE, PASSWD_MODE };

	public String getHost() {
		return host;
	}
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getKeyPath() {
		return keyPath;
	}

	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}

	public AuthMode getAuthMode() {
		if(authMode==null)
			authMode = AuthMode.PASSWD_MODE;
		return authMode;
	}

	public void setAuthMode(AuthMode authMode) {
		this.authMode = authMode;
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

	public String getEpimsRoot() {
		return epimsRoot;
	}

	public void setEpimsRoot(String epimsRoot) {
		this.epimsRoot = epimsRoot;
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
