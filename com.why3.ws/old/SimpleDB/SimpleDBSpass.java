package com.why3.ws.data;

/**
* Simple DB Load Data
* Uses Typica Library to load sample data into Domain
* 
* @author David Ebo Adjepon-Yamoah
*
*/

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

//import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
//import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.xerox.amazonws.sdb.Domain;
import com.xerox.amazonws.sdb.Item;
import com.xerox.amazonws.sdb.ItemAttribute;
import com.xerox.amazonws.sdb.SDBException;
import com.xerox.amazonws.sdb.SimpleDB;

public class SimpleDBSpass{
	
	protected final static String AWSAccessKeyID = "AKIAJ6CCAW5FIIWP3WZA";
	protected final static String AWSSecretKey = "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";

	static Domain domain;
	
	static Logger log = Logger.getLogger(
            SimpleDBSinHash.class.getName());
		
	static SimpleDB sdb = new SimpleDB(AWSAccessKeyID,AWSSecretKey);
	
	public static String toolName; //For Testing
	public static int index;
	public static String version;
	public static String toolPath;
	public static String toolRes;
	
	
	public static void main(String[] args) {
		//createDomain();		
		Item item;
		List<ItemAttribute> list;
		
		try {
					int toolIndex = 2;
			
					toolName = "spass"; 
		    		index = toolIndex; 
		    		version = "3.7"; 
		    		toolPath = "www.ncl.ac.uk/spass"; 
		    		toolRes = "Valid";
		    		
		   
			//Unique Identifier ----> HashCode
		    String identifier = "tool"+String.valueOf(System.currentTimeMillis()).hashCode();
			
			//Adding Items to the Domain
					System.out.println("[INFO]-------> Adding Items to WHY3spass....");
			domain = sdb.getDomain("WHY3spass");
			item = domain.getItem(identifier);
			list = new ArrayList<ItemAttribute>();
			list.add(new ItemAttribute("toolIndex", String.valueOf(index), false));
					//System.out.println("		Index: "+String.valueOf(index));
			list.add(new ItemAttribute("toolName", toolName, false));
					//System.out.println("		Tool: "+toolName);
			list.add(new ItemAttribute("toolVersion", version, false));
					//System.out.println("		Tool Version: "+version);
			list.add(new ItemAttribute("toolPath", toolPath, false));
					//System.out.println("		ToolPath: "+toolPath);
			item.putAttributes(list);
			
			System.out.print("							[DONE]\n");
			
		} catch (SDBException ex) {
			System.err.println("Error Message : "+ex.getMessage());
			System.err.println("RequestID : "+ex.getRequestId());
		} catch (NullPointerException npe){
			System.err.println("Error Message : \n");
			npe.printStackTrace();
			}
	}
	
	// List the Domain(s)
	public void listDomain(){
		System.out.println("[INFO]-------> List Domains:");
		com.xerox.amazonws.sdb.ListDomainsResult domainsResult;
		try {
			domainsResult = sdb
									.listDomains();
			
			List<Domain> domains = domainsResult.getDomainList();
			int i = 1;
			for (Domain dom : domains) {
				
				System.out.println("   Domain ("+i+"): " + dom.getName());
				i++;
			}
		} catch (SDBException e) {
			e.printStackTrace();
		}
		
	}
	
	// Creating the Domain
	public static void createDomain(){
		
			try {
				domain = sdb.createDomain("WHY3spass");
			} catch (SDBException e1) {
				e1.printStackTrace();
			}					
				try {
					System.out.println("[INFO]-------> Waiting for domain WHY3spass to be created [5 seconds]...");
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
								
		System.out.println("[INFO]-------> WHY3spass is created");			
	}

	
}
