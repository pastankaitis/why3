package uk.ac.ncl.eventb.why3.main;

import org.eventb.core.seqprover.IProofMonitor;
import org.eventb.core.seqprover.IReasonerInput;
import org.eventb.core.seqprover.IReasonerInputReader;
import org.eventb.core.seqprover.IVersionedReasoner;
import org.eventb.core.seqprover.SerializeException;
import org.eventb.core.seqprover.transformer.ISimpleSequent;
import org.eventb.core.seqprover.xprover.XProverCall2;
import org.eventb.core.seqprover.xprover.XProverInput;
import org.eventb.core.seqprover.xprover.XProverReasoner2;

/**
 * Why3 connector reasoner interface
 * @author A. Iliasov
 *
 */
public class Why3Reasoner extends XProverReasoner2 implements IVersionedReasoner {
	public static final String REASONER_ID = "uk.ac.ncl.eventb.why3.why3";
	public static final int VERSION = 1;
	
	@Override
	public String getReasonerID() {
		return REASONER_ID;
	}

	@Override
	public XProverCall2 newProverCall(IReasonerInput input, ISimpleSequent sequent, IProofMonitor pm) {
		return new Why3Call((XProverInput)input, sequent, pm);
	}

	@Override
	public IReasonerInput deserializeInput(IReasonerInputReader reader) throws SerializeException {
		return new Why3Input(reader);
	}	
	
	@Override
	public int getVersion() {
		return VERSION;
	}

}
