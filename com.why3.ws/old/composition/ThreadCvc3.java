package com.why3.ws.composition;

import java.util.concurrent.Callable;

import com.why3.ws.Verifier;
import com.why3.ws.connector.Why3CallResult.WHY3_RESULT;
import com.why3.ws.connector.Why3Tool;
import com.why3.ws.data.SimpleDBCvc3;
import com.why3.ws.data.parallel.SimpleDBParCvc3;

public class ThreadCvc3 implements Callable<String> {
		
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
				
		WHY3_RESULT result = WHY3ProcessCvc3.mkProcess(Verifier.mod);
		String res = result.toString(); 
		ScenarioComposition.tCvc3 = res;
		
		if(ScenarioComposition.option=="seq"){
			
			SimpleDBCvc3.main(null);

			System.out.println("CVC3 ("+ Why3Tool.modifyIndex +") Result["+System.currentTimeMillis()+"]:"+ res );
			
			//SimpleDBResult.main(null);
		}else if(ScenarioComposition.option=="par"){
			SimpleDBParCvc3.main(null);

			System.out.println("CVC3 ("+ Why3Tool.modifyIndex +") Result["+System.currentTimeMillis()+"]:"+ res );
			
		}

		return res;
	}
}
	