package com.why3.ws.scenario;

import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;

public class IdTransformer implements ITheoremTransformer {
	public static final IdTransformer INSTANCE = new IdTransformer(); 
	
	private IdTransformer() {}
	
	@Override
	public TheoremTranslated transform(TheoremTranslated input) {
		return input;
	}

}
