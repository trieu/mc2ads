package com.mc2ads.model;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import com.mc2ads.db.mongo.MongoObject;
import com.mc2ads.utils.ParamUtil;
import com.mc2ads.utils.StringUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class User extends MongoObject {

	public static final String EMAIL_CONTACT = "email";
	public static final String FACEBOOK_CONTACT = "facebook";

	protected String email = "";
	protected String facebookId = "";
	protected String name = "";
	protected Set<String> groups = new HashSet<String>();
	
	
	public User() {
		super();
	}

	public User(HttpServletRequest request){
		super();
		this.email = ParamUtil.getString(request, "email", "");
		this.facebookId = ParamUtil.getString(request, "facebook_id", "");
		this.name = ParamUtil.getString(request, "name", "");
		String[] groupToks = ParamUtil.getString(request, "groups", "").split(",");
		
		if( StringUtil.isEmpty(email) ){
			throw new IllegalArgumentException("No identity info, email can not be null");
		}
		groups.add(EMAIL_CONTACT);
		if(!this.facebookId.isEmpty()){
			groups.add(FACEBOOK_CONTACT);
		}
		for (String g : groupToks) {
			if(!g.isEmpty()){
				groups.add(g.toLowerCase().trim());
			}			
		}
		
	}
	
	public User(DBObject obj ){
		this.email = obj.get("email").toString();
		this.facebookId = obj.get("facebook_id").toString();
		this.name = obj.get("name").toString();
		setGroups( obj.get("groups")) ;
	}

	public User(String email) {
		super();
		if( StringUtil.isEmpty(email) ){
			throw new IllegalArgumentException("Email can not be null");
		}
		this.email = email;
		groups.add(EMAIL_CONTACT);
	}
	
	

	public User(String email, String name) {
		super();
		if( StringUtil.isEmpty(email) ){
			throw new IllegalArgumentException("Email can not be null");
		}
		this.email = email;
		this.name = name;
		groups.add(EMAIL_CONTACT);
	}

	public User(String email, String facebookId, String name) {		
		super();
		if( StringUtil.isEmpty(email) ){
			throw new IllegalArgumentException("Email can not be null");
		}
		this.email = email;
		this.facebookId = facebookId;
		this.name = name;
		groups.add(EMAIL_CONTACT);
		
		if( ! StringUtil.isEmpty(facebookId) ){
			groups.add(FACEBOOK_CONTACT);
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getGroups() {
		return groups;
	}

	public void setGroups(Object groups) {
		if(groups != null){		
			if(groups.getClass().getName().equals("com.mongodb.BasicDBList")){
				BasicDBList list  = (BasicDBList) groups;
				for (int i = 0; i < list.size(); i++) {
					this.groups.add(list.get(i).toString());
				}
			}
		}
	}

	public boolean addGroup(String group) {
		return this.groups.add(group);
	}
	
	public BasicDBObject toMongoObject(){
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("name", this.name);
		basicDBObject.put("email", this.email);		
		basicDBObject.put("facebook_id", this.facebookId);		
		basicDBObject.put("groups", this.groups);
		return basicDBObject;
	}

}

