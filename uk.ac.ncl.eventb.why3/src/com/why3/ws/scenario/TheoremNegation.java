package com.why3.ws.scenario;

import java.util.List;

import org.eventb.core.seqprover.IProofMonitor;

import uk.ac.ncl.eventb.why3.driver.ToolConnection;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;


public class TheoremNegation implements IScenarioAction {
	private IScenarioAction body;
	
	public TheoremNegation(IScenarioAction body) {
		this.body = body;
	}
	
	@Override
	public Why3CallResult execute(final TheoremTranslated input, final long timeout, final IProofMonitor monitor, final List<ToolConnection> jobs) {
		try {
			return body.execute(input, timeout, monitor, jobs).negate();
		} catch (Throwable e) {
			return Why3CallResult.INSTANCE_FAILURE;
		}
	}

}
