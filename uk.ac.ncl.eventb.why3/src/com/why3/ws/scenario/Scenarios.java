package com.why3.ws.scenario;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Scenarios {
	private static final IScenarioAction[] ALL_PROVERS = {
			new ProverCall("alt-ergo"),
			new ProverCall("spass"),
			new ProverCall("z3"),
			new ProverCall("eprover"),			
			new ProverCall("cvc3"),			
			new ProverCall("cvc4")
		};

	private static final IScenarioAction[] ALL_PROVERS_TWICE_SEQ = {
		new DoubleProverCall("alt-ergo"),
		new DoubleProverCall("spass"),
		new DoubleProverCall("z3"),
		new DoubleProverCall("eprover"),			
		new DoubleProverCall("cvc3"),			
		new DoubleProverCall("cvc4")
		};
	
	public static final Map<String, IScenarioAction> SCENARIOS;
	public static final String DEFAULT_SCENARIO_NAME = "fast";
	public static final IScenarioAction DEFAULT_SCENARIO;
	
	static {
		Map<String, IScenarioAction> x = new HashMap<String, IScenarioAction>();
		
		/**
		 * Run all the provers in parallel, return any first success
		 */		
		x.put("fast", 
				new OrCompositionParallel(
						ALL_PROVERS.length,	//max threads 
						ALL_PROVERS			
						)
				);

		/**
		 * Run all the provers, one by one, return any first success
		 */		
		x.put("slow", 
				new OrCompositionSequential(
						ALL_PROVERS			
						)
				);		
		
		/**
		 * Run all the provers, one by one, adjudicate on the results
		 */		
		x.put("safe&slow", 
				new AndCompositionSequential(
						ALL_PROVERS			
						)
				);			
		
		/**
		 * Run provers sequentially with set deadlines
		 */
		x.put("smart", 
				new OrCompositionSequential(			 
						new Timeout(new ProverCall("eprover"), 300), // then eprover for 0.3 
						new Timeout(new ProverCall("alt-ergo"), 300), // try alt-ergo for 0.3 seconds 
						new Timeout(new ProverCall("cvc3"), 300), // then cvc3 for 0.3 
						new Timeout(new ProverCall("z3"), 300), // then z3 for 0.3 
						new Timeout(new ProverCall("alt-ergo"), 3000) // finally, alt-ergo for 3 seconds 
					)
				);
		
		/**
		 * Run all the provers, adjudicate on the result
		 */
		x.put("robust", 
				new AndCompositionParallel(
						ALL_PROVERS.length,			 
						ALL_PROVERS			
					)
				);		
		
		/**
		 * Run all the provers, each proving both positive and negative forms, adjudicate on the result
		 */		
		x.put("doublerobust", 
				new AndCompositionParallel(
						ALL_PROVERS_TWICE_SEQ.length,			 
						ALL_PROVERS_TWICE_SEQ			
					)
				);			
		
		SCENARIOS = Collections.unmodifiableMap(x);
		
		DEFAULT_SCENARIO = SCENARIOS.get(DEFAULT_SCENARIO_NAME);
    }	
	
}
