package com.why3.ws.composition;

import java.util.concurrent.Callable;

import com.why3.ws.Verifier;
import com.why3.ws.connector.Why3CallResult.WHY3_RESULT;
import com.why3.ws.connector.Why3Tool;
import com.why3.ws.connector.Why3ToolMetis;
import com.why3.ws.data.SimpleDBMetis;
import com.why3.ws.data.parallel.SimpleDBParMetis;

public class ThreadMetis implements Callable<String> {
	
	public class CompositionThread implements Runnable {
	    public void run() {
	    	try {	    		
	    			call();	    	
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
	
	@Override
	public String call() throws Exception {
						
		WHY3_RESULT result = WHY3ProcessMetis.mkProcess(Verifier.mod);
		String res = result.toString();

		ScenarioComposition.tMetis = res;
		
		if(ScenarioComposition.option=="seq"){
			
			SimpleDBMetis.main(null);

			System.out.println("Metis ("+ Why3Tool.modifyIndex +") Result["+System.currentTimeMillis()+"]:"+ res );
			
			//SimpleDBResult.main(null);
		}else if(ScenarioComposition.option=="par"){
			SimpleDBParMetis.main(null);

			System.out.println("Metis ("+ Why3ToolMetis.modifyIndex +") Result["+System.currentTimeMillis()+"]:"+ res );
			
		}

		return res;
	}
}
	