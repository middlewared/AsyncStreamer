package com.baku.asyncstreampipeline.business.control;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import com.mongodb.MongoClient;

@Singleton
public class DbClientProvider {

    private MongoClient mongoClient = null;

    @Lock(LockType.READ)
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    @PostConstruct
    public void init() {
        String mongoIpAddress = "asyncstreamer_db_1";
        Integer mongoPort = 27017;
        mongoClient = new MongoClient(mongoIpAddress, mongoPort);
    }
}
