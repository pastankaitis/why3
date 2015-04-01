package com.why3.ws.data;

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
import com.why3.ws.Verifier;
import com.xerox.amazonws.sdb.Domain;

public class DynamoDBSequent {
	//protected final static String AWSAccessKeyID = "AKIAJ6CCAW5FIIWP3WZA";
	//protected final static String AWSSecretKey = "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";

	static Domain domain;	
	private static String domName = "WHY3sequent";
	
	static Logger log = Logger.getLogger(
			DynamoDBSequent.class.getName());
	
	private AmazonDynamoDBClient client = null;
	
	public DynamoDBSequent() throws IOException {
		AWSCredentials credentials = new PropertiesCredentials(
				DynamoDBSequent.class
					.getResourceAsStream("AwsCredentials.properties"));
		
		client = new AmazonDynamoDBClient(credentials);
		client.setRegion(Region.getRegion(Regions.EU_WEST_1));
	}
		
	/**
	 * Logging Method
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
		
		//Unique Identifier ----> HashCode
		String identifier = "sequent"+String.valueOf(System.currentTimeMillis()).hashCode();
		
		//Adding Items to the Domain
		logMessage("Puting items into table " + domName);
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		try {
			String hashID = HashThread.makeMD5Hash(identifier);
	
		item.put("sequentIndex", new AttributeValue().withS(hashID));
		item.put("sequent", new AttributeValue().withS("sequent")); //CHECK
		item.put("sequentDate", new AttributeValue().withS(String.valueOf(Verifier.getDate(Verifier.start, "yyyy-MM-dd HH:mm:ss.S"))));
		item.put("scenario", new AttributeValue().withS(Verifier.getScenario()));
		
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
