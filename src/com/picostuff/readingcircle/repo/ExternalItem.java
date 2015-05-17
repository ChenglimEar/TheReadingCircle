package com.picostuff.readingcircle.repo;

public class ExternalItem {
	private String id;
	private String title;
	private String status;
	
	public ExternalItem(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public ExternalItem setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public String getTitle() {
		return title;
	}
	
	public ExternalItem setStatus(String status) {
		this.status = status;
		return this;
	}
	
	public String getStatus() {
		return status;
	}
	
}
