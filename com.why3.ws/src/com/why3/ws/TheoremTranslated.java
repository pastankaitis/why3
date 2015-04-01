package com.why3.ws;

public class TheoremTranslated {
	public static final String GOAL_PREFIX = "\tgoal g0: ";
	private String imports;	
	private String types;
	private String definitions;
	private String hypothesis;
	private String goal;
	private IContextProvider context;
	
	
	public TheoremTranslated() {
	}

	public TheoremTranslated(String types, String definitions, String hypothesis, String goal) {
		this.types = types;
		this.definitions = definitions;
		this.hypothesis = hypothesis;
		this.goal = goal;
	}
	
	public TheoremTranslated(String formula, boolean isDefinition) {
		if (isDefinition) {
			definitions = formula;
		} else {
			goal = formula;
		}
	}	
	
	public String getImports() {
		return imports;
	}

	public void setImports(String imports) {
		this.imports = imports;
	}

	@Override
	public TheoremTranslated clone() {
		return new TheoremTranslated(types, definitions, hypothesis, goal);
	}

	public TheoremTranslated getNegatedForm() {
		return new TheoremTranslated(types, definitions, hypothesis, "not (" + goal + ")");
	}
	
	public String getDefinitions() {
		return definitions;
	}

	public String getHypothesis() {
		return hypothesis;
	}

	public String getGoal() {
		return goal;
	}

	public String getTypes() {
		return types;
	}
	
	void setTypes(String types) {
		this.types = types;
	}

	void setDefinitions(String definitions) {
		this.definitions = definitions;
	}

	void setHypothesis(String hypothesis) {
		this.hypothesis = hypothesis;
	}

	void setGoal(String goal) {
		this.goal = goal;
	}

	public IContextProvider getContext() {
		return context;
	}

	public void setContext(IContextProvider context) {
		this.context = context;
	}
	
	public String toString(IContextProvider provider) {
		StringBuilder sb = new StringBuilder();
		sb.append("theory POx\n");
		provider.addHeader(sb);
		sb.append("\n");
		if (imports != null) {
			sb.append(imports);		
			sb.append("\n");
		}
		if (types != null) {
			sb.append(types);		
			sb.append("\n");
		}
		if (definitions != null) {
			sb.append(definitions);
			sb.append("\n");
		}
		if (hypothesis != null) {
			sb.append(hypothesis);
			sb.append("\n");
		}
		
		if (goal != null) {
			sb.append(GOAL_PREFIX);
			sb.append(goal);
			sb.append("\n");
		}
		sb.append("end\n");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		if (context == null)
			return toString(DefaultContextProvider.INSTANCE);
		else
			return toString(context);
	}

}
