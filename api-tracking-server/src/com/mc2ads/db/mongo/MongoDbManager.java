package com.mc2ads.db.mongo;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public interface MongoDbManager {

	public DBCollection getDBCollection(String name);
	public boolean insert(DBObject dbObject, String collectionName);
	public boolean update(DBObject query, DBObject dbObject, String collectionName);
	public boolean remove(DBObject dbObject, String collectionName);	
	public DBCursor find(DBObject dbObject, String collectionName);
	public DBCursor find(DBObject dbObject, String collectionName, int start, int limit);
	public void start();

}