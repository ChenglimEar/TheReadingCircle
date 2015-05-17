package com.picostuff.readingcircle;

import java.io.IOException;

import com.picostuff.readingcircle.repo.ExternalItem;

import us.monoid.json.JSONException;

public abstract class ExternalSystem {
	public abstract String getCode();
	
	public abstract void fillItem(ExternalItem item);
	
	public abstract void checkin(ExternalItem item) throws IOException, JSONException;
	
	public abstract void checkout(String patron, ExternalItem item) throws IOException, JSONException;
}
