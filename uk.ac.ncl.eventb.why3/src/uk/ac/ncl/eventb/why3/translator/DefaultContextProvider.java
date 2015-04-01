package uk.ac.ncl.eventb.why3.translator;

public class DefaultContextProvider implements IContextProvider {
	public static final DefaultContextProvider INSTANCE = new DefaultContextProvider(); 
	
	public static final String[] ALL_THEORIES = {
		// Integers and booleans
		"int.Int",
		"bool.Bool",
		"int.EuclideanDivision",
		// Set-theoretic definitions
		"set.Set",
		"set.SetComprehension",
		"set.Fset",
		"settheory.Interval",
		"settheory.PowerSet",
		"settheory.Relation",
		"settheory.Image",
		"settheory.InverseDomRan",
		"settheory.Function",
		"settheory.PowerSet",
		"settheory.PowerRelation",
		// Event-B axiomatization
		"eventb.Prods",
		"eventb.FuncRel",
		"eventb.SetProp",
		"eventb.SetRelMod",
		"eventb.Extras",
		"inference.Inference",
	};	
	
	private DefaultContextProvider() {}
	
	@Override
	public void addTheoryImports(StringBuilder sb) {
		for(String theory: ALL_THEORIES) {
			sb.append("\tuse import " + theory + "\n");
		}	
	}

	@Override
	public void addInplaceTheories(StringBuilder sb) {
		//ignore
	}	


}
