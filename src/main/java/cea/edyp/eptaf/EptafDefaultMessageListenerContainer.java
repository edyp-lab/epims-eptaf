package cea.edyp.eptaf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

public class EptafDefaultMessageListenerContainer extends DefaultMessageListenerContainer {

    private Logger logger = LoggerFactory.getLogger("TransfertApp");


    protected void refreshConnectionUntilSuccessful() {
        super.refreshConnectionUntilSuccessful();
        logger.info("Refreshed JMS Connection");
    }


}
