package com.picostuff.readingcircle.repo;

import java.io.File;

import com.picostuff.core.repo.RepoObject;

public class Item extends RepoObject {
	private ExternalItem extItem;
	
	public void prepareToSave() {
		if (extItem != null) {
			String title = extItem.getTitle();
			if (title != null)
				setTitle(title);
			String status = extItem.getStatus();
			if (status != null)
				setStatus(status);
		}
	}
	
	public Item setName(String name) {
		prop.put("name", name);
		return this;
	}
	
	public String getName() {
		return (String)prop.get("name");
	}
	
	public Item setExternalId(String id) {
		prop.put("extid", id);
		return this;
	}
	
	public String getExternalId() {
		return (String)prop.get("extid");
	}
	
	public Item setExternalSystem(String system) {
		prop.put("extsys",system);
		return this;
	}

	public String getExternalSystem() {
		return (String)prop.get("extsys");
	}
	
	public Item setTitle(String title) {
		prop.put("title", title);
		return this;
	}
	
	public String getTitle() {
		if ((extItem == null) || (extItem.getTitle() == null))
			return (String)prop.get("title");
		else
			return extItem.getTitle();
	}

	public Item setReader(String reader) {
		if (reader == null)
			prop.remove("reader");
		else
			prop.put("reader", reader);
		return this;
	}

	public String getReader() {
		return (String)prop.get("reader");
	}
	
	public Item setStatus(String status) {
		if (status == null) 
			prop.remove("status");
		else
			prop.put("status",status);
		return this;
	}
	
	public String getStatus() {
		if ((extItem == null) || (extItem.getStatus() == null))
			return (String)prop.get("status");
		else
			return extItem.getStatus();
	}
	
	public Item setExtItem(ExternalItem extItem) {
		this.extItem = extItem;
		return this;
	}
	
	public ExternalItem getExtItem() {
		if (extItem == null) {
			String extSys = getExternalSystem();
			if (extSys != null)
				extItem = new ExternalItem(getExternalId());
		}
		return extItem;
	}

}
