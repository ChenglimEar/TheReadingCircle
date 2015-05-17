package com.picostuff.core.repo;

public class DataStore {
	protected Repository repo;
	
	public DataStore() {
		repo = Repository.INSTANCE;
	}
	
	public String keyFromName(String name) {
		return name + ".properties";
	}

	public void clear() {
		repo.clear();
	}

	public void save(RepoObject object) {
		repo.lockAndSave(object);
	}
}
