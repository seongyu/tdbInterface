package com.leon.tfinterface;

import java.sql.Timestamp;
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * 
 *
 */
public class PutToMongo 
{
	String MONGO_HOST = "13.124.143.112";
	Integer MONGO_PORT = 27017;
	
	static String DB = "IOT";
	
	static String user = "admin";
	static String password = "data";
	
	MongoClient mClient;
	MongoDatabase mdb;
	
	ServerAddress ss = new ServerAddress(MONGO_HOST, MONGO_PORT);
	
    public MongoCollection<Document> createPool(String colname) {
    	MongoCollection<Document> collection = null;
    	
    	MongoCredential cred = MongoCredential.createCredential(user, DB, password.toCharArray());
    	mClient = new MongoClient(ss, Arrays.asList(cred));
    	mdb = mClient.getDatabase("IOT");
    		try {
    			collection = mdb.getCollection(colname);
    		}catch(Exception err) {
    			err.printStackTrace();
    			mClient = new MongoClient(MONGO_HOST,MONGO_PORT);
    			mdb = mClient.getDatabase("IOT");
    		}finally {
    			collection = mdb.getCollection(colname);
    		}
    		
    		return collection;
    }
    
    public void closePool(MongoClient mClient) {
    		mClient.close();
    }
    
    public void input(MongoCollection<Document> collection) {
    		Document doc = new Document("name","Test")
        		.append("type", "TestInsert")
        		.append("Count", 1)
        		.append("timestamp",new Timestamp(System.currentTimeMillis()).getTime());
        
        collection.insertOne(doc);
        System.out.println("Successfully inserted");
    }
    
    public void get(MongoCollection<Document> collection, Document doc) {
    		FindIterable<Document> item = collection.find(doc).limit(3);
    		
    		for(Document it:item) {
    			System.out.println(it.toJson());
    		}
    }
}
