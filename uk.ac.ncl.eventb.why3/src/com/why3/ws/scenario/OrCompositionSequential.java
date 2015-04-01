package com.why3.ws.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eventb.core.seqprover.IProofMonitor;

import uk.ac.ncl.eventb.why3.driver.ToolConnection;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;


public class OrCompositionSequential implements IScenarioAction {
	private List<IScenarioAction> actions;
	
	public OrCompositionSequential(List<IScenarioAction> actions) {
		this.actions = actions;
	}

	public OrCompositionSequential(IScenarioAction ... actions) {
		this.actions = new ArrayList<IScenarioAction>( Arrays.asList(actions)); 
	}
	
	public void addAction(IScenarioAction action) {
		if (action instanceof OrCompositionSequential) {
			OrCompositionSequential comp = (OrCompositionSequential) action;
			actions.addAll(comp.actions);
		} else {
			actions.add(action);
		}
	}	
	
	@Override
	public Why3CallResult execute(final TheoremTranslated input, final long timeout, final IProofMonitor monitor, final List<ToolConnection> jobs) {
		for(IScenarioAction action: actions) {
			Why3CallResult result = action.execute(input, timeout, monitor, jobs);
			if (result.getStatus().isDefinite())
				return result;
		}
		return Why3CallResult.INSTANCE_UNKNOWN;
	}

}
