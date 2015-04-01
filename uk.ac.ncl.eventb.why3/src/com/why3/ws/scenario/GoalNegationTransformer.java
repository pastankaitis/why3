package com.why3.ws.scenario;

import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;

public class GoalNegationTransformer implements ITheoremTransformer {
	public static final GoalNegationTransformer INSTANCE = new GoalNegationTransformer(); 
	
	private GoalNegationTransformer() {}
	
	@Override
	public TheoremTranslated transform(TheoremTranslated input) {
		return input.getNegatedForm();
	}

}
