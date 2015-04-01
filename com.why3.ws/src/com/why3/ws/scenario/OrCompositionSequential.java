package com.why3.ws.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.why3.ws.TheoremTranslated;
import com.why3.ws.connector.ToolConnection;
import com.why3.ws.connector.Why3CallResult;
import com.why3.ws.data.IProofEventFactory;

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
	public Why3CallResult execute(final TheoremTranslated input, final long timeout, final List<ToolConnection> jobs, final IProofEventFactory eventfactory) {
		for(IScenarioAction action: actions) {
			Why3CallResult result = action.execute(input, timeout, jobs, eventfactory);
			if (result.getStatus().isDefinite())
				return result;
		}
		return Why3CallResult.INSTANCE_UNKNOWN;
	}

}
