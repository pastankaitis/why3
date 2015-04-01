package com.why3.ws.composition;

import java.util.concurrent.Callable;

import com.why3.ws.Verifier;
import com.why3.ws.connector.Why3CallResult.WHY3_RESULT;
import com.why3.ws.connector.Why3Tool;
import com.why3.ws.connector.Why3ToolAltergo;
import com.why3.ws.data.SimpleDBAltergo;
import com.why3.ws.data.parallel.SimpleDBParAltergo;

public class ThreadAltergo implements Callable<String> {
	
	//Lock object
    private final static Object lock = new Object();
		
	public class CompositionThread implements Runnable {
	    public void run() {
	    	try {
	    		synchronized(lock){
	    			call();
	    		}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
	
	public String call(){
						
		WHY3_RESULT result = WHY3ProcessAltergo.mkProcess(Verifier.mod);
		
		String res = result.toString();

		ScenarioComposition.tAltergo = res;
		
		
		if(ScenarioComposition.option=="seq"){
			
			SimpleDBAltergo.main(null);

			System.out.println("Alt-ergo ("+ Why3Tool.modifyIndex +") Result ["+System.currentTimeMillis()+"]:"+ res );
		}else if(ScenarioComposition.option=="par"){
			
			SimpleDBParAltergo.main(null);

			System.out.println("Alt-ergo ("+ Why3ToolAltergo.modifyIndex +") Result ["+System.currentTimeMillis()+"]:"+ res );
		}

		
		return res;
	}
}
	