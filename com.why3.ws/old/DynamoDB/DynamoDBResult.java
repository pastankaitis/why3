package com.why3.ws.data;

/**
 * DynamoDBResult Class
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

public class DynamoDBResult {
	//protected final static String AWSAccessKeyID = "AKIAJ6CCAW5FIIWP3WZA";
	//protected final static String AWSSecretKey = "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";
	
	private static int append = String.valueOf(System.currentTimeMillis()).hashCode();
	
	private static String domName = "WHY3result";
		
	private AmazonDynamoDBClient client = null;
	
	static Logger log = Logger.getLogger(
            DynamoDBResult.class.getName());
	
	/**
	 * Constructor
	 * 
	 * @throws IOException
	 */
	public DynamoDBResult() throws IOException {
		AWSCredentials credentials = new PropertiesCredentials(
				DynamoDBTest3.class
					.getResourceAsStream("AwsCredentials.properties"));
		
		client = new AmazonDynamoDBClient(credentials);
		client.setRegion(Region.getRegion(Regions.EU_WEST_1));
	}
	
		
	/**
	 * Logging Method (using the org.apache.log4j.Logger class)
	 * 
	 * @param msg
	 */
	public static void logMessage(String msg){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(new Date()) + " --> " + msg);
		log.info(sdf.format(new Date()) + " --> " + msg);
		log.debug(sdf.format(new Date()) + " --> " + msg);
	}
			
	
	/**
	 * Adding Items to the Domain
	 */
	public void putItems(){
		
		String identifier = "result"+String.valueOf(System.currentTimeMillis()).hashCode();
		String thIndex = "thIndex"+getAppend();
		
		Why3CallResult res = new Why3CallResult(null, "", 0); 	//<----------[CHECK]
		String result = String.valueOf(res.getStatus());    	//<----------[CHECK]
		
		String index = String.valueOf(SimpleDBTool.index);    // CHANGE --> SimpleDBTool
		String time = String.valueOf(res.getTime());
		
		logMessage("Puting items into table " + domName);
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		try {
			String hashID = HashThread.makeMD5Hash(identifier);
	
		item.put("resultIndex", new AttributeValue().withS(hashID));
		item.put("theoryIndex", new AttributeValue().withS(thIndex));
		item.put("toolIndex", new AttributeValue().withS(index));
		item.put("result", new AttributeValue().withS(result));
		item.put("time", new AttributeValue().withN(time));
		//item.put("test", new AttributeValue().withSS(Arrays.asList("test1", "test2")));
		
		PutItemRequest itemRequest = new PutItemRequest().withTableName(domName)
				.withItem(item);
		client.putItem(itemRequest);
		//item.clear();
		
		logMessage("Item "+hashID+" with result ("+result+"), has been put into domain ("+domName+")");
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
			
			
			DynamoDBResult dbClient = new DynamoDBResult();	
			(dbClient).putItems();
		}catch(ResourceNotFoundException rnfe){
			System.out.println("ResourceNotFoundException: " );
			rnfe.printStackTrace();}
		catch(IOException ioe){
			System.out.println("IOException: " );
			ioe.printStackTrace();}
	}
		
	public static int getAppend(){
		return append;
	}
	
}

