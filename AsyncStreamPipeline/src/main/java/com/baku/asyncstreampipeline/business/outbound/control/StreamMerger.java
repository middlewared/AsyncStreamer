package com.baku.asyncstreampipeline.business.outbound.control;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import org.bson.Document;

@Stateless
public class StreamMerger {

    public List<String> mergeMessagesIfEligible(List<Document> unsentMessagesOrderedByTimestamp) {
        if (unsentMessagesOrderedByTimestamp == null || unsentMessagesOrderedByTimestamp.isEmpty()) {
            return null;
        }

        List<String> mergedMessagesFromDb = merge(unsentMessagesOrderedByTimestamp);
        return mergedMessagesFromDb;
    }

    private List<String> merge(List<Document> messagesFromDb) {
        List<String> mergedMessages = new LinkedList<>();
        Document messageBeingMerged = null;

        for (int mergedMessagesListIndex = 0; mergedMessagesListIndex < messagesFromDb.size(); mergedMessagesListIndex++) {
            Document currentMessage = messagesFromDb.get(mergedMessagesListIndex);

            if (mergedMessagesListIndex == messagesFromDb.size() - 1) {
                if (messageBeingMerged != null) {
                    mergedMessages.add(messageBeingMerged.toJson());
                    break;
                } else {
                    mergedMessages.add(currentMessage.toJson());
                    break;
                }
            }

            Document nextMessage = messagesFromDb.get(mergedMessagesListIndex + 1);

            String currentTimestamp = extractTimestamp(currentMessage);
            String nextTimestamp = extractTimestamp(nextMessage);

            if (!currentTimestamp.equals(nextTimestamp)) {
                if (messageBeingMerged != null) {
                    mergedMessages.add(messageBeingMerged.toJson());
                    messageBeingMerged = null;
                    continue;
                }
                mergedMessages.add(currentMessage.toJson());
                continue;
            }

            if (currentTimestamp.equals(nextTimestamp)) {
                if (messageBeingMerged != null) {
                    setNewMergedAmount(messageBeingMerged, nextMessage);
                    continue;
                }

                setNewMergedAmount(currentMessage, nextMessage);
                messageBeingMerged = currentMessage;
            }
        }
        return mergedMessages;
    }

    private String extractTimestamp(Document messageFromDb) {
        DBObject dbObject = (DBObject) JSON.parse(messageFromDb.toJson());
        DBObject data = (DBObject) dbObject.get("data");
        return data.get("timestamp").toString();
    }

    private void setNewMergedAmount(Document currentMessageFromDb, Document nextMessageFromDb) {
        BasicDBObject data = (BasicDBObject) currentMessageFromDb.get("data");
        data.put("amount", calculateNewAmount(currentMessageFromDb, nextMessageFromDb));
    }

    private double calculateNewAmount(Document currentMessageFromDb, Document nextMessageFromDb) {
        double currentMessageAmount = getAmount(currentMessageFromDb);
        double nextMessageAmount = getAmount(nextMessageFromDb);
        double sum = currentMessageAmount + nextMessageAmount;
        return sum;
    }

    private double getAmount(Document currentMessageFromDb) {
        BasicDBObject data = (BasicDBObject) currentMessageFromDb.get("data");
        return data.getDouble("amount");
    }
}
