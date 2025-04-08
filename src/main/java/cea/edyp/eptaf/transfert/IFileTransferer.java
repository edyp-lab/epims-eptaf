package cea.edyp.eptaf.transfert;

import java.io.File;

public interface IFileTransferer {
	
	public void startTransfert(String path, String fileName, File destination);
}
