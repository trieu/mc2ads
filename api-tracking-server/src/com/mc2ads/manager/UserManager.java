package com.mc2ads.manager;

import java.util.List;

import com.mc2ads.filter.UserFilter;
import com.mc2ads.model.User;
import com.mongodb.DBObject;

public interface UserManager {

	public boolean save(User contact);
	public boolean delete(UserFilter filter);
	public boolean delete(DBObject filter);
	public List<User> find(UserFilter contact);
	public List<User> find(DBObject q);
	public int count(UserFilter contact);
	public int count(DBObject q);
	public boolean sendMailsToContacts(String to_users, String from, String  subject, String mail_content , String exclude_domains );

}
