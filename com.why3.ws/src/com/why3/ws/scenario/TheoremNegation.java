package com.why3.ws.scenario;

import java.util.List;

import com.why3.ws.TheoremTranslated;
import com.why3.ws.connector.ToolConnection;
import com.why3.ws.connector.Why3CallResult;
import com.why3.ws.data.IProofEventFactory;

public class TheoremNegation implements IScenarioAction {
	private IScenarioAction body;
	
	public TheoremNegation(IScenarioAction body) {
		this.body = body;
	}
	
	@Override
	public Why3CallResult execute(final TheoremTranslated input, final long timeout, final List<ToolConnection> jobs, final IProofEventFactory eventfactory) {
		try {
			return body.execute(input, timeout, jobs, eventfactory).negate();
		} catch (Throwable e) {
			return Why3CallResult.INSTANCE_FAILURE;
		}
	}

}
