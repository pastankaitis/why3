package com.why3.ws.data;

/**
 * DynamoDBTool Class
 * 
 * @author David Ebo Adjepon-Yamoah
 */


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.why3.ws.HashThread;
import com.why3.ws.connector.Why3CallResult;

public class DynamoDBTool {
	//protected final static String AWSAccessKeyID = "AKIAJ6CCAW5FIIWP3WZA";
	//protected final static String AWSSecretKey = "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";

	static Logger log = Logger.getLogger(
			DynamoDBSinHash.class.getName());
	
	private static String domName = "WHY3tool";
	
	private AmazonDynamoDBClient client = null;
	
	public static Why3CallResult res = new Why3CallResult(null, "", 0); //CHECK
	public static String result = String.valueOf(res.getStatus());		//CHECK
	public static String tool = res.getTool();							//CHECK
	
	private static String toolName;
	public static int index;
	private static String version;
	private static String toolPath;
	private static String toolRes;
	
	public DynamoDBTool() throws IOException {
		AWSCredentials credentials = new PropertiesCredentials(
				DynamoDBTool.class
					.getResourceAsStream("AwsCredentials.properties"));
		
		client = new AmazonDynamoDBClient(credentials);
		client.setRegion(Region.getRegion(Regions.EU_WEST_1));
	}
		
	/**
	 * Logging Method (using the org.apache.log4j.Logger class)
	 * 
	 * @param msg
	 */
	public void logMessage(String msg){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(new Date()) + " --> " + msg);
		log.info(sdf.format(new Date()) + " --> " + msg);
		log.debug(sdf.format(new Date()) + " --> " + msg);
	}
	
		
	
	/**
	 * Adding Items to the Domain
	 */
	public void putItems(){
			
		//Adding Items to the Table
		logMessage("Puting items into table " + domName);
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		System.out.println("[INFO]-------> Adding Items to "+domName+"....");
		logMessage("[INFO]-------> Adding Items to "+domName+"....");
		
		try {
			//Unique Identifier ----> HashCode
			int append = String.valueOf(System.currentTimeMillis()).hashCode();
			String hashID = "tool"+HashThread.makeMD5Hash(String.valueOf(append));
	
			item.put("toolIndex", new AttributeValue().withS(hashID));
			item.put("toolName", new AttributeValue().withS(toolName));
			item.put("toolVersion", new AttributeValue().withS(version));
			item.put("toolPath", new AttributeValue().withS(toolPath));
			item.put("result", new AttributeValue().withS(toolRes));
			
			PutItemRequest itemRequest = new PutItemRequest().withTableName(domName)
					.withItem(item);
			client.putItem(itemRequest);
			//item.clear();
			
			logMessage("Item "+hashID+" has been put into domain ("+domName+")");
		} catch (Exception e) {	e.printStackTrace(); }		
	} 
	
	
	/**
	 * Main Method
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		try{
			
			/*DomainCall table = new DomainCall();
			table.tableName = domName;
			table.createTable();
			table.waitForTableAvaliability(domName);*/
			
			DynamoDBSequent dbClient = new DynamoDBSequent();
			(dbClient).putItems();
			
		}catch(ResourceNotFoundException rnfe){
			System.out.println("ResourceNotFoundException: " );
			rnfe.printStackTrace();}
		catch(IOException ioe){
			System.out.println("IOException: " );
			ioe.printStackTrace();}
	}
}
