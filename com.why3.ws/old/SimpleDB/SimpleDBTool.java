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
import com.why3.ws.connector.Why3CallResult;
import com.xerox.amazonws.sdb.Domain;
import com.xerox.amazonws.sdb.Item;
import com.xerox.amazonws.sdb.ItemAttribute;
import com.xerox.amazonws.sdb.SDBException;
import com.xerox.amazonws.sdb.SimpleDB;

public class SimpleDBTool{
	
	static Logger log = Logger.getLogger(
            SimpleDBSinHash.class.getName());
	
	private static String domName = "WHY3tool";
		
	final static String AWSAccessKeyID = "AKIAJ6CCAW5FIIWP3WZA";
	final static String AWSSecretKey = "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";
	
	static SimpleDB sdb = new SimpleDB(AWSAccessKeyID,AWSSecretKey);
	static Domain domain;
	static long count = 1;
	static int domIndex = 1;
	
	public static Why3CallResult res = new Why3CallResult(null, "", 0);
	public static String result = String.valueOf(res.getStatus());
	public static String tool = res.getTool();
	
	private static String toolName; //For Testing
	public static int index;
	private static String version;
	private static String toolPath;
	private static String toolRes;
	
	public static void main(String[] args) {
		//createDomain(domName);
		try{
			if(count <= 100){
			
			if(tool.equalsIgnoreCase(null)) tool = res.getTool();	
				
			switch(tool)
				{ 
				    case "alt-ergo": toolName =  tool; index = 1; version = "0.95.2"; toolPath = "www.ncl.ac.uk/altergo"; toolRes = result;
				        break; 
				    case "spass": toolName = "spass"; index = 2; version = "3.7"; toolPath = "www.ncl.ac.uk/spass"; toolRes = result;
				        break;
				    case "z3": toolName = tool; index = 3; version = "1.1.1"; toolPath = "www.ncl.ac.uk/z3"; toolRes = result;
				        break; 
				    case "eprover": toolName = tool; index = 4; version = "1.3.7"; toolPath = "www.ncl.ac.uk/eprover"; toolRes = result;
				        break;
				    case "cvc3": toolName = tool; index = 5; version = "2.4.1"; toolPath = "www.ncl.ac.uk/cvc3"; toolRes = result;
				        break; 
				    case "cvc4": toolName = tool; index = 6; version = "1.1.3"; toolPath = "www.ncl.ac.uk/cvc4"; toolRes = result;
				        break;
				    case "metis": toolName = tool; index = 7; version = "2.3"; toolPath = "www.ncl.ac.uk/metis"; toolRes = result;
				        break; 
				    case "gappa": toolName = tool; index = 8; version = "1.0.0"; toolPath = "www.ncl.ac.uk/gappa"; toolRes = result;
				        break;
				    case "yices": toolName = tool; index = 9; version = "1.0.38"; toolPath = "www.ncl.ac.uk/yices"; toolRes = result;
				        break;
				    default: toolName = "tooltest"; index = 9; version = "1.0.38"; toolPath = "www.ncl.ac.uk/yices"; toolRes = result;
				        log.info("Tool does not exist!");
				        break;    
				}
				
	    		addItems();
			    
	    		count++;
			}else {
							
				count = 1;
				domName = "WHY3tool";
				createDomain(domName+domIndex);
			    
				if(tool.equalsIgnoreCase(null)) tool = res.getTool();
				
				switch(tool)
				{ 
				    case "alt-ergo": toolName =  tool; index = 1; version = "0.95.2"; toolPath = "www.ncl.ac.uk/altergo"; toolRes = result;
				        break; 
				    case "spass": toolName = "spass"; index = 2; version = "3.7"; toolPath = "www.ncl.ac.uk/spass"; toolRes = result;
				        break;
				    case "z3": toolName = tool; index = 3; version = "1.1.1"; toolPath = "www.ncl.ac.uk/z3"; toolRes = result;
				        break; 
				    case "eprover": toolName = tool; index = 4; version = "1.3.7"; toolPath = "www.ncl.ac.uk/eprover"; toolRes = result;
				        break;
				    case "cvc3": toolName = tool; index = 5; version = "2.4.1"; toolPath = "www.ncl.ac.uk/cvc3"; toolRes = result;
				        break; 
				    case "cvc4": toolName = tool; index = 6; version = "1.1.3"; toolPath = "www.ncl.ac.uk/cvc4"; toolRes = result;
				        break;
				    case "metis": toolName = tool; index = 7; version = "2.3"; toolPath = "www.ncl.ac.uk/metis"; toolRes = result;
				        break; 
				    case "gappa": toolName = tool; index = 8; version = "1.0.0"; toolPath = "www.ncl.ac.uk/gappa"; toolRes = result;
				        break;
				    case "yices": toolName = tool; index = 9; version = "1.0.38"; toolPath = "www.ncl.ac.uk/yices"; toolRes = result;
				        break;
				    default: toolName = "tooltest"; index = 9; version = "1.0.38"; toolPath = "www.ncl.ac.uk/yices"; toolRes = result;
				        log.info("Tool does not exist!");
				        break;  
				} 
				
	    		domName = "WHY3tool";
				domName = domName+domIndex;
	    		addItems();
				
	    		domIndex++;
				count++;
			}
		}
		catch(Exception exp){exp.printStackTrace();}
	}
	
	/**
	 * Adding Items to the Domain
	 */
	public static void addItems(){
		Item item;
		List<ItemAttribute> list;
		
		 try{	
			//Unique Identifier ----> HashCode
		    String identifier = "tool"+String.valueOf(System.currentTimeMillis()).hashCode();
			
			System.out.println("[INFO]-------> Adding Items to "+domName+"....");
			log.info("[INFO]-------> Adding Items to "+domName+"....");
					
			domain = sdb.getDomain(domName);
			item = domain.getItem(identifier);
			list = new ArrayList<ItemAttribute>();
			list.add(new ItemAttribute("toolIndex", String.valueOf(index), false));
			list.add(new ItemAttribute("toolName", toolName, false));
			list.add(new ItemAttribute("toolVersion", version, false));
			list.add(new ItemAttribute("toolPath", toolPath, false));
			list.add(new ItemAttribute("result", toolRes, false));
			item.putAttributes(list);
			
			System.out.print("							[DONE]\n");
			log.info("							[DONE]\n");
		} catch (SDBException ex) {
			System.err.println("Error Message : "+ex.getMessage());
			System.err.println("RequestID : "+ex.getRequestId());
		} catch (NullPointerException npe){
			System.err.println("Error Message : \n");
			npe.printStackTrace();
			}
		}
		
	/**
	 * Create Domain
	 * 
	 * @param domName
	 */
	public static void createDomain(String domName){
		try {
			domain = sdb.createDomain(domName);
		} catch (SDBException e1) {
			e1.printStackTrace();
		}					
			try {
				System.out.println("[INFO]-------> Waiting for domain "+domName+" to be created [5 seconds]...");
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
							
	System.out.println("[INFO]-------> "+domName+" is created");	
	}
	
	//TEST
	public static String toolChoice(){
		int num = 1 + (int)(Math.random()*9); 
		
		switch(num){
		case 1: toolName = "alt-ergo"; break;
		case 2: toolName = "spass"; break;
		case 3: toolName = "z3"; break;
		case 4: toolName = "eprover"; break;
		case 5: toolName = "cvc3"; break;
		case 6: toolName = "cvc4"; break;
		case 7: toolName = "metis"; break;
		case 8: toolName = "gappa"; break;
		case 9: toolName = "yices"; break;
		}
		
		return toolName;
	}
}