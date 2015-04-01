package com.why3.ws.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.why3.ws.TheoremTranslated;
import com.why3.ws.connector.ToolConnection;
import com.why3.ws.connector.Why3CallResult;
import com.why3.ws.data.IProofEventFactory;

public class AndCompositionSequential implements IScenarioAction {
	private List<IScenarioAction> actions;
	
	public static long tooltime;
	
	public AndCompositionSequential(List<IScenarioAction> actions) {
		this.actions = actions;
	}

	public AndCompositionSequential(IScenarioAction ... actions) {
		this.actions = new ArrayList<IScenarioAction>( Arrays.asList(actions));
	}
	
	public void addAction(IScenarioAction action) {
		if (action instanceof AndCompositionSequential) {
			AndCompositionSequential comp = (AndCompositionSequential) action;
			actions.addAll(comp.actions);
		} else {
			actions.add(action);
		}
	}	
	
	@Override
	public Why3CallResult execute(final TheoremTranslated input, final long timeout, final List<ToolConnection> jobs, final IProofEventFactory eventfactory) {
		long time_start = System.currentTimeMillis();
		Why3CallResult adjudicated = Why3CallResult.INSTANCE_UNKNOWN;
		for(IScenarioAction action: actions) {
			adjudicated = adjudicated.adjudicate(action.execute(input, timeout, jobs, eventfactory));
		}
		tooltime = System.currentTimeMillis() - time_start;
		return adjudicated;
	}

}
