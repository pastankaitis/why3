package com.why3.ws.data;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;


public class DynamoDBTest3 {
	private String tableName = "ProductCat";
	private AmazonDynamoDBClient client = null;
	
	static Logger log = Logger.getLogger(
            DynamoDBTest3.class.getName());
	
	public DynamoDBTest3() throws IOException {
		AWSCredentials credentials = new PropertiesCredentials(
				DynamoDBTest3.class
					.getResourceAsStream("AwsCredentials.properties"));
		
		client = new AmazonDynamoDBClient(credentials);
	}
	
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
	
	public static void logMessage(String msg){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(new Date()) + " --> " + msg);
	}
	
	public void putItems(){
		logMessage("Puting items into table " + tableName);
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		// Add bikes.
		item.put("Id", new AttributeValue().withN("201"));
		item.put("Title", new AttributeValue().withS("18-Bike-201"));
		item.put("Descrption", new AttributeValue().withS("201 Description"));
		item.put("BicycleType", new AttributeValue().withS("Road"));
		item.put("Brand", new AttributeValue().withS("Mountain A"));
		
		item.put("Price", new AttributeValue().withN("100"));
		item.put("Gender", new AttributeValue().withS("M"));
		item.put("Colour", new AttributeValue().withSS(Arrays.asList("Red", "Black")));
		item.put("ProductCategory", new AttributeValue().withS("Bicycle"));
		
		PutItemRequest itemRequest = new PutItemRequest().withTableName(tableName)
				.withItem(item);
		client.putItem(itemRequest);
		//item.clear();
	} 
	
	public static void main(String[] args){
		try{
			DynamoDBTest3 dbClient = new DynamoDBTest3();
			
			dbClient.createTable();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				System.out.println("InterruptedException: " );
				e.printStackTrace();
			}
			
			(dbClient).putItems();
		}catch(ResourceNotFoundException rnfe){
			System.out.println("ResourceNotFoundException: " );
			rnfe.printStackTrace();}
		catch(IOException ioe){
			System.out.println("IOException: " );
			ioe.printStackTrace();}
	}
	
	
//	public String getTableStatus(){
//		TableDescription tableDesc = client.describeTable(
//				new DescribeTableRequest().withTableName(tableName).getTableName());
//		return tableDesc.getTableStatus();
//		
//	}
	
//	public void describeTable(){
//		logMessage("Describing table " + tableName);
//		TableDescription tableDesc = client.describeTable(
//				new DescribeTableRequest().withTableName(tableName).getTableName());
//		String desc = String.format("%s: %s \t ReadCapacityUnits: %d \t WriteCapacityUnits: %d", 
//				tableDesc.getTableStatus(), tableDesc.getTableName(), tableDesc.getProvisionedThroughput().getReadCapacityUnits(),
//				tableDesc.getProvisionedThroughput().getWriteCapacityUnits());
//	}
	
//	public void updateTable(){
//		logMessage("Updating table " + tableName);
//		TableDescription tableDesc;
//
//		ProvisionedThroughput provtp = new ProvisionedThroughput()
//				.withReadCapacityUnits(5L).withWriteCapacityUnits(5L);
//		
//		UpdateTableResult result = new UpdateTableResult()
//			.withTableName(tableName);
		
//	}

}
