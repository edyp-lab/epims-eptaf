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

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class LogAppender  extends AppenderBase<ILoggingEvent> {

    private PatternLayout m_patternLayout;

    @Override
    public void start() {
        m_patternLayout = new PatternLayout();
        m_patternLayout.setContext(getContext());
        m_patternLayout.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        m_patternLayout.start();

        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        // format message and log it
        String formattedMsg = m_patternLayout.doLayout(event);

        MainFrame.append(formattedMsg);

    }

}