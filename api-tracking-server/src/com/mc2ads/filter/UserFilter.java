package com.mc2ads.filter;

import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.mc2ads.model.User;
import com.mc2ads.utils.ParamUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class UserFilter extends User {

	BasicDBObject query = new BasicDBObject();
	int start = 0;
	int limit = 0;
	

	public UserFilter(int start, int limit) {
		super();
		this.start = start;
		this.limit = limit;
	}

	public UserFilter() {
		super();
	}

	public UserFilter(HttpServletRequest request) {
		super();
		email = ParamUtil.getString(request, "email", "");
		start = ParamUtil.getInteger(request, "start", 0);
		limit = ParamUtil.getInteger(request, "limit", 0);
		boolean all = ParamUtil.getBoolean(request, "all", false);
		if(all){
			start = 0;
			limit = -1;
		}
		if (!email.isEmpty()) {
			query.put("email", email);
		}

		String[] groupToks = ParamUtil.getString(request, "groups", "").split(",");

		BasicDBList list = new BasicDBList();
		for (String g : groupToks) {
			if (!g.isEmpty()) {
				list.add(g.toLowerCase().trim());
			}
		}
		if (list.size() > 0) {
			query.put("groups", new BasicDBObject("$in", list));
		}

	}

	public UserFilter(String email, String name) {
		super(email, name);
		// TODO Auto-generated constructor stub
	}

	public UserFilter(String email) {
		super(email);
		// TODO Auto-generated constructor stub
	}

	public UserFilter(String email, String facebookId, String name) {
		super(email, facebookId, name);
		// TODO Auto-generated constructor stub
	}

	public void setFilterByPattern(String name, Pattern pattern) {
		query.put(name, pattern);
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public BasicDBObject toMongoQuery() {
		if (start == 0 && limit == -1) {
			// show all
			return new BasicDBObject();
		}
		Set<String> keys = query.keySet();
		for (String k : keys) {
			Object v = query.get(k);
			System.out.println(k + "->" + v);
		}

		return query;
	}

}
