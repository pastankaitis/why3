package uk.ac.ncl.eventb.why3.driver;

import org.eventb.core.seqprover.IProofMonitor;

import uk.ac.ncl.eventb.why3.main.BTheorem;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;


public interface IProverDriver {
	/**
	 * @param theorem a theorem to prove
	 * @param option a suggested scenario or prover name
	 * @return verification result
	 */
	public Why3CallResult check(BTheorem theorem, String scenario, IProofMonitor monitor);

	/**
	 * @param theorem a theorem to prove
	 * @param option a suggested scenario or prover name
	 * @return verification result
	 */
	public Why3CallResult check(TheoremTranslated input, String scenario, IProofMonitor monitor);
	
}
