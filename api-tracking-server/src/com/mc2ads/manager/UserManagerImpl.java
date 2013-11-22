package com.mc2ads.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mc2ads.db.SqlDbConfigs;
import com.mc2ads.db.mongo.MongoDbManager;
import com.mc2ads.db.mongo.MongoDbManagerImpl;
import com.mc2ads.filter.UserFilter;
import com.mc2ads.model.User;
import com.mc2ads.utils.MailUtil;
import com.mc2ads.utils.StringUtil;
import com.mc2ads.utils.TemplateUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class UserManagerImpl implements UserManager {
	MongoDbManager dbManager = null;
	public static final String COLLECTION_NAME = "contacts";
	static final int LIMIT = 36000;
	
	static UserManager theInstance;
	public static UserManager getTheInstance() {
		if(theInstance == null){
			theInstance = new UserManagerImpl();
		}
		return theInstance;
	}
	
	protected UserManagerImpl(){
		try {
			SqlDbConfigs configs = SqlDbConfigs.load( "user");
			dbManager = new MongoDbManagerImpl(configs.getHost(), configs.getDatabase());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean save(User contact) {
		DBObject o = contact.toMongoObject();
		boolean rs = dbManager.insert(o, COLLECTION_NAME);
		if( ! rs ){
			DBObject q = new BasicDBObject("email", contact.getEmail() );
			return dbManager.update(q, o, COLLECTION_NAME);
		}
		return rs;
	}
	
	public int count(UserFilter filter){
		DBCursor cursor =  dbManager.find(filter.toMongoQuery(),COLLECTION_NAME);;
		return cursor.size();	
	}
	
	public int count(DBObject q){
		DBCursor cursor = dbManager.find(q,COLLECTION_NAME);;
		return cursor.size();	
	}
	

	
	public List<User> find(UserFilter filter) {
		DBCursor cursor = null;
		if(filter.getLimit()>0){
			cursor = dbManager.find(filter.toMongoQuery(),COLLECTION_NAME,filter.getStart(),filter.getLimit() );
		} else {
			cursor = dbManager.find(filter.toMongoQuery(),COLLECTION_NAME);
		}		
		
        //System.out.println("cursor.size(): "+cursor.size()); 
        List<User> contacts = new ArrayList<User>();
        try {
            while(cursor.hasNext()) {
            	contacts.add(new User(cursor.next()));
            }
        }catch(Exception ext){
            ext.printStackTrace();
        } finally {
            cursor.close();
        }
        return contacts;
	}
	
	public List<User> find(DBObject q)
	{
		DBCursor cursor = dbManager.find(q,COLLECTION_NAME);
        System.out.println("cursor.size(): "+cursor.size()); 
        List<User> contacts = new ArrayList<User>(cursor.size());
        try {
            while(cursor.hasNext()) {
            	contacts.add(new User(cursor.next()));
            }
        }catch(Exception ext){
            ext.printStackTrace();
        } finally {
            cursor.close();
        }
        return contacts;
	}

	
	public boolean delete(UserFilter filter) {
		return dbManager.remove(filter.toMongoQuery(), COLLECTION_NAME);
	}
	
	
	public boolean delete(DBObject filter){
		return dbManager.remove(filter, COLLECTION_NAME);
	}
	
	public static void main(String[] args) throws InterruptedException {
		UserManager manager = new UserManagerImpl();
		User contact = new User("trieu@mc2ads.com", "Trieu Nguyen");
		//System.out.println(manager.save(contact )); ;
		UserFilter filter = new UserFilter(0,-1);
		//filter.setFilterByField("email", Pattern.compile("gmail.com$", Pattern.CASE_INSENSITIVE));
		List<User> list = manager.find(filter);
		Set<String> email = new HashSet<String>(2);
		email.add("trieu@mc2ads.com");
		//email.add("founder.i2tree@gmail.com");
		
		Map<String, Object> dataModel = new HashMap<String, Object>();
		String mailContent = TemplateUtils.processModel(dataModel , "email/", "user-confirm.ftl");
		
		for (User userContact : list) {
			System.out.println(userContact.getEmail());
			System.out.println(userContact.getName());
			System.out.println(userContact.getGroups());
			MailUtil.send(userContact.getEmail(), email , "User Confirmation", mailContent);
		}		
		//Thread.sleep(5000);
	}

	@Override
	public boolean sendMailsToContacts(String to_users, String from, String  subject, String mail_content , String exclude_domains ) {
		String[] to_users_toks = to_users.split(",");
		Set<String> emails = new HashSet<String>(90000);
		 
		int c = 0;
		for (String s : to_users_toks) {
			s = s.trim();
			if( ! s.isEmpty() ){
				if( StringUtil.isValidEmailAddress(s) ){
					emails.add(s);
				} else {
					System.out.println(" filter group " + s);
					DBObject q = new BasicDBObject("groups", s);
									
					List<User> users = this.find(q);
					System.out.println("total: " + users.size() + " in group " + s);
					for (User user : users) {
						System.out.println(user.getEmail());
						if( StringUtil.isValidEmailAddress(user.getEmail(),exclude_domains) ){
							emails.add(user.getEmail());
							c++;
							if(c > LIMIT){
								break;
							}
						}
					}
				}
			}	
		}
		return MailUtil.send(from, emails , subject, mail_content);
	}

}
