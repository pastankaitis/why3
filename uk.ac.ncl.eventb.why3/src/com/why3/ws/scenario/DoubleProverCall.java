package com.why3.ws.scenario;

import java.util.List;

import org.eventb.core.seqprover.IProofMonitor;

import uk.ac.ncl.eventb.why3.driver.ToolConnection;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;


/**
 * Prove the same theorem in postive and negative forms and then adjudicate on the result
 * @author alex
 *
 */
public class DoubleProverCall implements IScenarioAction {
	private IScenarioAction action;
	public DoubleProverCall(String prover) {
		this.action = new AndCompositionSequential(
				new ProverCall(prover),
				new TheoremNegation(new ProverCall(GoalNegationTransformer.INSTANCE, prover))
				);
	}
	
	@Override
	public Why3CallResult execute(TheoremTranslated input, long timeout, final IProofMonitor monitor, List<ToolConnection> jobs) {
		try {
			return action.execute(input, timeout, monitor, jobs);
		} catch (Throwable e) {
			return Why3CallResult.INSTANCE_FAILURE;
		}
	}

}
