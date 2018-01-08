package com.baku.asyncstreampipeline.business.outbound.control;

import com.baku.asyncstreampipeline.business.control.MessagesRepository;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.bson.Document;

@Stateless
public class OutputOrchestrator {

    @Inject
    MessagesRepository messagesRepository;

    @Inject
    StreamMerger streamMerger;

    @Inject
    StreamExposer streamExposer;

    public void prepareMessagesForStreamingOut() {
        List<Document> unsentMessagesOrderedByTimestamp = messagesRepository.getUnsentMessagesOrderedByTimestamp();
        List<String> mergedMessages = streamMerger.mergeMessagesIfEligible(unsentMessagesOrderedByTimestamp);
        streamExposer.putMessageOnOutgoingQueue(mergedMessages);
    }
}
