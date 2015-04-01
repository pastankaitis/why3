package com.why3.ws.scenario;

import java.util.List;

import com.why3.ws.TheoremTranslated;
import com.why3.ws.connector.ToolConnection;
import com.why3.ws.connector.Why3CallResult;
import com.why3.ws.data.IProofEventFactory;

public class Timeout implements IScenarioAction {
	private long timeout;
	private IScenarioAction body;
	
	public Timeout(IScenarioAction body, long timeout) {
		this.timeout = timeout;
		this.body = body;
	}
	
	@Override
	public Why3CallResult execute(final TheoremTranslated input, final long timeout, final List<ToolConnection> jobs, final IProofEventFactory eventfactory) {
		try {
			long t0 = Math.min(this.timeout, timeout);
			return body.execute(input, t0, jobs, eventfactory);
		} catch (Throwable e) {
			return Why3CallResult.INSTANCE_FAILURE;
		}
	}

}
