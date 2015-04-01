package com.why3.ws.scenario;

import java.util.List;

import com.why3.ws.TheoremTranslated;
import com.why3.ws.connector.ToolConnection;
import com.why3.ws.connector.Why3CallResult;
import com.why3.ws.data.IProofEventFactory;

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
	public Why3CallResult execute(TheoremTranslated input, long timeout, final List<ToolConnection> jobs, final IProofEventFactory eventfactory) {
		try {
			return action.execute(input, timeout, jobs, eventfactory);
		} catch (Throwable e) {
			return Why3CallResult.INSTANCE_FAILURE;
		}
	}

}
