package com.why3.ws.scenario;

import java.util.List;

import org.eventb.core.seqprover.IProofMonitor;

import uk.ac.ncl.eventb.why3.driver.ToolConnection;
import uk.ac.ncl.eventb.why3.driver.Why3Tool;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;


public class ProverCall implements IScenarioAction {
	private ITheoremTransformer transformer = IdTransformer.INSTANCE;
	private String prover;
	
	public ProverCall(String prover) {
		this.prover = prover;
	}

	public ProverCall(ITheoremTransformer transformer, String prover) {
		this.prover = prover;
		this.transformer = transformer;
	}
	
	@Override
	public Why3CallResult execute(TheoremTranslated input, long timeout, final IProofMonitor monitor, final List<ToolConnection> jobs) {
		try {
			Why3Tool tool = new Why3Tool(prover, timeout, transformer.transform(input));
			if (jobs != null) 
				jobs.add(tool);
			return tool.check(timeout, monitor);
		} catch (Throwable e) {
			return Why3CallResult.INSTANCE_FAILURE;
		}
	}
	
	
}
