package com.picostuff.readingcircle.repo;

import java.io.File;

import com.picostuff.core.repo.RepoObject;

public class Reader extends RepoObject {
	
	@Override
	public void prepareToSave() {
		// No external data at this time, so nothing to do
	}
	
	public Reader setName(String name) {
		prop.put("name", name);
		return this;
	}
	
	public String getName() {
		return (String)prop.get("name");
	}

}
