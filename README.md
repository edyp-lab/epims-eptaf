# epims-eptaf
Software that listens to a JMS file to find out about new acquisitions available on the server.  
According to filter rules, it retrieves new acquisitions from the repository via SFTP and copies them to the Process PCs.  

Previously hosted on CEA Tuleap Projects.

## Usage

Before running `start-epTAF.bat` you should modify following files

* eptaf.properties
  * jms.url_provider : define host & port of ePims server to get message from. Port should be 61617
  * ftp.xxx : information to connect to the FTP server  
 
* configuration.xml : defines rules to apply to filter server messages and download only expected acquisitions. 
  * Description of allowed properties that may be used will be provided. [Previous doc could be found here...](http://biodev.extra.cea.fr/docs/epims/doku.php?id=wiki:epims4_1:admin:configurationepims#fichier_eptafpropertiesconfigurer_de_l_environnement_pour_ep-taf)   

## Revisions

### version 2.0.0

* Use graphic frame for log information
* Use new SpringBoot ePims version

