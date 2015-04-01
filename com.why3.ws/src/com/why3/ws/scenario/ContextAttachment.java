package com.why3.ws.scenario;

import java.util.List;

import com.why3.ws.IContextProvider;
import com.why3.ws.TheoremTranslated;
import com.why3.ws.connector.ToolConnection;
import com.why3.ws.connector.Why3CallResult;
import com.why3.ws.data.IProofEventFactory;

public class ContextAttachment implements IScenarioAction {
	private IContextProvider context;
	private IScenarioAction body;
	
	public ContextAttachment(IScenarioAction body, IContextProvider context) {
		this.context = context;
		this.body = body;
	}

	@Override
	public Why3CallResult execute(TheoremTranslated input, long timeout, final List<ToolConnection> jobs, final IProofEventFactory eventfactory) {
		TheoremTranslated result = input.clone();
		result.setContext(context);
		return body.execute(result, timeout, jobs, eventfactory);
	}

}
