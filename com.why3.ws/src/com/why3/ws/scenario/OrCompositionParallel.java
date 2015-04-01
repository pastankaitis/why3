package com.why3.ws.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.why3.ws.TheoremTranslated;
import com.why3.ws.connector.ToolConnection;
import com.why3.ws.connector.Why3CallResult;
import com.why3.ws.data.IProofEventFactory;

public class OrCompositionParallel implements IScenarioAction {
	private List<IScenarioAction> actions;
	private int workers;
	
	public OrCompositionParallel(int workers, List<IScenarioAction> actions) {
		this.workers = workers;
		this.actions = actions;
	}

	public OrCompositionParallel(int workers, IScenarioAction ... actions) {
		this.workers = workers;
		this.actions = new ArrayList<IScenarioAction>( Arrays.asList(actions));
	}
	
	public void addAction(IScenarioAction action) {
		if (action instanceof OrCompositionParallel) {
			OrCompositionParallel comp = (OrCompositionParallel) action;
			actions.addAll(comp.actions);
		} else {
			actions.add(action);
		}
	}	
	
	@Override
	public Why3CallResult execute(final TheoremTranslated input, final long timeout, final List<ToolConnection> jobs, final IProofEventFactory eventfactory) {
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(workers > 0 ? workers : Runtime.getRuntime().availableProcessors());
		
		final Message message = new Message();
		
		final List<ToolConnection> jobs_new = Collections.synchronizedList(new ArrayList<ToolConnection>());		
		
		for(final IScenarioAction action: actions) {
			Runnable task = new Runnable() {
				@Override
				public void run() {
					Why3CallResult result = action.execute(input, timeout, jobs_new, eventfactory);
					synchronized(message) {
						message.setResult(result);
						message.notify();
					}
				}

			};
			pool.submit(task);
		}
		
		// collect
		long time_start = System.currentTimeMillis();
		long timelimit = timeout;
		synchronized(message) {
			while (!message.getResult().getStatus().isDefinite() && pool.getActiveCount() > 0 && timelimit > 0) {
				try {
					message.wait(timeout);
				} catch (InterruptedException e) {
					timelimit = timelimit - (System.currentTimeMillis() - time_start);
					time_start = System.currentTimeMillis();
				}
			}
			pool.shutdownNow();
		}

		
		// destroy all the running processes
		for(ToolConnection t: jobs_new)
			t.stop();		
		
		return message.getResult();
	}

	class Message {
		private Why3CallResult result = Why3CallResult.INSTANCE_UNKNOWN;

		public void setResult(Why3CallResult result) {
			this.result = result;
		}

		public Why3CallResult getResult() {
			return result;
		}
	}
}
