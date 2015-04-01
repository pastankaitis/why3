package com.why3.ws.data;

import com.why3.ws.connector.Why3CallResult;

public class ProofAttemptEvent extends DataEvent {
	private SessionContext sessionContext;
	private long duration;
	private int status;
	private int prover;	
	private long context;

	private ProofAttemptEvent(SessionContext sessionContext, long context, int prover, int status, long duration) {
		this.sessionContext = sessionContext;
		this.prover = prover;
		this.status = status;
		this.duration = duration;
		this.context = 0;
	}

	@Override
	public void commit(DatabaseConnector connector) throws Exception {
		long theorem = sessionContext.getLong("theorem");
		long user = sessionContext.getLong("uuid");
		connector.insertAttempt(theorem, context, prover, user, status, duration);
	}

	public static class ProofAttemptEventFactory implements IProofEventFactory {
		private SessionContext sessionContext;
		private long context;
		
		public ProofAttemptEventFactory(SessionContext sessionContext) {
			this.sessionContext = sessionContext;
			this.context = 0;
		}

		public ProofAttemptEventFactory(ProofAttemptEventFactory parent, long context) {
			this.sessionContext = parent.sessionContext;
			this.context = context;
		}

		@Override
		public DataEvent create(String proverName, long context, Why3CallResult result) {
			long duration = result.getTime();
			int status = result.getStatus().getCode();
			int prover = ProverRegistry.getProverId(proverName);
			return new ProofAttemptEvent(sessionContext, context, prover, status, duration);
		}
		
	}

	
}
