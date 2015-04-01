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

import com.xerox.amazonws.sdb.Domain;
import com.xerox.amazonws.sdb.Item;
import com.xerox.amazonws.sdb.ItemAttribute;
import com.xerox.amazonws.sdb.SDBException;
import com.xerox.amazonws.sdb.SimpleDB;

public class SimpleDBSinHash {
	
	protected final static String AWSAccessKeyID = "AKIAJ6CCAW5FIIWP3WZA";
	protected final static String AWSSecretKey = "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";

	static Domain domain;
	private static long count = 1;
	private static int domIndex = 1;
	
	static Logger log = Logger.getLogger(
            SimpleDBSinHash.class.getName());
	
	private static String domName = "WHY3sinhash";
	
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
				domName = "WHY3sinhash";
				createDomain(domName+domIndex);
		
	    		domName = "WHY3sinhash";
				domName = domName+domIndex;
	    		addItems();
				
	    		domIndex++;
				count++;
			}
	}
	
	
	//SinHash 
	static class SinHash implements Function
	{
		public SinHash(){
			
		}
		
	    public int func(String str)
	    {
	        int hash = 1;
	        for(int i=0;i<str.length();i++)
	        {
	            hash *= (int)str.charAt(i);
	        }
	        hash= (int)(1000000000 * Math.abs(Math.sin(hash)));
	        return hash;
	    }
	    
	    public String hashString(String str){
	    	String hashStr;
	    	
	    	int hash = 1;
	        for(int i=0;i<str.length();i++)
	        {
	            hash *= (int)str.charAt(i);
	        }
	        hash= (int)(1000000000 * Math.abs(Math.sin(hash)));
	    	
	    	hashStr = String.valueOf(hash);
	    	
	    	return hashStr;
	    }
	}
	
	interface Function
	{
	    public int func(String str);
	}
	
	public void listDomain(){
		// List the Domain(s)
		System.out.println("[INFO]-------> List Domains in WHY3sinhash:");	
		com.xerox.amazonws.sdb.ListDomainsResult domainsResult;
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
			int append = String.valueOf(System.currentTimeMillis()).hashCode();
			String identifier = "sinhash"+append;
			String index = "index"+append;
			String thIndex = "thIndex"+SimpleDBResult.getAppend();
				
			//Adding Items to the Domain
			System.out.println("[INFO]-------> Adding Items to WHY3sinhash....");
			domain = sdb.getDomain("WHY3sinhash");
			item = domain.getItem(identifier);
			list = new ArrayList<ItemAttribute>();
			list.add(new ItemAttribute("sinhashIndex", index, false));
			list.add(new ItemAttribute("theoryIndex", thIndex, false)); 
			list.add(new ItemAttribute("sinhash", new SinHash().hashString("sinhash"), false)); 
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
		}
		
		
		}
	
	}
