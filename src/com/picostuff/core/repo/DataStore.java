package com.picostuff.core.repo;

public class DataStore {
	protected Repository repo;
	private Collection systems;
	
	public DataStore() {
		repo = Repository.INSTANCE;
		systems = new Collection().fillFromRepo("systems");
	}
	public Collection getSystems() {
		return systems;
	}
	public SystemInfo getSystem(String key) {
		return (SystemInfo)systems.fillObject(new SystemInfo(), key);
	}
	public String[] getSystemKeys() {
		return systems.getKeys();
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
