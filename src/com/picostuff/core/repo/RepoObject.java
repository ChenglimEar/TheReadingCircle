package com.picostuff.core.repo;

import java.io.File;
import java.util.Properties;

public abstract class RepoObject {
	protected Properties prop;
	private File file;

	protected RepoObject setFile(File file,Properties prop) {
		this.file = file;
		this.prop = prop;
		return this;
	}
	
	protected File getFile() {
		return file;
	}
	
	public RepoObject fillFromCollection(Collection collection, String key) {
		collection.fillObject(this, key);
		return this;
	}
	
	public abstract void prepareToSave();
	public abstract String getName();
	
	// TODO: this should also be protected since it's a modification of an existing file and multiple
	// threads can be accessing this file at the same time
	public void remove() {
		file.delete();
	}
	
	public void saveToDataStore(DataStore store) {
		store.save(this);
	}
	public void saveToCollection(Collection collection) {
		collection.save(this);
	}
	
}
