package com.picostuff.readingcircle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.picostuff.core.repo.SystemInfo;
import com.picostuff.readingcircle.repo.ExternalItem;
import com.picostuff.readingcircle.repo.Item;
import com.picostuff.readingcircle.repo.Reader;
import com.picostuff.readingcircle.repo.ReadingCircleDataStore;

public class ReadingCircle {
	
	private ReadingCircleDataStore dataStore = new ReadingCircleDataStore();
	private Map<String,ExternalSystem> externalSystems = new HashMap<String, ExternalSystem>();
	
	public ReadingCircle() {
		String[] keys = dataStore.getSystemKeys();
		for (String key:keys) {
			SystemInfo system = dataStore.getSystem(key);
			Class<?> clazz;
			try {
				clazz = Class.forName(system.getClassName());
				Constructor<?> constructor = clazz.getConstructor(SystemInfo.class);
				ExternalSystem extSystem = (ExternalSystem)constructor.newInstance(system);
				externalSystems.put(extSystem.getCode(), extSystem);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String echo(String incoming) {
		return incoming;
	}
	
	public boolean readerExists(String name) {
		String readerKey = dataStore.keyFromName(name);
		return (dataStore.readerExists(readerKey));
	}
	
	public void updateReader(String name) {
		String readerKey = dataStore.keyFromName(name);
		if (dataStore.readerExists(readerKey)) {
			Reader reader = dataStore.getReader(readerKey);
			reader.setName(name);
			dataStore.save(reader);
		} else {
			throw new RuntimeException("Trying to update non-existent user");
		}
	}
	
	public void addReader(String name) {
		String readerKey = dataStore.keyFromName(name);
		if (!dataStore.readerExists(readerKey)) {
			Reader reader = dataStore.getReader(readerKey);
			reader.setName(name);
			dataStore.save(reader);
		} else {
			throw new RuntimeException("Trying to add existent user");
		}
	}
	
	public String[] getReaders() {
		//return new String[] {"chenglim","maire","lee","elaine"};
		List<String> readers = new ArrayList<String>();
		String[] readerKeys = dataStore.getReaderKeys();
		for (String readerKey:readerKeys) {
			Reader reader = dataStore.getReader(readerKey);
			readers.add(reader.getName());
		}
		return readers.toArray(new String[] {});
	}

	public void updateItem(String name) {
		String itemKey = dataStore.keyFromName(name);
		if (dataStore.itemExists(itemKey)) {
			Item item = dataStore.getItem(itemKey);
			item.setName(name);
			dataStore.save(item);
		} else {
			throw new RuntimeException("Trying to update non-existent user");
		}
	}
	
	public void addItem(String name) {
		String itemKey = dataStore.keyFromName(name);
		if (!dataStore.itemExists(itemKey)) {
			Item item = dataStore.getItem(itemKey);
			item.setName(name);
			dataStore.save(item);
		} else {
			throw new RuntimeException("Trying to add existent user");
		}
	}
	
	public void checkinItem(String name) throws Exception {
		String itemKey = dataStore.keyFromName(name);
		if (dataStore.itemExists(itemKey)) {
			Item item = dataStore.getItem(itemKey);
			ExternalItem extItem = item.getExtItem();
			if (extItem == null) {
				item.setStatus("done");
				dataStore.save( item);
			} else {
				String code = item.getExternalSystem();
				ExternalSystem extSystem = externalSystems.get(code);
				if (extSystem != null) {
					extSystem.checkin(extItem);
				}
			}
		} else {
			throw new Exception("Item does not exist for checking in");
		}
	}

	public void markItemAtMyDesk(String name, String reader) throws Exception {
		String itemKey = dataStore.keyFromName(name);
		if (dataStore.itemExists(itemKey)) {
			Item item = dataStore.getItem(itemKey);
			item.setReader(reader);
			item.setStatus("done");
			dataStore.save(item);
		} else {
			throw new Exception("Item does not exist for marking available");
		}
	}

	public void checkoutItem(String name, String reader) throws Exception {
		String itemKey = dataStore.keyFromName(name);
		if (dataStore.itemExists(itemKey)) {
			Item item = dataStore.getItem(itemKey);
			item.setReader(reader);
			ExternalItem extItem = item.getExtItem();
			if (extItem == null) {
				item.setStatus("reading");
			} else {
				String code = item.getExternalSystem();
				ExternalSystem extSystem = externalSystems.get(code);
				if (extSystem != null) {
					extSystem.checkout(reader,extItem);
				}
			}
			dataStore.save(item);
		} else {
			throw new Exception("Item does not exist for checking out");
		}
	}

	public void markItemUnknown(String name) throws Exception {
		String itemKey = dataStore.keyFromName(name);
		if (dataStore.itemExists(itemKey)) {
			Item item = dataStore.getItem(itemKey);
			item.setReader(null);
			item.setStatus(null);
			dataStore.save(item);
		} else {
			throw new Exception("Item does not exist for marking unknown");
		}
	}

	public List<Item> getCollection() {		
		List<Item> items = new ArrayList<Item>();
		String[] itemKeys = dataStore.getItemKeys();
		for (String itemKey:itemKeys) {
			Item item = dataStore.getItem(itemKey);
			ExternalItem extItem = item.getExtItem();
			if (extItem != null) {
				String code = item.getExternalSystem();
				ExternalSystem extSystem = externalSystems.get(code);
				if (extSystem != null) {
					extSystem.fillItem(extItem);
				}
				dataStore.save(item);
			}
			items.add(item);
		}
		return items;
	}
	
	public String[] getItemNames(List<Item> items) {
		List<String> itemsArray = new ArrayList<String>();
		for (Item item:items) {
			itemsArray.add(item.getName());
		}
		return itemsArray.toArray(new String[] {});
	}

	public List<Item> getCurrentlyReading(String sessionReader) {
		List<Item> items = new ArrayList<Item>();
		String[] itemKeys = dataStore.getItemKeys();
		for (String itemKey:itemKeys) {
			Item item = dataStore.getItem(itemKey);
			ExternalItem extItem = item.getExtItem();
			if (extItem != null) {
				String code = item.getExternalSystem();
				ExternalSystem extSystem = externalSystems.get(code);
				if (extSystem != null) {
					extSystem.fillItem(extItem);
					dataStore.save(item);
				}
			}
			String reader = item.getReader();
			String status = item.getStatus();
			if (status == null)
				status = "<failed to retrieve status>";
			
			if ((reader != null) && reader.equals(sessionReader) && (status.equals("reading")))
				items.add(item);
		}
		return items;
	}

	public List<Item> getAtMyDesk(String sessionReader) {
		//return new String[] {"item 1","item 2","item 3","item 4", "item 5"};
		List<Item> items = new ArrayList<Item>();
		String[] itemKeys = dataStore.getItemKeys();
		for (String itemKey:itemKeys) {
			Item item = dataStore.getItem(itemKey);
			String reader = item.getReader();
			if ((reader != null) && reader.equals(sessionReader)) {
				ExternalItem extItem = item.getExtItem();
				if (extItem != null) {
					String code = item.getExternalSystem();
					ExternalSystem extSystem = externalSystems.get(code);
					if (extSystem != null) {
						extSystem.fillItem(extItem);
						dataStore.save(item);
					}
				}
				String status = item.getStatus();
				if (status == null)
					status = "<failed to retrieve status>";
				if (status.equals("done"))
					items.add(item);
			}
		}
		return items;
	}
	
	public List<Item> getUnknowns() {
		//return new String[] {"item 1","item 2","item 3","item 4", "item 5"};
		List<Item> items = new ArrayList<Item>();
		String[] itemKeys = dataStore.getItemKeys();
		for (String itemKey:itemKeys) {
			Item item = dataStore.getItem(itemKey);
			ExternalItem extItem = item.getExtItem();
			if (extItem != null) {
				String code = item.getExternalSystem();
				ExternalSystem extSystem = externalSystems.get(code);
				if (extSystem != null) {
					extSystem.fillItem(extItem);
					dataStore.save(item);
				}
			}
			if (item.getReader() == null)
				items.add(item);
		}
		return items;
	}


	////////////
	// TESTS
	
	@Test
	public void testGetReaders() {
		initCollection();
		String[] readers = getReaders();
		Assert.assertEquals("check num readers", 4, readers.length);
		Assert.assertEquals("check reader name", "chenglim", readers[0]);
	}
	
	private void initCollection() {
		dataStore.clear();
		// readers
		dataStore.getReader("chenglim.properties").setName("chenglim").saveToDataStore(dataStore);
		dataStore.getReader("maire.properties").setName("maire").saveToDataStore(dataStore);
		dataStore.getReader("lee.properties").setName("lee").saveToDataStore(dataStore);
		dataStore.getReader("elaine.properties").setName("elaine").saveToDataStore(dataStore);
		
		// available
		dataStore.getItem("item1.properties")
			.setName("item1")
			.setTitle("The First Title")
			.setReader("chenglim")
			.setStatus("done")
			.saveToDataStore(dataStore);
		// reading
		dataStore.getItem("item2.properties")
			.setName("item2")
			.setTitle("The Second Title")
			.setReader("chenglim")
			.setStatus("reading")
			.saveToDataStore(dataStore);
		// available
		dataStore.getItem("item3.properties")
			.setName("item3")
			.setTitle("The Third Title")
			.setReader("chenglim")
			.setStatus("done")
			.saveToDataStore(dataStore);
		// unknown
		dataStore.getItem("item4.properties")
			.setName("item4")
			.setTitle("The Fourth Title")
			.saveToDataStore(dataStore);
		// leap items
		dataStore.getItem("item5.properties")
			.setName("item5")
			.setExternalSystem("leap")
			.setExternalId("2236480")
			.setReader("chenglim")
			.saveToDataStore(dataStore);
	}

	@Test
	public void testGetCollection() {
		initCollection();
		List<Item> collection = getCollection();
		Assert.assertEquals("check num items", 5, collection.size());
		Assert.assertEquals("check item name", "item1", collection.get(0).getName());
		Assert.assertEquals("reading", collection.get(4).getStatus());
		Assert.assertEquals("Harry Potter and the Half-Blood Prince", collection.get(4).getTitle());
	}

	@Test
	public void testGetCurrentlyReading() {
		initCollection();
		List<Item> collection = getCurrentlyReading("chenglim");
		Assert.assertEquals("check num items", 1, collection.size());
		Assert.assertEquals("check item name", "item2", collection.get(0).getName());
	}

	@Test
	public void testGetAtMyDesk() {
		initCollection();
		List<Item> collection = getAtMyDesk("chenglim");
		Assert.assertEquals("check num items", 3, collection.size());
		Assert.assertEquals("check item name", "item1", collection.get(0).getName());
	}

	@Test
	public void testGetUnknowns() {
		initCollection();
		List<Item> collection = getUnknowns();
		Assert.assertEquals("check num items", 1, collection.size());
		Assert.assertEquals("check item name", "item4", collection.get(0).getName());
	}

	
}
