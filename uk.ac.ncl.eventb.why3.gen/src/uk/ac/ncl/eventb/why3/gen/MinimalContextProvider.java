package uk.ac.ncl.eventb.why3.gen;

import uk.ac.ncl.eventb.why3.translator.IContextProvider;

public class MinimalContextProvider implements IContextProvider {
	public static final MinimalContextProvider INSTANCE = new MinimalContextProvider(); 
	
	public static final String[] ALL_THEORIES = {
		// Integers and booleans
		"int.Int",
		"bool.Bool",
		"int.EuclideanDivision",
		"set.Set",
		"settheory.Relation"
	};	
	
	private MinimalContextProvider() {}
	
	@Override
	public void addTheoryImports(StringBuilder sb) {
		
		for(String theory: ALL_THEORIES) {
			sb.append("\tuse import " + theory + "\n");
		}	
	}

	@Override
	public void addInplaceTheories(StringBuilder sb) {
		// ignore
	}	

}
