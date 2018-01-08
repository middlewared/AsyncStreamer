package com.baku.asyncstreampipeline.business.outbound.control;

import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.inject.Inject;

@Singleton
@TransactionAttribute(REQUIRES_NEW)
public class OutgoingStreamScheduler {
    
    @Inject
    OutputOrchestrator outputOrchestrator;
    
    private static final Logger LOG = Logger.getLogger(OutgoingStreamScheduler.class.getName());
    
    @Schedule(hour = "*", minute = "*", second = "*/10")
    public void triggerOutput() {
        LOG.severe("OutgoingStreamScheduler executed");
        outputOrchestrator.prepareMessagesForStreamingOut();
    }
}
