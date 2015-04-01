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

import com.xerox.amazonws.sdb.Item;
import com.xerox.amazonws.sdb.ItemAttribute;
import com.xerox.amazonws.sdb.SDBException;

public class SimpleDBAltergo extends DomainCall{
	
	static Logger log = Logger.getLogger(
            SimpleDBSinHash.class.getName());
	
	private static String domName = "WHY3altergo";
	
	public static String toolName; //For Testing
	public static int index;
	public static String version;
	public static String toolPath;
	public static String toolRes;
	
	public static void main(String[] args) {
		//DomainCall.authenticate();
		//DomainCall.createDomain(domName);	
		
		Item item;
		List<ItemAttribute> list;
		
		try {
					int toolIndex = 0;
		    		toolName = "altergo"; 
    				index = toolIndex; 
		    		version = "0.95.2"; 
		    		toolPath = "www.ncl.ac.uk/altergo"; 
		    		toolRes = "Valid"; //TO BE Changed
		    		
			//Unique Identifier ----> HashCode
		    String identifier = "tool"+String.valueOf(System.currentTimeMillis()).hashCode();
			
			//Adding Items to the Domain
					System.out.println("[INFO]-------> Adding Items to "+domName+"....");
			DomainCall.domain = DomainCall.sdb.getDomain(domName);
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
}