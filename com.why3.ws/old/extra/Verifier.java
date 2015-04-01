/**
 * 
 */
package com.why3.ws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;



import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


/**
 * @author b1024094
 *
 */
public class Verifier {
	public static String verification;
	public static Long startTime;
	public static long toolTimeOut;
	public static long curTime;
	
	public InputStream stderr;
	public InputStream stdout;
    static BufferedReader bout;
	
    static double finalTime;
    static String s;
    static long tim;
    static String sTime;
    static String option;
    static String m_status;
    
    static String file_Num;
    
    static JSONObject jsonObject;
    public static ProcessBuilder procBuilder0;
    
	
    //Time Converter Method
	public static String getDate(long milliSeconds, String dateFormat)
	{
	    // Creates a DateFormatter object for displaying date in specified format.
	    DateFormat formatter = new SimpleDateFormat(dateFormat);

	    // Creates a calendar object that will convert the date and time value in milliseconds to date. 
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTimeInMillis(milliSeconds);
	     
	     return formatter.format(calendar.getTime());
	}
	
			
	//MAIN verification method 
	public void process(JSONObject json) throws IOException, URISyntaxException, JSONException, InterruptedException, NoSuchAlgorithmException
	{
		System.out.println("\nBEGIN VERIFICATION------------------------------------------------------Started: "+ getDate(System.currentTimeMillis(), "dd/MM/yyyy hh:mm:ss.SSS")+" ms\n");
		
		//Developer's Tool Option
								
				int which = (int)(Math.random() * 5); 

				switch (which) {
				    case 0:  option = "alt-ergo";
				             break;
				    case 1:  option = "cvc3";
				             break;
				    case 2:  option = "Metis";
				             break;
				    case 3:  option = "SPASS";
				             break;
				    case 4:  option = "Coq";
		                    break;
				    default: option = "Oops -- something is wrong with this code.";
				}
		
		//Tool TimeOut
		toolTimeOut = json.getLong("t");
		
				
		//Model Spec
		s = json.getString("model");
		
		curTime = System.currentTimeMillis(); //Assigning a changing time stamp 
		
		System.out.println("HASH_1 of Model: "+makeSHA1Hash(s+curTime)); 
		
		//Start Process
		Process proc = null;
		
		//ProcessBuilder pb = why3Process.mkProcess(json); <--------------ProB
		ProcessBuilder pb = why3Main.mkProcess(json);
		
		proc = pb.start();
		
							//Test Process
							try{
								if(proc.waitFor() ==0){System.out.println("8<------- [process is running] :)");}
								else{
									System.out.println("8<------- [process is NOT running] :(");
									if(proc != null) proc.destroy();
								}
							}
							catch(IllegalThreadStateException e){e.printStackTrace();}
		
		
		    //File homedir = new File(System.getProperty("user.home"));
		    //int num = new File(homedir, "WHY3_Workspace/com.why3.ws/src/com/why3/ws/models").list().length + 1; //LINUX: ls -1a | wc -l
		    
		    
		    file_Num = "model.why";
		    //System.out.println("Model Name: " + file_Num);
		
		
		//Saving file in the models folder <------ MODEL INFO
		    try {
	            //File TextFile = new File(homedir, "workspace/com.why3.ws/src/com/why3/ws/models/"+file_Num);
		    	
		    	File currentDir = new File("");
				File TextFile = new File(currentDir.getAbsolutePath()+"/"+file_Num);
				
				//File TextFile = new File(${rootPath}WEB-INF/+"/"+file_Num);
		    	
	            FileWriter fw = new FileWriter(TextFile);
	            fw.write( (int) tim );
	            fw.write("\nTool Option: "+ option+"\n" );
	            fw.write("Model Specification: "+s);
	            fw.close();

	        } catch (IOException iox) {
	            iox.printStackTrace();
	        }    
		    
		    
		  //Saving file in the models folder <------ WHY3 MODELS
            try {
            	
                //File newTextFile = new File(homedir, "workspace/com.why3.ws/src/com/why3/ws/why3models/"+file_Num);
                 
            	File currentDir = new File("");
				File newTextFile = new File(currentDir.getAbsolutePath()+"/"+file_Num);
				
                FileWriter nfw = new FileWriter(newTextFile);
                nfw.write(s);
                nfw.close();

            } catch (IOException iox) {
                iox.printStackTrace();
            }    
		
		    
		if(s != null && !s.isEmpty()){
		    m_status = "RECIEVED";
			
			System.out.println("[TEST]--> Model Status: "+ m_status);
			}
		else if(s == null && s.isEmpty()){
		    m_status = "NOT RECIEVED";
		    System.out.println("[INFO]---->Client's Message is NOT received at the Web Service\n");}
		
		
		//Output from Tool 
		stdout = proc.getInputStream();
		
		bout = new BufferedReader(new InputStreamReader(stdout));
		//bout.close();
		try{
		verResults();

		String [] args = new String[1];
	    args[0] = "";
		try {
			SimpleDBAdmin.main(args);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		}
		catch(IOException e){e.printStackTrace();}
		 
	}
	
			//Verification Output to File and Console   <-----CLIENT
			public String verResults() throws IOException, JSONException{	
				
				StringBuilder sb = new StringBuilder();
				
				String mod = bout.readLine();
				
				while(mod != null){
					sb.append(mod);
					sb.append("\n");
					mod = bout.readLine();
					sb.append("\n");
				}
		        //Verification Time Calculation
		        finalTime = (double)System.currentTimeMillis();     //       <----------Final Time set after verification
		        double age = finalTime - why3Main.initTime;
		        
		        //Converting time to 2 decimal places
		        DecimalFormat twoDForm = new DecimalFormat("#.00");
		        String time = twoDForm.format(age); 
		        sTime = time;
		        
		        tim = (long) age;
		        
				verification = "\nSummary of Verification:\n"+ "Verification Time of "+file_Num+": "+time+" ms"+ 
				"\n\n************************************************************\n		REPORT_0     ["+getDate(System.currentTimeMillis(), "dd/MM/yyyy hh:mm:ss.SSS")
				+"]\n************************************************************\nTIME:    "+ getDate(tim, "dd/MM/yyyy hh:mm:ss.SSS") +"ms\nTOOL OPTION:    "+option+"\nMODEL STATUS: "+ m_status +"\n\nRESULTS: \n\n"+sb.toString()+"\n\n"+
				"[Time for Verification on Cloud: "+ getDate((long)age, "dd/MM/yyyy hh:mm:ss.SSS") +" ms]";
				
				System.out.println(verification + "\n");
						
				
				//CHECK - Initialisation
				time = "";
				age = 0;
				finalTime = 0;
											
				return verification;
			}
			
			
			/*
			 * Apache commons code provides many overloaded methods to generate md5 hash. It contains
			 * md5 method which can accept String, byte[] or InputStream and can return hash as 16 element byte
			 * array or 32 character hex String.
			 */
			public static String md5ApacheCommonsCodec(String content){
				return DigestUtils.md5Hex(content); }	
			
			public static String makeSHA1Hash(String input)      //    <--------- Hash Function
		            throws NoSuchAlgorithmException
		        {
		            MessageDigest md = MessageDigest.getInstance("SHA1");
		            md.reset();
		            byte[] buffer = input.getBytes();
		            md.update(buffer);
		            byte[] digest = md.digest();

		            String hexStr = "";
		            for (int i = 0; i < digest.length; i++) {
		                hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
		            }
		            return hexStr;
		        }
			
			
			
			//Temporary File
			public static File createTempDirectory()
				    throws IOException
				{
				    final File temp;

				    temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

				    if(!(temp.delete()))
				    {
				        throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
				    }

				    if(!(temp.mkdir()))
				    {
				        throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
				    }

				    return (temp);
				}
}
