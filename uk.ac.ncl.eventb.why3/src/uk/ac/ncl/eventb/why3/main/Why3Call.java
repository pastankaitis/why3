package uk.ac.ncl.eventb.why3.main;
import java.util.concurrent.CancellationException;

import org.eventb.core.seqprover.IProofMonitor;
import org.eventb.core.seqprover.transformer.ISimpleSequent;
import org.eventb.core.seqprover.xprover.XProverCall2;
import org.eventb.core.seqprover.xprover.XProverInput;

import uk.ac.ncl.eventb.why3.main.Why3CallResult.WHY3_RESULT;

/**
 * Implementation of {@link XProverCall2} for the why3 connector.
 * 
 * @author A. Iliasov
 * 
 */
public class Why3Call extends XProverCall2 {

	private String scenario;
	private boolean cloud;
	private Why3CallResult result;
	private IProofMonitor pm;

	public Why3Call(XProverInput input, ISimpleSequent sequent, IProofMonitor pm) {
		super(sequent, pm);
		scenario = ((Why3Input) input).getScenario();
		cloud = ((Why3Input) input).isCloud();
		this.pm = pm;
	}

	@Override
	public void cleanup() {
		// do nothing for now
	}

	@Override
	public String displayMessage() {
		return result.toString();
	}

	@Override
	public boolean isValid() {
		if (result == null) {
			return false;
		}
		return result.getStatus() == WHY3_RESULT.VALID;
	}

	@Override
	public void run() {
		try {			
			Why3Connector connector = new Why3Connector(sequent, scenario, cloud, pm);
			result = connector.invoke();
		} catch (CancellationException | Why3ToolException e) {
			result = Why3CallResult.INSTANCE_CANCEL;
		}
	}

}