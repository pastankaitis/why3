package com.why3.ws.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.why3.ws.TheoremTranslated;
import com.why3.ws.connector.ToolConnection;
import com.why3.ws.connector.Why3CallResult;
import com.why3.ws.data.IProofEventFactory;

public class AndCompositionParallel implements IScenarioAction {
	private List<IScenarioAction> actions;
	private int workers;
	
	public static long tooltime;
	
	public AndCompositionParallel(int workers, List<IScenarioAction> actions) {
		this.workers = workers;
		this.actions = actions;
	}

	public AndCompositionParallel(int workers, IScenarioAction ... actions) {
		this.workers = workers;
		this.actions = new ArrayList<IScenarioAction>( Arrays.asList(actions));
	}
	
	public void addAction(IScenarioAction action) {
		if (action instanceof AndCompositionParallel) {
			AndCompositionParallel comp = (AndCompositionParallel) action;
			actions.addAll(comp.actions);
		} else {
			actions.add(action);
		}
	}	
	
	@SuppressWarnings("rawtypes")
	@Override
	public Why3CallResult execute(final TheoremTranslated input, final long timeout, final List<ToolConnection> jobs, final IProofEventFactory eventfactory) {
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(workers > 0 ? workers : Runtime.getRuntime().availableProcessors());
		List<Future<Why3CallResult>> futures = new ArrayList<Future<Why3CallResult>>(actions.size());
		
		for(final IScenarioAction action: actions) {
			Callable<Why3CallResult> task = new Callable<Why3CallResult>() {
				@Override
				public Why3CallResult call() {
					return action.execute(input, timeout, jobs, eventfactory);
				}

			};
			futures.add(pool.submit(task));
		}

		pool.shutdown();
		long time_start = System.currentTimeMillis();
		long timelimit = timeout;
		while (timelimit > 0) {
			try {
				pool.awaitTermination(timelimit, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				timelimit = timelimit
						- (System.currentTimeMillis() - time_start);
				tooltime = timelimit;
				time_start = System.currentTimeMillis();
			}
		}
		Why3CallResult adjudicated = Why3CallResult.INSTANCE_UNKNOWN;
		for(Future<Why3CallResult> f: futures) {
			if (f.isDone() && !f.isCancelled()) {
				try {
					adjudicated = adjudicated.adjudicate(f.get());
				} catch (InterruptedException | ExecutionException e) {
					// ignore
				}
			}
		}

		return adjudicated;
	}
}