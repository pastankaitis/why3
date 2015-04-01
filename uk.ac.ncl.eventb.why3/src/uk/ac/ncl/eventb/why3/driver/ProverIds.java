package uk.ac.ncl.eventb.why3.driver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import uk.ac.ncl.eventb.why3.main.Why3Plugin;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class ProverIds {
	private static final List<String> possibleIds =  Arrays.asList("cvc3", "cvc4", "z3", "eprover", "verit", "alt-ergo", "iprover", "vampire", "yices", "spass", "simplify");
	
	public static boolean isPossibleId(String proverId) {
		return possibleIds.contains(proverId);
	}
	
	public static boolean isValidLocalId(String proverId) {
		if (Why3Plugin.hasWhy3LocalPath()) {
			List<Why3ToolInfo> toolInfo = Why3ProverList.getToolList();
			if (toolInfo != null) {
				for(Why3ToolInfo ti: toolInfo) {
					if (ti.getName().equals(proverId))
						return true;
				}
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isValidRemoteId(String proverId) {
		for (Why3ToolInfo ti : getRemoteIds()) {
			if (ti.getName().equals(proverId))
				return true;
		}
		return false;
	}
	
	public static List<String> getPossibleIds() {
		return possibleIds;
	}
	
	public static List<Why3ToolInfo> getLocalIds() {
		return Why3ProverList.getToolList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Why3ToolInfo> getRemoteIds() {
		if (Why3Plugin.hasWhy3CloudPath()) {
			if (System.currentTimeMillis() - timeCached >= CACHE_TIME) {
				try {
					queryServerProverList();
				} catch (Throwable e) {
					e.printStackTrace();
					cachedRemoteIds = null;
				} finally {
					timeCached = System.currentTimeMillis();
				}
			}
			
			// if not failed
			if (cachedRemoteIds != null) {
				return cachedRemoteIds;
			}
		}
		return java.util.Collections.EMPTY_LIST;
	}

	private static List<Why3ToolInfo> cachedRemoteIds = null;
	private static long timeCached = 0;
	private static final long CACHE_TIME = 3600000; // one hour in milliseconds
	
	private synchronized static void queryServerProverList() throws JSONException {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		WebResource service = client.resource(Why3Plugin.getCloudURI("listprovers"));
		ClientResponse response = service.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class);
		if (response.getStatus() == 200 && response.hasEntity()) {
			JSONObject jsonOutput = response.getEntity(JSONObject.class);
			if (jsonOutput.has("error")) {
				System.err.println("queryServerProverList:"
						+ jsonOutput.getString("error"));
				return;
			}

			if (cachedRemoteIds != null)
				cachedRemoteIds.clear();
			else
				cachedRemoteIds = new ArrayList<Why3ToolInfo>(20);

			Iterator<String> iterator = (Iterator<String>) jsonOutput.keys();
			while (iterator.hasNext()) {
				String key = iterator.next();
				Why3ToolInfo info = new Why3ToolInfo(key,
						jsonOutput.getString(key));
				cachedRemoteIds.add(info);
			}

			
		} else {
			System.err.println("queryServerProverList: " + response.toString());
		}
	}
}
