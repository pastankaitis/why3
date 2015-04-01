package com.why3.ws.scenario;

import java.util.List;

import com.why3.ws.TheoremTranslated;
import com.why3.ws.connector.ToolConnection;
import com.why3.ws.connector.Why3CallResult;
import com.why3.ws.data.IProofEventFactory;

/**
 * Common scenario interface 
 * @author alex
 *
 */
public interface IScenarioAction {
	public Why3CallResult execute(final TheoremTranslated input, final long timeout, final List<ToolConnection> jobs, IProofEventFactory eventfactory);
}
