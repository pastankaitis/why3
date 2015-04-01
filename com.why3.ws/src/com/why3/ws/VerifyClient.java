/**
 * 
 */
package com.why3.ws;

//import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStoreException;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;

public class VerifyClient {
	public static ClientResponse response;
	
private static void sendFileJSON() throws JSONException, IOException, KeyStoreException{
		
		ClientConfig config = new DefaultClientConfig();		
		Client client = Client.create(config);
		
		//client.addFilter(new LoggingFilter());
		
		WebResource service = client.resource("http://localhost:8080/com.why3.ws/rest/service/run"); //HOME
		//WebResource service = client.resource("http://10.14.128.225:8080/com.why3.ws/rest/service/run"); //OFFICE
			//WebResource service = client.resource("http://ec2-54-194-206-149.eu-west-1.compute.amazonaws.com/com.why3.ws/rest/service/run");
		//WebResource service = client.resource("http://why3env-kr2m59mcwi.elasticbeanstalk.com:8080/com.why3.ws/rest/service/run");
		//WebResource service = client.resource("http://why3.akofenadom.eu.cloudbees.net/rest/service/run"); //CLOUDBEES		
		
		JSONObject dataFile = new JSONObject();
		
		//WHY3 XML Format EXTRACTION
		String model = null;
		//File homedir = new File(System.getProperty("user.home"));
	    
		 FileInputStream inputStream = new FileInputStream("/home/ebo/why3-0.85/examples/f_puzzle.why");
		 //FileInputStream inputStream = new FileInputStream(homedir + "/tmp/eclipse/ws/com.why3.ws/src/com/why3/ws/f_puzzle.why");
		    try {
		        model = IOUtils.toString(inputStream);
		    } finally {
		        inputStream.close();
		    }
		    
			dataFile.put("definitions", model); 
			dataFile.put("hypothesis", model);
			dataFile.put("goal", ""); 
			dataFile.put("timeout", 20000); 
			dataFile.put("scenario", "fast"); //Default Scenario name
			
		response = service.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, dataFile);
		
		System.out.println("Status of File Transfer @ "+ System.currentTimeMillis()+ ": " + response.getStatus());
						
		client.destroy();
	}



	public static void main(String[] args) throws Exception, JSONException, IOException{
		
		sendFileJSON();
		
		String output = response.getEntity(String.class);
		System.out.println("Output: \n" + output);
		
	}
}
