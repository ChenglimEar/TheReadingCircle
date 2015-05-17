package com.picostuff.core.repo;

import java.io.File;

public class Collection {
	private Repository repo;
	private File file;
	
	protected void setFile(Repository repo, File file) {
		this.repo = repo;
		this.file = file;
	}
	
	public Collection fillFromRepo(Repository repo, String key) {
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

}
