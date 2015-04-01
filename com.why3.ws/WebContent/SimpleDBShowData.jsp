
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

  <jsp:directive.page import="com.xerox.amazonws.sdb.*"/>
  <jsp:directive.page import="java.util.List"/>
  <jsp:directive.page import="java.util.ArrayList"/>
 
 <%
		SimpleDB exampleDB = new SimpleDB("AKIAJ6CCAW5FIIWP3WZA","yrcA635mDv2+S8dljk96RcUMqdDvBYDNEDlUppqS");
		Domain exampleDomain;
		List<Item> items;
		QueryResult result = null;
		
		//StringBuffer Index = new StringBuffer("");
		StringBuffer sequentIndex= new StringBuffer("");
		StringBuffer sequent= new StringBuffer("");
		StringBuffer sequentDate= new StringBuffer("");
				
		String itemIdentifier= null;
		List<String> attributesToGet = new ArrayList<String>();
		
		try {
			exampleDomain=exampleDB.getDomain("WHY3sequent");
			result = exampleDomain.listItems();
			items = result.getItemList();
			
			
			for (Item exampleItem : items) {
				itemIdentifier =exampleItem.getIdentifier();
				 //Index = new StringBuffer("");
				 sequentIndex= new StringBuffer("");
				 sequent= new StringBuffer("");
				 sequentDate= new StringBuffer("");
				 				
				
				List<ItemAttribute> attrs = exampleItem.getAttributes(attributesToGet);
				for (ItemAttribute currentAttribute : attrs) {
					
					
					if (currentAttribute.getName().equals("Author")){
						sequentIndex.append( currentAttribute.getValue()+" ");
					}else
					if (currentAttribute.getName().equals("Year")){
						sequent.append( currentAttribute.getValue()+" ");
					}else
					if (currentAttribute.getName().equals("Keyword")){
						sequentDate.append( currentAttribute.getValue()+" ");
					}
				}	// for each attribute
				String display = itemIdentifier+"|"+sequentIndex.toString() +"|"+ sequent.toString() +"|" + sequentDate.toString();
				%>
				
				<%=display %>
				
                <%			
			} // for each item
		} catch (SDBException ex) {
			System.err.println("message : "+ex.getMessage());
			System.err.println("requestID : "+ex.getRequestId());
		}
	
 %>