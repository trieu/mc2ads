package com.mc2ads.db.mongo;

import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

public class MongoQueryUtil {
	
	/**
	 * Get the next unique ID for a named sequence.
	 * @param db Mongo database to work with
	 * @param seq_name The name of your sequence (I name mine after my collections)
	 * @return The next ID
	 */
	public static String getNextId(DB db, String seq_name) {
	    String sequence_collection = "seq"; // the name of the sequence collection
	    String sequence_field = "seq"; // the name of the field which holds the sequence

	    DBCollection seq = db.getCollection(sequence_collection); // get the collection (this will create it if needed)
	 
	    // this object represents your "query", its analogous to a WHERE clause in SQL
	    DBObject query = new BasicDBObject();
	    query.put("_id", seq_name); // where _id = the input sequence name
	 
	    // this object represents the "update" or the SET blah=blah in SQL
	    DBObject change = new BasicDBObject(sequence_field, 1);
	    DBObject update = new BasicDBObject("$inc", change); // the $inc here is a mongodb command for increment
	 
	    // Atomically updates the sequence field and returns the value for you
	    DBObject res = seq.findAndModify(query, new BasicDBObject(), new BasicDBObject(), false, update, true, true);
	    return res.get(sequence_field).toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Mongo m = null ;
		try {
			m = new Mongo();
			m.setWriteConcern(WriteConcern.SAFE);
			DB contactsDB = m.connect(new DBAddress("50.57.180.237", "contact_db"));
		
			DBCollection coll = contactsDB.getCollection("contacts");
			
			BasicDBObject query = new BasicDBObject();

			//exact find
	        //query.put("email", "trieu@mc2ads.com");
			
			//see http://www.vogella.com/articles/JavaRegularExpressions/article.html
			// any string contain "database"
			//Pattern pattern = Pattern.compile(".*(database).*", Pattern.CASE_INSENSITIVE);
			
			//regex must match at the beginning of the line
			//Pattern pattern = Pattern.compile("^tan", Pattern.CASE_INSENSITIVE);
			
			//Finds regex must match at the end of the line
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
			
			
			BasicDBObject basicDBObject = new BasicDBObject();
			basicDBObject.put("name", "trieu");
			basicDBObject.put("email", "tantrieuf31.database1@gmail.com");			
	        basicDBObject.put("groups", "user");
	        
//	        WriteResult rs = coll.insert(basicDBObject);
//	        String errMsg = rs.getError();
//	        if(errMsg != null)
//	        	System.out.println(errMsg);
//	        else 
//	        	System.out.println("Ok ");
	        
	      //  m.close();			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if(m != null){
				m.close();
			}
		}

	}

}
