package com.why3.ws.composition;


import java.io.IOException;

import com.why3.ws.Verifier;
import com.why3.ws.connector.Why3CallResult.WHY3_RESULT;
import com.why3.ws.connector.Why3Tool;

/*
 * CVC3 Prover
 */
public class WHY3ProcessCvc3{

	//Lock object
    private final static Object lock = new Object();
    public static int index = 0;
		
				public static WHY3_RESULT mkProcess(String model)
				{
					WHY3_RESULT	result = null;
					try {
						//CVC3
						//if(ScenarioComposition.option=="par"){
						synchronized(lock){
							Why3Tool.modifyIndex = index;
							Verifier.tool = new Why3Tool(model);
							
							result = Verifier.tool.check();
							lock.notifyAll();
						}
						/*else if(ScenarioComposition.option=="seq"){
							Why3Tool.modifyIndex = 0;
							
							Why3Tool tool = new Why3Tool(model);
							
							result = tool.check();
						}*/
					}catch (IOException e) {
						e.printStackTrace();
					}
					return result;
				}	

}
