/**
 * 
 */
package uk.ac.ncl.eventb.why3.cloud;

import java.io.IOException;
import java.security.KeyStoreException;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.eventb.core.seqprover.IProofMonitor;

import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.main.Why3CallResult.WHY3_RESULT;
import uk.ac.ncl.eventb.why3.main.Why3Plugin;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class CloudClient {
	private String cloudURI;
	
	public CloudClient(String cloudURI) {
		this.cloudURI = cloudURI;
	}

	/**
	 * Submits a why3 theory for verification on cloud
	 * @param model a why model
	 * @param timeout timeout, in seconds
	 * @throws JSONException
	 * @throws IOException
	 * @throws KeyStoreException
	 */
	public Why3CallResult submit(TheoremTranslated model, String option, long timeout, IProofMonitor monitor) throws JSONException, IOException, KeyStoreException {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		//client.addFilter(new LoggingFilter());

		if (Why3Plugin.DEBUG)
			System.out.println("### UUID: " + Long.toHexString(Why3Plugin.getUUID()) + " (" + Why3Plugin.getUUIDStrength() + " bits)");
		
		WebResource service = client.resource(cloudURI);
		JSONObject dataFile = new JSONObject();
		
		dataFile.put("uuid", Why3Plugin.getUUID()); 
		dataFile.put("timeout", timeout); 
		dataFile.put("scenario", option); 
		dataFile.put("types", model.getTypes());
		dataFile.put("definitions", model.getDefinitions());
		dataFile.put("hypothesis", model.getHypothesis());
		dataFile.put("goal", model.getGoal());
		ClientResponse response = service.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, dataFile);
		JSONObject jsonOutput = response.getEntity(JSONObject.class);
		if (jsonOutput.has("error")) {
			return Why3CallResult.INSTANCE_FAILURE;
		} 		
		
		String status = jsonOutput.getString("status");
		String tool = jsonOutput.getString("tool");
		long time = Long.valueOf(jsonOutput.getString("time"));
		Why3CallResult result = new Why3CallResult(extractStatus(status), tool, time);
		result.setLocal(false);
		
		return result; 
	}


	private WHY3_RESULT extractStatus(String output) {
		return WHY3_RESULT.fromString(output);
	}
}
