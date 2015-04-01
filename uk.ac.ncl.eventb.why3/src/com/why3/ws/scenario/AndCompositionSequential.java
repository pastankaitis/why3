package com.why3.ws.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eventb.core.seqprover.IProofMonitor;

import uk.ac.ncl.eventb.why3.driver.ToolConnection;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;


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
	public Why3CallResult execute(final TheoremTranslated input, final long timeout, final IProofMonitor monitor, List<ToolConnection> jobs) {
		long time_start = System.currentTimeMillis();
		Why3CallResult adjudicated = Why3CallResult.INSTANCE_UNKNOWN;
		for(IScenarioAction action: actions) {
			adjudicated = adjudicated.adjudicate(action.execute(input, timeout, monitor, jobs));
		}
		tooltime = System.currentTimeMillis() - time_start;
		return adjudicated;
	}

}
