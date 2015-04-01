package com.why3.ws.data;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains definition of the known prover. A mathcing duplicate must be placed in the database
 * @author alex
 *
 */
public class ProverRegistry {
	private static final Map<String, Integer> provers;
	
	static {
		provers = new HashMap<String, Integer>();
		provers.put("alt-ergo", 1);
		provers.put("cvc3", 2);
		provers.put("simplify", 3);
		provers.put("spass", 4);
		provers.put("vampire", 5);
		provers.put("yices", 6);
		provers.put("iprover", 7);
		provers.put("verit", 8);
		provers.put("eprover", 9);
		provers.put("cvc4", 8);
		provers.put("z3", 9);
	}
	
	public static int getProverId(String name) {
		Integer code = provers.get(name);
		if (code == null)
			return 0;
		else
			return code;
	}
}
