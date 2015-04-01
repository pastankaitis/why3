package com.why3.ws.composition;

import java.io.IOException;

import com.why3.ws.Verifier;
import com.why3.ws.connector.Why3CallResult.WHY3_RESULT;
import com.why3.ws.connector.Why3ToolGappa;

/*
 * Gappa Prover
 */
public class WHY3ProcessGappa{

	//Lock object
    private final static Object lock = new Object();
    public static int index = 4;
	
				public static WHY3_RESULT mkProcess(String model)
				{
					WHY3_RESULT	result = null;
					try {
						//if(ScenarioComposition.option=="par"){
						synchronized(lock){
							Why3ToolGappa.modifyIndex = index;
							Verifier.toolgappa = new Why3ToolGappa(model);
							
							result = Verifier.toolgappa.check();
						}
						/*else if(ScenarioComposition.option=="seq"){
							
						}*/
					}catch (IOException e) {
						e.printStackTrace();
					}
					return result;
				}	

}
