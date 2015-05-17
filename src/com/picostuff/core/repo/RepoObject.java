package com.picostuff.core.repo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
	
	/*
	public void saveToRepo(Repository repo) {
		repo.lockAndSave(this);
	}
	*/
	
	/*
	protected void save() {
		prepareToSave();
		OutputStream output = null;
		 
		try {
	 
			output = new FileOutputStream(file);
	 	 
			// save properties to project root folder
			prop.store(output, null);
	 
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	 
		}
		
	}
	*/
}
