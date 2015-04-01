package com.why3.ws.data;

/**
* Simple DB Load Data
* Uses Typica Library to load sample data into Domain
* 
* @author David Ebo Adjepon-Yamoah
*
*/

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.why3.ws.HashThread;
import com.why3.ws.Verifier;
import com.why3.ws.connector.Why3CallResult;
import com.xerox.amazonws.sdb.Domain;
import com.xerox.amazonws.sdb.Item;
import com.xerox.amazonws.sdb.ItemAttribute;
import com.xerox.amazonws.sdb.ListDomainsResult;
import com.xerox.amazonws.sdb.SDBException;
import com.xerox.amazonws.sdb.SimpleDB;

public class SimpleDBResult {
	
	protected final static String AWSAccessKeyID = "AKIAJ6CCAW5FIIWP3WZA";
	protected final static String AWSSecretKey = "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";
	
	private static Domain domain;
	private static long count = 1;
	private static int domIndex = 1;
	private static int append = String.valueOf(System.currentTimeMillis()).hashCode();
	
	private static String domName = "WHY3result";
		
	static Logger log = Logger.getLogger(
            SimpleDBResult.class.getName());
	
	static SimpleDB sdb = new SimpleDB(AWSAccessKeyID,AWSSecretKey);
	
	
	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//createDomain(domName);
		
		
		//Since SimpleDB has a very small capacity per domain,
		//we conducted some test and are confident that on the average
		//100 entries will be an optimal number before a new domain
		//is created and populated
		if(count <= 100){
			
		addItems();
		
		count++;
		
		}else {
			count = 1;
			
			domName = "WHY3result";
			createDomain(domName+domIndex);
	
    		domName = "WHY3result";
			domName = domName+domIndex;
    		addItems();
			
    		domIndex++;
			count++;
			
		}
	}
	
	/**
	 * List the Domain(s)
	 */
	public static void listDomain(){ 
		System.out.println("[INFO]-------> List Domains in WHY3result:");	
		ListDomainsResult domainsResult;
		try {
			domainsResult = sdb.listDomains();
			
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
	
	/**
	 * Adding Items to the Domain
	 */
	public static void addItems(){
		
		Item item;
		List<ItemAttribute> list;
		

		try {
			
			//Unique Identifier ----> HashCode
			String identifier = "result"+String.valueOf(System.currentTimeMillis()).hashCode();
			String thIndex = "thIndex"+getAppend();
			
			Why3CallResult res = new Why3CallResult(null, "", 0);
			String result = String.valueOf(res.getStatus());    //<----------[CHECK]
			String index = String.valueOf(SimpleDBTool.index);
			String time = String.valueOf(res.getTime());
			
			if((time.equalsIgnoreCase(null)) || (time.equals("0"))) time = String.valueOf(Verifier.time);
			
			//Adding Items to the Domain
			System.out.println("[INFO]-------> Adding Items to "+domName+"....");
			log.info("[INFO]-------> Adding Items to "+domName+"....");
			domain = sdb.getDomain(domName);
			item = domain.getItem(identifier);
			list = new ArrayList<ItemAttribute>();
			list.add(new ItemAttribute("resultIndex", HashThread.makeMD5Hash(identifier) , false)); 
			list.add(new ItemAttribute("theoryIndex", thIndex, false));
			list.add(new ItemAttribute("toolIndex", index , false)); 
			list.add(new ItemAttribute("result", result , false));
			list.add(new ItemAttribute("time", time , false)); 
			item.putAttributes(list);
			
			log.debug(domain);
			log.info(domain);
			System.out.print("							[DONE]\n");
		} catch (SDBException ex) {
			System.err.println("Error Message : "+ex.getMessage());
			System.err.println("RequestID : "+ex.getRequestId());
		} catch (NullPointerException npe){
			System.err.println("Error Message : \n");
			npe.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
		
	public static int getAppend(){
		return append;
	}
}
