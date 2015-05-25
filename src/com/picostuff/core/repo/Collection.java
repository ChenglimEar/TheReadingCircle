package com.picostuff.core.repo;

import java.io.File;

public class Collection {
	protected Repository repo;
	private File file;
	
	public Collection() {
		repo = Repository.INSTANCE;
	}
	
	protected void setFile(File file) {
		this.file = file;
	}
	
	public Collection fillFromRepo(String key) {
		repo.fillCollection(this, key);
		return this;
	}
	protected File getFile() {
		return file;
	}
	public boolean objectExists(String key) {
		File objectFile = new File(file,key);
		return objectFile.exists();
	}
	private File getFile(String key) {
		File objectFile = new File(file,key);
		return objectFile;
	}
	public RepoObject fillObject(RepoObject object, String key) {
		repo.lockAndLoad(object, getFile(key));
		return object;
	}
	public String[] getKeys() {
		return file.list();
	}
	public void clear() {
		if (file.isDirectory()) {
			String[] keys = getKeys();
			for (String key:keys) {
				new AnyRepoObject().fillFromCollection(this, key).remove();
			}
		}
	}
	public String keyFromName(String name) {
		return name + ".properties";
	}
	public void save(RepoObject object) {
		repo.lockAndSave(object);
	}
	public void waitForChangeByName(String name) {
		repo.waitForChange(getFile(keyFromName(name)));
	}

}
