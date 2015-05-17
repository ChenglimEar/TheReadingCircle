package com.picostuff.readingcircle.repo;

import org.junit.Assert;
import org.junit.Test;

import com.picostuff.core.repo.Collection;
import com.picostuff.core.repo.DataStore;

public class ReadingCircleDataStore extends DataStore {
	private Collection readers;
	private Collection items;
	private Collection systems;

	public ReadingCircleDataStore() {
		readers = new Collection().fillFromRepo(repo, "readers");
		items = new Collection().fillFromRepo(repo, "items");
		systems = new Collection().fillFromRepo(repo,"systems");
		
	}
	public Collection getReaders() {
		return readers;
	}
	public Collection getItems() {
		return items;
	}
	public Collection getSystems() {
		return systems;
	}
	public boolean readerExists(String key) {
		return readers.objectExists(key);
	}
	
	public Reader getReader(String key) {
		return (Reader)readers.fillObject(new Reader(), key);
	}

	public boolean itemExists(String key) {
		return items.objectExists(key);
	}

	public Item getItem(String key) {
		return (Item)items.fillObject(new Item(), key);
	}
	
	public SystemInfo getSystem(String key) {
		return (SystemInfo)systems.fillObject(new SystemInfo(), key);
	}
	
	public String[] getReaderKeys() {
		return readers.getKeys();
	}
	
	public String[] getItemKeys() {
		return items.getKeys();
	}
	public String[] getSystemKeys() {
		return systems.getKeys();
	}
	
	////////////
	// tests
	
	@Test
	public void testDataStore() {
		ReadingCircleDataStore dataStore = new ReadingCircleDataStore();
		dataStore.clear();
		String[] readerKeys = dataStore.getReaderKeys();
		Assert.assertEquals("check no readers", 0, readerKeys.length);
		String[] itemKeys = dataStore.getItemKeys();
		Assert.assertEquals("check no items", 0, itemKeys.length);
		Reader reader = dataStore.getReader("chenglim.properties");
		reader.setName("chenglim");
		dataStore.save(reader);
		reader = dataStore.getReader("chenglim.properties");
		String name = reader.getName();
		Assert.assertEquals("check name saved", "chenglim", name);
		
	}
}
