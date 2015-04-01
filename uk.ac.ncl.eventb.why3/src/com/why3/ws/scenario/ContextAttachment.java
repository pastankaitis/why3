package com.why3.ws.scenario;

import java.util.List;

import org.eventb.core.seqprover.IProofMonitor;

import uk.ac.ncl.eventb.why3.driver.ToolConnection;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.translator.IContextProvider;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;


public class ContextAttachment implements IScenarioAction {
	private IContextProvider context;
	private IScenarioAction body;
	
	public ContextAttachment(IScenarioAction body, IContextProvider context) {
		this.context = context;
		this.body = body;
	}

	@Override
	public Why3CallResult execute(TheoremTranslated input, long timeout, final IProofMonitor monitor, List<ToolConnection> jobs) {
		TheoremTranslated result = input.clone();
		result.setContext(context);
		return body.execute(result, timeout, monitor, jobs);
	}

}
