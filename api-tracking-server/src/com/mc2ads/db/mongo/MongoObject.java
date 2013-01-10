package com.mc2ads.db.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public abstract class MongoObject {
	
	public MongoObject() {
		// TODO Auto-generated constructor stub
	}
	
	public MongoObject(DBObject obj) {
		
	}
	
	public abstract BasicDBObject toMongoObject();
}
