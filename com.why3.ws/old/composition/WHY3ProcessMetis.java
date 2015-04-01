package com.why3.ws.composition;

import java.io.IOException;

import com.why3.ws.Verifier;
import com.why3.ws.connector.Why3CallResult.WHY3_RESULT;
import com.why3.ws.connector.Why3ToolMetis;

/*
 * Metis Prover
 */
public class WHY3ProcessMetis{

	//Lock object
    private final static Object lock = new Object();
    public static int index = 3;
	
				public static WHY3_RESULT mkProcess(String model)
				{
					WHY3_RESULT	result = null;
					try {
						
						//if(ScenarioComposition.option=="par"){
						synchronized(lock){
							Why3ToolMetis.modifyIndex = index;
							Verifier.toolmetis = new Why3ToolMetis(model);
							
							result = Verifier.toolmetis.check();
						}
						/*else if(ScenarioComposition.option=="seq"){
							
						}*/
					}catch (IOException e) {
						e.printStackTrace();
					}
					return result;
				}
				
		

}
