package com.why3.ws.data;

/**
* Simple DB Load Data
* Uses Typica Library to load sample data into Domain
* 
* @author David Ebo Adjepon-Yamoah
*
*/

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xerox.amazonws.sdb.Domain;
import com.xerox.amazonws.sdb.ItemAttribute;
import com.xerox.amazonws.sdb.QueryWithAttributesResult;
import com.xerox.amazonws.sdb.SDBException;
import com.xerox.amazonws.sdb.SimpleDB;

public class QuerySimpleDB {
	
	protected final static String AWSAccessKeyID = "AKIAJ6CCAW5FIIWP3WZA";
	protected final static String AWSSecretKey = "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";

	static Domain domain;
	
	static Logger log = Logger.getLogger(
			QuerySimpleDB.class.getName());
	

	public static void main(String[] args) {
		SimpleDB sdb = new SimpleDB(AWSAccessKeyID,AWSSecretKey);
		try {
			domain = sdb.getDomain("WHY3sequent");
		} catch (SDBException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("===========================================");
		System.out.println("Query items from "+domain.getName());
		System.out.println("===========================================\n");
		
		// Retrieve Items from Domain
		 String queryString = "SELECT * FROM `WHY3sequent`"; 
		 
		 int itemCount = 0; 
		 String nextToken = null; 
		 
		 do {
			 QueryWithAttributesResult queryResults;
			try {
					queryResults = domain.selectItems(queryString, nextToken);
					Map<String,List<ItemAttribute>> items = queryResults.getItems(); 
					
					for (String id : items.keySet()) {
						System.out.println("Item : " + id);
						for(ItemAttribute attr : items.get(id)) {
							System.out.println(attr.getName() + " = " + attr.getValue());
						}
						System.out.println("\n");
					 itemCount++;
					 }
					
					//Item count in domain
					System.out.println("[INFO]-------> Item Count in Domain["+domain.getName()+"]: "+itemCount);
					
					nextToken = queryResults.getNextToken();
					log.info(queryResults);
			} catch (SDBException e) {
				e.printStackTrace();
			}
		 } while(nextToken != null && !nextToken .trim().equals(""));
		 

		// Retrieving Individual Items from the Domain 
		/*
		 * Item req =
		 * domain.getItem(Verifier.makeSHA1Hash(Verifier.s+Verifier
		 * .curTime)); List<ItemAttribute> itemAttrs = req.getAttributes();
		 * for (ItemAttribute attr : itemAttrs) {
		 * System.out.println(attr.getName() + " = " + attr.getValue()); }
		 */
	}

}
