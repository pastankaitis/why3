package uk.ac.ncl.eventb.why3.main;

import org.eventb.core.seqprover.IProofMonitor;
import org.eventb.core.seqprover.transformer.ISimpleSequent;
import org.rodinp.core.RodinDBException;

import uk.ac.ncl.eventb.why3.cloud.Why3CloudDriver;
import uk.ac.ncl.eventb.why3.driver.IProverDriver;
import uk.ac.ncl.eventb.why3.driver.Why3LocalDriver;

/**
 * 
 * @author A. Iliasov
 */
public class Why3Connector {
	private String scenario;
	private ISimpleSequent sequent;
	private IProofMonitor monitor;
	private IProverDriver driver;
	
	public Why3Connector(ISimpleSequent sequent, String scenario, boolean cloud, IProofMonitor monitor) {
		this.sequent = sequent;
		this.monitor = monitor;
		this.scenario = scenario;
		if (cloud) {
			driver = new Why3CloudDriver();
		} else {
			driver = new Why3LocalDriver();
		}
	}

	/**
	 * Call why3 (either local or cloud) with the translation result
	 * @return
	 */
	public Why3CallResult invoke() throws Why3ToolException{
		
		try {
			BTheorem theorem = new BTheorem(sequent);
			return driver.check(theorem, scenario, monitor);
		} catch (RodinDBException e) {
			throw new Why3ToolException(e.getMessage());
		}
	}
}
