package com.baku.asyncstreampipeline.business.control;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.bson.Document;
import org.json.JSONObject;
import org.json.XML;

@Stateless
public class MessagesRepository {

    @Inject
    DbClientProvider dbClientProvider;

    public void transformAndWrite(String xmlMessage) {
        JSONObject xmlAsJson = XML.toJSONObject(xmlMessage);
        writeMessageToDb(xmlAsJson);
    }

    private void writeMessageToDb(JSONObject xmlJSONObj) {
        MongoDatabase db = dbClientProvider.getMongoClient().getDatabase("persistent_queue");
        MongoCollection<Document> mongoCollection = db.getCollection("messages");
        mongoCollection.insertOne(Document.parse(xmlJSONObj.toString()));
    }

    public List<Document> getUnsentMessagesOrderedByTimestamp() {
        MongoDatabase db = dbClientProvider.getMongoClient().getDatabase("persistent_queue");
        MongoCollection<Document> mongoCollection = db.getCollection("messages");
        FindIterable<Document> sortedMongoCollection = mongoCollection.find().sort(new BasicDBObject("timestamp", 1));
        MongoCursor<Document> iterator = sortedMongoCollection.iterator();

        List<Document> sortedMessages = new ArrayList<>();
        while (iterator.hasNext()) {
            sortedMessages.add(iterator.next());
        }

        return sortedMessages;
    }
}
