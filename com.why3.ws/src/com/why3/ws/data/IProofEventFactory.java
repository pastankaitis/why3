package com.why3.ws.data;

import com.why3.ws.connector.Why3CallResult;

public interface IProofEventFactory {
	public DataEvent create(String prover, long context, Why3CallResult result);
}
