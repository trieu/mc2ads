package com.mc2ads.storm.analytics.model;

import java.util.ArrayList;
import java.util.List;

public class User {
	
	long id;
	List<Webpage> visitedPages = new ArrayList<Webpage>();
	List<Item> clickedItems = new ArrayList<Item>();
	InterestGraph interestGraph;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<Webpage> getVisitedPages() {
		return visitedPages;
	}
	public void setVisitedPages(List<Webpage> visitedPages) {
		this.visitedPages = visitedPages;
	}
	public List<Item> getClickedItems() {
		return clickedItems;
	}
	public void setClickedItems(List<Item> clickedItems) {
		this.clickedItems = clickedItems;
	}
	public InterestGraph getInterestGraph() {
		return interestGraph;
	}
	public void setInterestGraph(InterestGraph interestGraph) {
		this.interestGraph = interestGraph;
	}
	
	
}
