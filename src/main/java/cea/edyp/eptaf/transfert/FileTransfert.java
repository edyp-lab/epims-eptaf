package cea.edyp.eptaf.transfert;

import java.io.File;

import cea.edyp.eptaf.FTPConfiguration;

public class FileTransfert {

	private IFileTransferer transferer;

	public FileTransfert(FTPConfiguration cfg) {
		switch (cfg.getMode()) {
		case FTP_MODE:
			transferer = new FileFTPTransfert(cfg);
			break;
		case SFTP_MODE:
			transferer = new FileSFTPTransfert(cfg);
			break;

		default:
			transferer = new FileFTPTransfert(cfg);
			break;
		}
	}

	public void startTransfert(String path, String fileName, File destination) {
		transferer.startTransfert(path, fileName, destination);
	}

}
