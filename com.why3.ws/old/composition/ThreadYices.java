package com.why3.ws.composition;

import java.util.concurrent.Callable;

import com.why3.ws.Verifier;
import com.why3.ws.connector.Why3CallResult.WHY3_RESULT;
import com.why3.ws.connector.Why3Tool;
import com.why3.ws.connector.Why3ToolYices;
import com.why3.ws.data.SimpleDBYices;
import com.why3.ws.data.parallel.SimpleDBParYices;

public class ThreadYices implements Callable<String> {
	
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
						
		WHY3_RESULT result = WHY3ProcessYices.mkProcess(Verifier.mod);
		
		String res = result.toString();

		ScenarioComposition.tYices = res;
				
		if(ScenarioComposition.option=="seq"){
			
			SimpleDBYices.main(null);
			System.out.println("Yices ("+ Why3Tool.modifyIndex +") Result ["+System.currentTimeMillis()+"]:"+ res );
			//SimpleDBResult.main(null);
		}else if(ScenarioComposition.option=="par"){
			SimpleDBParYices.main(null);
			System.out.println("Yices ("+ Why3ToolYices.modifyIndex +") Result ["+System.currentTimeMillis()+"]:"+ res );
		}

		
		return res;
	}
}
	