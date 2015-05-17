package com.picostuff.core.repo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This class should be protected from access outside the package.
 *
 */
public class Repository {
	protected final static Repository INSTANCE = new Repository();
	private Map<String, SyncSaver> savers = new HashMap<String, SyncSaver>();
	private File repo;
	
	private Repository() {
		String repoPath = System.getProperty("repo");
		if (repoPath != null) {
			repo = new File(repoPath);
		} else {
			repo = new File("repo");
		}
		System.out.println("repo: " + repo.getAbsolutePath());
		if (!repo.exists())
			repo.mkdir();
	}
	
	protected void clear() {
		String[] colKeys = repo.list();
		for (String key:colKeys) {
			Collection collection = new Collection().fillFromRepo(this, key);
			collection.clear();
		}
		
		savers.clear();
	}
	
	private synchronized SyncSaver getSaver(String key) {
		SyncSaver saver = savers.get(key);
		if (saver == null) {
			saver = new SyncSaver();
			savers.put(key, saver);
		}
		return saver;
	}
	
	protected void lockAndSave(RepoObject object) {
		// a simple way to make it thread-safe
		SyncSaver saver = getSaver(object.getFile().getName());
		saver.syncSave(object);
	}
	
	protected void lockAndLoad(RepoObject object, File file) {
		// a simple way to make it thread-safe
		SyncSaver saver = getSaver(file.getName());
		saver.syncLoad(object, file);
	}
	
	protected File makeCollection(String colKey) {
		File collection = getCollection(colKey);
		if (!collection.exists())
			collection.mkdir();
		return collection;
	}
	
	protected void fillCollection(Collection collection, String key) {
		collection.setFile(this,makeCollection(key));
	}
	protected File getCollection(String colKey) {
		File collection = new File(repo,colKey);
		return collection;
	}
	protected File getFile(String colKey, String key) {
		File file = new File(getCollection(colKey),key);
		return file;
	}
	protected File getFile(File collection, String key) {
		File file = new File(collection,key);
		return file;
	}
	
}
