package com.why3.ws.data;

/**
 * DynamoDBSinHash Class
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
import com.why3.ws.data.SimpleDBSinHash.SinHash;

public class DynamoDBSinHash {
	//protected final static String AWSAccessKeyID = "AKIAJ6CCAW5FIIWP3WZA";
	//protected final static String AWSSecretKey = "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";

	static Logger log = Logger.getLogger(
			DynamoDBSinHash.class.getName());
	
	private static String domName = "WHY3sinhash";
	
	private AmazonDynamoDBClient client = null;
	
	/**
	 * DynamoDBSinHash Constructor
	 * 
	 * @throws IOException
	 */
	public DynamoDBSinHash() throws IOException {
		AWSCredentials credentials = new PropertiesCredentials(
				DynamoDBSequent.class
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
		
		//Adding Items to the Domain
		logMessage("Puting items into table " + domName);
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		try {
			//Unique Identifier ----> HashCode
			int append = String.valueOf(System.currentTimeMillis()).hashCode();
			String hashID = "sinhash"+HashThread.makeMD5Hash(String.valueOf(append));
			
			String thIndex = "thIndex"+DynamoDBResult.getAppend();
	
			item.put("sinhashIndex", new AttributeValue().withS(hashID));
			item.put("theoryIndex", new AttributeValue().withS(thIndex)); //CHECK
			item.put("sinhash", new AttributeValue().withS(new SinHash().hashString("sinhash")));
			
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
			
			DynamoDBSinHash dbClient = new DynamoDBSinHash();
			(dbClient).putItems();
			
		}catch(ResourceNotFoundException rnfe){
			System.out.println("ResourceNotFoundException: " );
			rnfe.printStackTrace();}
		catch(IOException ioe){
			System.out.println("IOException: " );
			ioe.printStackTrace();}
	}
}
