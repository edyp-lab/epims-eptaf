package cea.edyp.eptaf;

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