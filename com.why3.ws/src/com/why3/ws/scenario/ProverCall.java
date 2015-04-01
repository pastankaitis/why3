package com.why3.ws.scenario;

import java.util.List;

import com.why3.ws.TheoremTranslated;
import com.why3.ws.connector.ToolConnection;
import com.why3.ws.connector.Why3CallResult;
import com.why3.ws.connector.Why3Tool;
import com.why3.ws.data.EventProcessor;
import com.why3.ws.data.IProofEventFactory;

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
	public Why3CallResult execute(TheoremTranslated input, long timeout, final List<ToolConnection> jobs, final IProofEventFactory eventfactory) {
		try {
			Why3Tool tool = new Why3Tool(prover, timeout, transformer.transform(input));
			if (jobs != null) 
				jobs.add(tool);			
			Why3CallResult result = tool.check();
			EventProcessor.INSTANCE.queue(eventfactory.create(prover, 0, result));
			return result;
		} catch (Throwable e) {
			return Why3CallResult.INSTANCE_FAILURE;
		}
	}
	
	
}
