package com.mc2ads.db.mongo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.mc2ads.db.SqlDbConfigs;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

public class MongoDbManagerImpl implements MongoDbManager {
	private DB dbInstance;
	private static MongoDbManager instance;
	
	
	public static MongoDbManager startInstance(String mongodbBinDir, String dbPathDir) throws Exception {
		if(instance == null){
			
		}
		return instance;
	}

	public static MongoDbManager getInstance(String host, String dbName) throws Exception {
		if(instance == null){
			instance = new MongoDbManagerImpl(host, dbName);
		}
		return instance;
	}
	
	public static MongoDbManager getInstance(String configName) throws Exception {
		if(instance == null){
			try {
				SqlDbConfigs configs = SqlDbConfigs.loadFromFile(configName);
				instance = new MongoDbManagerImpl(configs.getHost(), configs.getDatabase());
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public static MongoDbManager getInstanceWithConfigPath(String filePath, String configName) throws Exception {
		if(instance == null){
			try {
				SqlDbConfigs configs = SqlDbConfigs.loadFromFile(filePath, configName);
				instance = new MongoDbManagerImpl(configs.getHost(), configs.getDatabase());
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	protected MongoDbManagerImpl() {}
	

	
	public MongoDbManagerImpl(String host, String dbName) throws Exception{
		Mongo m = new Mongo();
		m.setWriteConcern(WriteConcern.SAFE);
		this.dbInstance = m.connect(new DBAddress(host, dbName));			
	}
	
	/* (non-Javadoc)
	 * @see com.fosp.test.mongo.MongoDbManager#getDBCollection(java.lang.String)
	 */
	@Override
	public DBCollection getDBCollection(String name){		
		return this.dbInstance.getCollection(name);		
	}
	
	/* (non-Javadoc)
	 * @see com.fosp.test.mongo.MongoDbManager#insert(com.mongodb.BasicDBObject, java.lang.String)
	 */
	@Override
	public boolean insert(DBObject dbObject, String collectionName){		
		WriteResult result = getDBCollection(collectionName).insert(dbObject);
		
		String er = result.getError();
		if(er == null){
			return true;
		} else {
			System.err.println(er);
			return false;
		}
	}
	
	@Override
	public boolean update(DBObject query, DBObject dbObject, String collectionName){		
		WriteResult result =  getDBCollection(collectionName).update(query, dbObject);
		
		String er = result.getError();
		if(er == null){
			return true;
		} else {
			System.err.println(er);
			return false;
		}
	}
	
	@Override
	public boolean remove(DBObject dbObject, String collectionName) {
		WriteResult result =  getDBCollection(collectionName).remove(dbObject);
		return result.getError() == null;
	}

	@Override
	public DBCursor find(DBObject dbObject, String collectionName) {
		DBCursor cursor =  getDBCollection(collectionName).find(dbObject);       
		return cursor;
	}
	
	
	@Override
	public DBCursor find(DBObject dbObject, String collectionName, int start, int limit) {
		DBCursor cursor =  getDBCollection(collectionName).find(dbObject).skip(start).limit(limit);       
		return cursor;
	}
	
	@Override
	public void start() {
		try {
			// final Process p = new ProcessBuilder("java", "-jar",
			// "D:/Researchs/JsOptimizer/dist/JsOptimizer.jar").start();
			Logger.getLogger("com.mongodb").setLevel(Level.OFF); 
			boolean ok = false;
			try {
				Mongo m = new Mongo();
				DB dbcontacts = Mongo.connect(new DBAddress("127.0.0.1", "contacts"));

				if (!m.isLocked()) {
					ok = true;		

					System.out.println("mongod has started");
					Thread.sleep(10000);
		
					System.out.println("shutdown Mongo");
					dbcontacts.command("shutdown", 1);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			//System.exit(1);

			if (!ok) {
				final Process p = new ProcessBuilder(
						"D:/mongodb-win32-i386-2.0.7/bin/mongod",
						"--dbpath",
						"D:/mongodb-win32-i386-2.0.7/data/db", "--journal")
						.start();				

				new Thread(new Runnable() {
					public void run() {
						try {
							IOUtils.copy(p.getInputStream(), System.out);
						} catch (IOException e) {							
						}
					}
				}).start();
			} else {				
				System.out.println("mongod has started");
				int c = 0; 
				while (ok) {
					Thread.sleep(2000);
					System.out.println("working ...");
					c++;
					if(c >2){
						System.out.println("Bye...");
						System.exit(1);
					}					
					
				}
			}
			
			Thread hook = new Thread(new Runnable() {
				@Override
				public void run() {				
					System.out.println("exit ....");
				}
			});
			Runtime.getRuntime().addShutdownHook(hook);
			
			Thread.sleep(10000);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		
		
	}
	
	static void testInsert(){
		try {
			MongoDbManager dbManager = new MongoDbManagerImpl("50.57.180.237", "contact_db");
			
			BasicDBObject basicDBObject = new BasicDBObject();
			basicDBObject.put("name", "trieu");
			basicDBObject.put("email", "tantrieuf31.database90@gmail.com");			
			basicDBObject.put("groups", "user");
			String collectionName = "contacts";
			boolean n = dbManager.insert(basicDBObject , collectionName );
			System.out.println(n);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void testGet(){
		try {
			MongoDbManager dbManager = new MongoDbManagerImpl("50.57.180.237", "contact_db");
			DBCollection coll = dbManager.getDBCollection("contacts");
			
			BasicDBObject query = new BasicDBObject();
			Pattern pattern = Pattern.compile("gmail.com$", Pattern.CASE_INSENSITIVE);
		//	query.put("email", pattern);

	        DBCursor  cursor = coll.find(query);
	        System.out.println("cursor.size(): "+cursor.size()); 

	        try {
	            while(cursor.hasNext()) {
	                System.out.println(cursor.next());
	            }
	        } finally {
	            cursor.close();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void testDelete(){
		try {
			MongoDbManager dbManager = new MongoDbManagerImpl("50.57.180.237", "contact_db");
			DBCollection coll = dbManager.getDBCollection("contacts");
			
			BasicDBObject query = new BasicDBObject();
			coll.remove(query);
			
			
			Pattern pattern = Pattern.compile("gmail.com$", Pattern.CASE_INSENSITIVE);
			query.put("email", pattern);

	        DBCursor  cursor = coll.find(query);
	        System.out.println("cursor.size(): "+cursor.size()); 

	        try {
	            while(cursor.hasNext()) {
	                System.out.println(cursor.next());
	            }
	        } finally {
	            cursor.close();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		//testDelete();
		testGet();
	}



}
