package com.baku.asyncstreampipeline.business.outbound.control;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import static junit.framework.Assert.assertEquals;
import org.bson.Document;

import org.json.JSONObject;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class StreamMergerTest {

    @Test
    public void shouldReturnNullWhenGivenNullMessagesList() {
        StreamMerger streamMerger = new StreamMerger();
        List<Document> unsentMessagesOrderedByTimestamp = null;

        List<String> mergedMessages = streamMerger.mergeMessagesIfEligible(unsentMessagesOrderedByTimestamp);

        Assert.assertNull(mergedMessages);
    }

    @Test
    public void shouldReturnNullWhenGivenEmptyMessagesList() {
        StreamMerger streamMerger = new StreamMerger();
        List<Document> unsentMessagesOrderedByTimestamp = new LinkedList<>();

        List<String> mergedMessages = streamMerger.mergeMessagesIfEligible(unsentMessagesOrderedByTimestamp);

        Assert.assertNull(mergedMessages);
    }

    @Test
    public void givenListWithOnlyUniqueTimestamps_shouldReturnListOfTheSameSize() {
        StreamMerger streamMerger = new StreamMerger();
        List<Document> unsentMessagesOrderedByTimestamp = new LinkedList<>();

        BasicDBObject document1 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.998\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document2 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.997\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document3 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.996\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document4 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.995\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document5 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.994\" \n"
                + "  }    \n"
                + "}");

        unsentMessagesOrderedByTimestamp.add(new Document(document1));
        unsentMessagesOrderedByTimestamp.add(new Document(document2));
        unsentMessagesOrderedByTimestamp.add(new Document(document3));
        unsentMessagesOrderedByTimestamp.add(new Document(document4));
        unsentMessagesOrderedByTimestamp.add(new Document(document5));

        List<String> mergedMessages = streamMerger.mergeMessagesIfEligible(unsentMessagesOrderedByTimestamp);

        Assert.assertTrue(unsentMessagesOrderedByTimestamp.size() == mergedMessages.size());
    }

    @Test
    public void givenListWithIdenticalTimestamps_shouldReturnListWithSingleEntry() {
        StreamMerger streamMerger = new StreamMerger();
        List<Document> unsentMessagesOrderedByTimestamp = new LinkedList<>();

        BasicDBObject document1 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.997\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document2 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.997\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document3 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.997\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document4 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.997\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document5 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.997\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document6 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.997\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document7 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.997\" \n"
                + "  }    \n"
                + "}");

        unsentMessagesOrderedByTimestamp.add(new Document(document1));
        unsentMessagesOrderedByTimestamp.add(new Document(document2));
        unsentMessagesOrderedByTimestamp.add(new Document(document3));
        unsentMessagesOrderedByTimestamp.add(new Document(document4));
        unsentMessagesOrderedByTimestamp.add(new Document(document5));
        unsentMessagesOrderedByTimestamp.add(new Document(document6));
        unsentMessagesOrderedByTimestamp.add(new Document(document7));

        List<String> mergedMessages = streamMerger.mergeMessagesIfEligible(unsentMessagesOrderedByTimestamp);

        assertTrue(mergedMessages.size() == 1);
    }

    @Test
    public void givenListWithMergeableValues_shouldMergeMessages() {
        StreamMerger streamMerger = new StreamMerger();
        List<Document> unsentMessagesOrderedByTimestamp = new LinkedList<>();

        BasicDBObject document1 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.997\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document2 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : 134.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.997\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document3 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.998\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document4 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.999\" \n"
                + "  }    \n"
                + "}");

        unsentMessagesOrderedByTimestamp.add(new Document(document1));
        unsentMessagesOrderedByTimestamp.add(new Document(document2));
        unsentMessagesOrderedByTimestamp.add(new Document(document3));
        unsentMessagesOrderedByTimestamp.add(new Document(document4));

        List<String> mergedMessages = streamMerger.mergeMessagesIfEligible(unsentMessagesOrderedByTimestamp);

        assertTrue(mergedMessages.size() == getNumberOfUniqueTimestamps(unsentMessagesOrderedByTimestamp));
    }

    @Test
    public void givenListWithMergeableValues_shouldSumAmounts() {
        StreamMerger streamMerger = new StreamMerger();
        List<Document> unsentMessagesOrderedByTimestamp = new LinkedList<>();

        BasicDBObject document1 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.997\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document2 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : 134.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.997\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document3 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.998\" \n"
                + "  }    \n"
                + "}");
        BasicDBObject document4 = (BasicDBObject) JSON.parse("{\n"
                + "  \"data\" : \n"
                + "  { \n"
                + "    \"amount\" : -34.859, \n"
                + "    \"source\" : \"0.0.0.0/0.0.0.0:15001\", \n"
                + "    \"timestamp\" : \"2018-01-02 09:54:35.999\" \n"
                + "  }    \n"
                + "}");

        unsentMessagesOrderedByTimestamp.add(new Document(document1));
        unsentMessagesOrderedByTimestamp.add(new Document(document2));
        unsentMessagesOrderedByTimestamp.add(new Document(document3));
        unsentMessagesOrderedByTimestamp.add(new Document(document4));

        List<String> mergedMessages = streamMerger.mergeMessagesIfEligible(unsentMessagesOrderedByTimestamp);
        JSONObject parsedMessage = new JSONObject(mergedMessages.get(0));
        assertEquals(100d, parsedMessage.getJSONObject("data").getDouble("amount"));
    }

    private int getNumberOfUniqueTimestamps(List<Document> mergeableMessages) {
        Set<String> timestamps = new HashSet<>();
        mergeableMessages.stream().map((mergeableMessage) -> (BasicDBObject) mergeableMessage.get("data")).forEach((data) -> {
            timestamps.add(data.getString("timestamp"));
        });

        return timestamps.size();
    }
}
