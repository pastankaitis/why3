package com.why3.ws.composition;

import java.util.concurrent.Callable;

import com.why3.ws.Verifier;
import com.why3.ws.connector.Why3CallResult.WHY3_RESULT;
import com.why3.ws.connector.Why3Tool;
import com.why3.ws.connector.Why3ToolSpass;
import com.why3.ws.data.SimpleDBSpass;
import com.why3.ws.data.parallel.SimpleDBParSpass;

public class ThreadSpass implements Callable<String> {
	
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
						
		WHY3_RESULT result = WHY3ProcessSpass.mkProcess(Verifier.mod);
		String res = result.toString(); 

		ScenarioComposition.tSpass = res;
				
		if(ScenarioComposition.option=="seq"){
			
			SimpleDBSpass.main(null);

			System.out.println("Spass ("+ Why3Tool.modifyIndex +") Result["+System.currentTimeMillis()+"]:"+ res );

			//SimpleDBResult.main(null);
		}else if(ScenarioComposition.option=="par"){
			SimpleDBParSpass.main(null);

			System.out.println("Spass ("+ Why3ToolSpass.modifyIndex +") Result["+System.currentTimeMillis()+"]:"+ res );

		}

		return res;
	}
}
	