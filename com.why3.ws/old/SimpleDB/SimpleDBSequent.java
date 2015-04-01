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

import com.why3.ws.Verifier;
//import com.why3.ws.Verifier;
import com.xerox.amazonws.sdb.Domain;
import com.xerox.amazonws.sdb.Item;
import com.xerox.amazonws.sdb.ItemAttribute;
import com.xerox.amazonws.sdb.SDBException;
import com.xerox.amazonws.sdb.SimpleDB;

public class SimpleDBSequent {
	
	protected final static String AWSAccessKeyID = "AKIAJ6CCAW5FIIWP3WZA";
	protected final static String AWSSecretKey = "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";

	static Domain domain;
	
	private static long count = 1;
	private static int domIndex = 1;
	
	private static String domName = "WHY3sequent";
	
	static Logger log = Logger.getLogger(
            SimpleDBSequent.class.getName());
	
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
				domName = "WHY3sequent";
				createDomain(domName+domIndex);
		
	    		domName = "WHY3sequent";
				domName = domName+domIndex;
	    		addItems();
				
	    		domIndex++;
				count++;
			}
	}
	
	/**
	 * List all available Domain(s)
	 */
	public void listDomain(){
					System.out.println("[INFO]-------> List Domains in WHY3sequent:");
					
						log.info("["+String.valueOf(Verifier.getDate((System.currentTimeMillis()),"dd/MM/yyyy hh:mm:ss.SSS"))+"]");
						log.info("[INFO]-------> List Domains in WHY3sequent:");
					
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
			String identifier = "sequent"+String.valueOf(System.currentTimeMillis()).hashCode();
			
			//Adding Items to the Domain
			System.out.println("\n[INFO]-------> Adding Items to WHY3sequent....");
				log.info("\n[INFO]-------> Adding Items to WHY3sequent....");
			
			domain = sdb.getDomain("WHY3sequent");
			item = domain.getItem(identifier);
			list = new ArrayList<ItemAttribute>();
			list.add(new ItemAttribute("sequentIndex", String.valueOf(identifier), false)); 
			list.add(new ItemAttribute("sequent", "sequent", false)); 
			list.add(new ItemAttribute("sequentDate", String.valueOf(Verifier.getDate(Verifier.start, "yyyy-MM-dd HH:mm:ss.S")), false)); 
			list.add(new ItemAttribute("scenario", Verifier.getScenario(), false));
			item.putAttributes(list);
			
			log.info(domain);
			System.out.print("							[DONE]\n");
			
			log.info("[INFO]-------> Item(s) have been added successfully to WHY3sequent");
			log.debug("[INFO]-------> Item(s) have been added successfully to WHY3sequent");
		} catch (SDBException ex) {
			System.err.println("Error Message : "+ex.getMessage());
			System.err.println("RequestID : "+ex.getRequestId());
		} catch (NullPointerException npe){
			System.err.println("Error Message : \n");
			npe.printStackTrace();
			}
	}
}
