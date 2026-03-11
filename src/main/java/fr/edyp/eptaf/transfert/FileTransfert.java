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
