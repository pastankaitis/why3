package com.why3.ws.composition;

import java.util.concurrent.Callable;

import com.why3.ws.Verifier;
import com.why3.ws.connector.Why3CallResult.WHY3_RESULT;
import com.why3.ws.connector.Why3Tool;
import com.why3.ws.connector.Why3ToolGappa;
import com.why3.ws.data.SimpleDBGappa;
import com.why3.ws.data.parallel.SimpleDBParGappa;

public class ThreadGappa implements Callable<String> {
	
	public class CompositionThread implements Runnable {
	    public void run() {
	    	try {	    		
	    			call();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
	
	public String call(){
						
		WHY3_RESULT result = WHY3ProcessGappa.mkProcess(Verifier.mod);
		
		String res = result.toString();
		
		ScenarioComposition.tGappa = res;
		
		if(ScenarioComposition.option=="seq"){
			
			SimpleDBGappa.main(null);

			System.out.println("Gappa ("+ Why3Tool.modifyIndex +") Result ["+System.currentTimeMillis()+"]:"+ res );
			
			//SimpleDBResult.main(null);
		}else if(ScenarioComposition.option=="par"){
			SimpleDBParGappa.main(null);

			System.out.println("Gappa ("+ Why3ToolGappa.modifyIndex +") Result ["+System.currentTimeMillis()+"]:"+ res );
			
		}
		
		return res;
	}
}
	