package com.picostuff.core.repo;


public class SystemInfo extends RepoObject {
	
	@Override
	public void prepareToSave() {
		// No external data at this time, so nothing to do
	}
	
	public SystemInfo setName(String name) {
		prop.put("name", name);
		return this;
	}
	
	public String getName() {
		return (String)prop.get("name");
	}

	public SystemInfo setClassName(String name) {
		prop.put("class", name);
		return this;
	}
	
	public String getClassName() {
		return (String)prop.get("class");
	}

	public SystemInfo setAuthHeader(String authHeader) {
		prop.put("authHeader", authHeader);
		return this;
	}
	
	public String getAuthHeader() {
		return (String)prop.get("authHeader");
	}

}
