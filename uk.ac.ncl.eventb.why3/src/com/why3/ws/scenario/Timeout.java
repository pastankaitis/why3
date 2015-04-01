package com.why3.ws.scenario;

import java.util.List;

import org.eventb.core.seqprover.IProofMonitor;

import uk.ac.ncl.eventb.why3.driver.ToolConnection;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;


public class Timeout implements IScenarioAction {
	private long timeout;
	private IScenarioAction body;
	
	public Timeout(IScenarioAction body, long timeout) {
		this.timeout = timeout;
		this.body = body;
	}
	
	@Override
	public Why3CallResult execute(final TheoremTranslated input, final long timeout, final IProofMonitor monitor, final List<ToolConnection> jobs) {
		try {
			long t0 = Math.min(this.timeout, timeout);
			return body.execute(input, t0, monitor, jobs);
		} catch (Throwable e) {
			return Why3CallResult.INSTANCE_FAILURE;
		}
	}

}
