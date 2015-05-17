package com.picostuff.readingcircle;


import java.io.BufferedReader;
import java.io.InputStream; 
import java.io.InputStreamReader;
import java.util.List;

import javax.ws.rs.Consumes; 
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.picostuff.readingcircle.repo.Item;

@Path("/")
public class ReadingCircleRESTService {
	
	public ReadingCircleRESTService() {
		System.out.println("Reading Circle Version 0.1.6");
	}
	@POST
	@Path("/echo") @Consumes(MediaType.APPLICATION_JSON)
	public Response echo(InputStream incomingData) {
		ReadingCircle sdk = new ReadingCircle();
		String incoming = getIncoming(incomingData);
        // return HTTP response 200 in case of success
		return Response.status(200).entity(sdk.echo(incoming)).type("application/json").build(); 
	}

	@GET
	@Path("/msg")
	public Response msg() {
		ReadingCircle sdk = new ReadingCircle();
        // return HTTP response 200 in case of success
		return Response.status(200).entity("{\"msg\":\"hello there\"}").type("application/json").build(); 
	}

	@POST
	@Path("/login") @Consumes(MediaType.APPLICATION_JSON)
	public Response login(InputStream incomingData) {
		ReadingCircle sdk = new ReadingCircle();
		String incoming = getIncoming(incomingData);
		JSONObject response = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject(incoming);
			String name = jsonObject.getString("name");
			System.out.println("Checking for reader " + name);
			if (!sdk.readerExists(name)) {
				System.out.println("Reader " + name + " doesn't exist");
				response.put("error", "reader does not exist");
			}
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // return HTTP response 200 in case of success
		return Response.status(200).entity(response.toString()).type("application/json").build(); 
	}

	@GET
	@Path("/collection")
	public Response collection() {
		System.out.println("collection called");
		ReadingCircle sdk = new ReadingCircle();
		List<Item> items = sdk.getCollection();
		JSONArray response = putInJsonArray(items);
        // return HTTP response 200 in case of success
		return Response.status(200).entity(response.toString()).type("application/json").build(); 
	}

	@GET
	@Path("/reading")
	public Response reading(@QueryParam("reader") String reader) {
		System.out.println("reading called with reader " + reader);
		ReadingCircle sdk = new ReadingCircle();
		List<Item> items = sdk.getCurrentlyReading(reader);
		JSONArray response = putInJsonArray(items);
        // return HTTP response 200 in case of success
		return Response.status(200).entity(response.toString()).type("application/json").build(); 
	}

	@GET
	@Path("/available")
	public Response available(@QueryParam("reader") String reader) {
		System.out.println("available called with reader " + reader);
		ReadingCircle sdk = new ReadingCircle();
		List<Item> items = sdk.getAtMyDesk(reader);
		JSONArray response = putInJsonArray(items);
        // return HTTP response 200 in case of success
		return Response.status(200).entity(response.toString()).type("application/json").build(); 
	}

	@GET
	@Path("/unknown")
	public Response unknown() {
		System.out.println("collection called");
		ReadingCircle sdk = new ReadingCircle();
		List<Item> items = sdk.getUnknowns();
		JSONArray response = putInJsonArray(items);
        // return HTTP response 200 in case of success
		return Response.status(200).entity(response.toString()).type("application/json").build(); 
	}

	@POST
	@Path("/checkin") @Consumes(MediaType.APPLICATION_JSON)
	public Response checkin(InputStream incomingData) {
		ReadingCircle sdk = new ReadingCircle();
		String incoming = getIncoming(incomingData);
		JSONObject response = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject(incoming);
			String name = jsonObject.getString("name");
			System.out.println("Checkin item " + name);
			try {
				sdk.checkinItem(name);
			} catch (Exception e) {
				response.put("error", e.getMessage());
			}
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // return HTTP response 200 in case of success
		return Response.status(200).entity(response.toString()).type("application/json").build(); 
	}

	@POST
	@Path("/checkout") @Consumes(MediaType.APPLICATION_JSON)
	public Response checkout(InputStream incomingData) {
		ReadingCircle sdk = new ReadingCircle();
		String incoming = getIncoming(incomingData);
		JSONObject response = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject(incoming);
			String name = jsonObject.getString("name");
			String reader = jsonObject.getString("reader");
			System.out.println("Checkout item " + name);
			try {
				sdk.checkoutItem(name, reader);
			} catch (Exception e) {
				response.put("error", e.getMessage());
			}
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // return HTTP response 200 in case of success
		return Response.status(200).entity(response.toString()).type("application/json").build(); 
	}

	@POST
	@Path("/markAtMyDesk") @Consumes(MediaType.APPLICATION_JSON)
	public Response markAtMyDesk(InputStream incomingData) {
		ReadingCircle sdk = new ReadingCircle();
		String incoming = getIncoming(incomingData);
		JSONObject response = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject(incoming);
			String name = jsonObject.getString("name");
			String reader = jsonObject.getString("reader");
			System.out.println("Mark item " + name + " at my desk");
			try {
				sdk.markItemAtMyDesk(name, reader);
			} catch (Exception e) {
				response.put("error", e.getMessage());
			}
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // return HTTP response 200 in case of success
		return Response.status(200).entity(response.toString()).type("application/json").build(); 
	}

	@POST
	@Path("/markUnknown") @Consumes(MediaType.APPLICATION_JSON)
	public Response markUnknown(InputStream incomingData) {
		ReadingCircle sdk = new ReadingCircle();
		String incoming = getIncoming(incomingData);
		JSONObject response = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject(incoming);
			String name = jsonObject.getString("name");
			System.out.println("Mark item " + name + " unknown");
			try {
				sdk.markItemUnknown(name);
			} catch (Exception e) {
				response.put("error", e.getMessage());
			}
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // return HTTP response 200 in case of success
		return Response.status(200).entity(response.toString()).type("application/json").build(); 
	}

	private JSONArray putInJsonArray(List<Item> items) {
		JSONArray response = new JSONArray();
		System.out.println("num items in collection: " + items.size());
		for (Item item:items) {
			try {
				JSONObject itemObject = new JSONObject();
				itemObject.put("name", item.getName());
				itemObject.put("title", item.getTitle());
				itemObject.put("reader", item.getReader());
				itemObject.put("status", item.getStatus());
				response.put(itemObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	private String getIncoming(InputStream incomingData) {
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				builder.append(line);
			}
		} catch (Exception e) { 
			System.out.println("Error Parsing: - ");
		}
		String incoming = builder.toString();
        System.out.println("Data Received: " + incoming);
		return incoming;
	}
	
}
