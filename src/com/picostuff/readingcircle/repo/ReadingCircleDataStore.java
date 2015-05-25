package com.picostuff.readingcircle.repo;

import org.junit.Assert;
import org.junit.Test;

import com.picostuff.core.repo.Collection;
import com.picostuff.core.repo.DataStore;

public class ReadingCircleDataStore extends DataStore {
	private Collection readers;
	private Collection items;

	public ReadingCircleDataStore() {
		readers = new Collection().fillFromRepo("readers");
		items = new Collection().fillFromRepo("items");
		
	}
	public Collection getReaders() {
		return readers;
	}
	public Collection getItems() {
		return items;
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
	
	
	public String[] getReaderKeys() {
		return readers.getKeys();
	}
	
	public String[] getItemKeys() {
		return items.getKeys();
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
