package com.why3.ws.scenario;

import java.util.List;

import org.eventb.core.seqprover.IProofMonitor;

import uk.ac.ncl.eventb.why3.driver.ToolConnection;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;



/**
 * Common scenario interface 
 * @author alex
 *
 */
public interface IScenarioAction {
	@SuppressWarnings("rawtypes")
	public Why3CallResult execute(final TheoremTranslated input, final long timeout, IProofMonitor monitor, List<ToolConnection> jobs);
}
