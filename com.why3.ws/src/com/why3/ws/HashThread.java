package com.why3.ws;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class HashThread{
	//This method will be executed when this thread is called  
	public void run(JSONObject json)     
	  {
		  //JSONObject json = new JSONObject();
		  try {
			makeMD5Hash(json);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		  	      
	  }
	  
	  //Hash Function for JSON files
	  public static String makeMD5Hash(JSONObject json)      
	            throws NoSuchAlgorithmException
	  {
		  String model;
		  String hexStr = "";
		  		
		  if(!(json.equals(null))){
					
			try {
				model = json.getString("model");
					
				
			    MessageDigest md = MessageDigest.getInstance("MD5");
			    md.reset();
			    byte[] buffer = model.getBytes();
			    md.update(buffer);
			    byte[] digest = md.digest();
		
			    for (int i = 0; i < digest.length; i++) {
			         hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
			    }
		            	
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("JSON file is NULL!");
			}
		//System.out.println("MD5 HASH: "+ hexStr);
	return hexStr;          
	  }
	  
	//Hash Function for Strings
	  public static String makeMD5Hash(String Str)      
	            throws NoSuchAlgorithmException
	  {
		  String hexStr = "";
		  if(Str.equals(null)){Str = "test";}
		  else if(!(Str.equals(null))){
					
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			byte[] buffer = Str.getBytes();
			md.update(buffer);
			byte[] digest = md.digest();

			for (int i = 0; i < digest.length; i++) {
			     hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
			}
		}else{
			System.out.println("String is NULL!");
			}
		//System.out.println("MD5 HASH: "+ hexStr);
	return hexStr;          
	  }

//	@Override
//	public void run() {
//		JSONObject json = new JSONObject();
//		try {
//			makeMD5Hash(json);
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//		
//	}

}