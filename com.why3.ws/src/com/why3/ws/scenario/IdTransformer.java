package com.why3.ws.scenario;

import com.why3.ws.TheoremTranslated;


public class IdTransformer implements ITheoremTransformer {
	public static final IdTransformer INSTANCE = new IdTransformer(); 
	
	private IdTransformer() {}
	
	@Override
	public TheoremTranslated transform(TheoremTranslated input) {
		return input;
	}

}
