package com.why3.ws;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class why3Main {
		
		static double initTime;

		//------------------------------------------------------------------------
		//ProcessBuilder for WHY3 with the parameters on LINUX
		//------------------------------------------------------------------------
				public static ProcessBuilder mkProcess(JSONObject m) throws IOException, URISyntaxException, JSONException, InterruptedException
				{
					initTime = (double)System.currentTimeMillis(); //       <----------Initial Time set before verification
					String st = m.getString("model");
					
												
					if(st != null){System.out.println("[INFO]---->Model is received at the Web Service\n");} 
					
					//File homedir = new File(System.getProperty("user.home"));
					
					PrintStream out = null;
					//File modelToRead = new File(homedir, "workspace/com.why3.ws/src/com/why3/ws/why3models/model.why");
					File model = FileUtil.getFileInput(st, "dsl.why");
//					
//					File currentDir = new File("");
//					File modelToRead = new File(currentDir.getAbsolutePath()+"/model.why");
					
					
					
//					try {		
//					    out = new PrintStream(new FileOutputStream(modelToRead));//Verifier.class.getClassLoader().getResource("/com/prob/ws/ProB0/file.mch").getPath() 
//						
//					    out.print(st);
//					    //System.out.println("[TEST] -----> File Path"+modelToRead.getPath());
//					}
//					finally {
//					    if (out != null){
//					    	//br.close();
//					    	out.close();
//					    }
//					}
					
						//String arg = "-version";
						String arg0 = "prove";
						//String arg1 = "-t"; 		//TIMEOUT
						
						List<String> command = Arrays.asList("why3", arg0, model.getAbsolutePath()); //Verifier.class.getClassLoader().getResource("./ProBCloud/src/com/prob/ws/ProB0/file.mch").getPath()
						ProcessBuilder procBuilder = new ProcessBuilder(command);	
								
					return procBuilder;
				}
		//------------------------------------------------------------------------		
			


}
