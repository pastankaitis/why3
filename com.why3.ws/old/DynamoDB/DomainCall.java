
package com.why3.ws.data;

/**
 * DomainCall Class
 * 
 * @author David Ebo Adjepon-Yamoah
 */

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.TableStatus;
import com.xerox.amazonws.sdb.Domain;



public class DomainCall {

	String tableName = "";
	static Domain domain;
	
	static Logger log = Logger.getLogger(
			DomainCall.class.getName());
	
	private static AmazonDynamoDBClient client = null;
	
	//protected final static String AWSAccessKeyID = "AKIAJ6CCAW5FIIWP3WZA";
	//protected final static String AWSSecretKey = "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";

	
	/**
	 * Constructor
	 */
	public DomainCall(){
		authenticate();
		createTable();
		waitForTableAvaliability(tableName);
	}
	
	public void authenticate() {
				
		AWSCredentials credentials;
		try {
			credentials = new PropertiesCredentials(
					DynamoDBSequent.class
						.getResourceAsStream("AwsCredentials.properties"));

			client = new AmazonDynamoDBClient(credentials);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
	 * Create Table
	 */
	public void createTable(){
		logMessage("Creating table" + tableName);
		ArrayList<AttributeDefinition> attribDefs = new ArrayList<AttributeDefinition>();
		attribDefs.add(new AttributeDefinition().withAttributeName("Id").withAttributeType("N"));
		
		ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
		ks.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));
		
		ProvisionedThroughput provtp = new ProvisionedThroughput()
				.withReadCapacityUnits(10L).withWriteCapacityUnits(10L);
		
		CreateTableRequest request = new CreateTableRequest()
			.withTableName(tableName)
			.withAttributeDefinitions(attribDefs)
			.withKeySchema(ks)
			.withProvisionedThroughput(provtp);
		
		CreateTableResult result = client.createTable(request);
		logMessage("Creating table " 
				+ result.getTableDescription().getTableName());
		
	}
	
	/**
	 * Waits for Table to become Available 
	 * 
	 * @param tableName
	 */
	public void waitForTableAvaliability(String tableName) {
		System.out.println("Waiting for " + tableName + " to become ACTIVE...");
		long startTime = System.currentTimeMillis();
		long endTime = startTime + (10 * 60 * 1000);
		
		while (System.currentTimeMillis() < endTime) {
			try {Thread.sleep(1000 * 20);} catch (Exception e) {}
			try {
				DescribeTableRequest request = new DescribeTableRequest().withTableName(tableName);
				TableDescription tableDescription = client.describeTable(request).getTable();
				String tableStatus = tableDescription.getTableStatus();
				System.out.println(" - current state: " + tableStatus);
				if (tableStatus.equals(TableStatus.ACTIVE.toString())) return;
			} catch (AmazonServiceException ase) {
				if (ase.getErrorCode().equalsIgnoreCase("ResourceNotFoundException") == false) throw ase;
			}
		}
		throw new RuntimeException("Table " + tableName + " never went active");
	}
	
}
