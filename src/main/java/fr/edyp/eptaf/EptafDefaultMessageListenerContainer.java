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
