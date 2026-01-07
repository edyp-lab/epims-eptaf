package fr.edyp.eptaf.transfert;

import java.io.File;
import java.util.Objects;

import fr.edyp.eptaf.FTPConfiguration;

public class FileTransfert {

	private final IFileTransferer transferer;

	public FileTransfert(FTPConfiguration cfg) {
    if (Objects.requireNonNull(cfg.getMode()) == FTPConfiguration.TransfertMode.FTP_MODE) {
      transferer = new FileFTPTransfert(cfg);

      //SFTP_MODE & DEFAULT
    } else {
      transferer = new FileSFTPTransfert(cfg);
    }
	}

	public void startTransfert(String path, String fileName, File destination) {
		transferer.startTransfert(path, fileName, destination);
	}

}
