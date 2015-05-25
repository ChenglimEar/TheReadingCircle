package com.picostuff.core.repo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


public class SyncSaver {
	public synchronized void syncSave(RepoObject object) {
		object.prepareToSave();
		save(object.prop, object.getFile());
		notifyAll();
	}
	public synchronized void syncLoad(RepoObject object, File file) {
		Properties prop = loadProperties(file);
		object.setFile(file,prop);
	}
	public synchronized void waitForChange() {
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void save(Properties prop, File file) {
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

	private Properties loadProperties(File file) {
		Properties prop = new Properties();
		if (!file.exists())
			return prop;
		
		InputStream input = null;
	 
		try {
			input = new FileInputStream(file);
	 
			// load a properties file
			prop.load(input);
	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}
	

}
