package uk.ac.ncl.eventb.why3.main;
import org.eventb.core.seqprover.IReasonerInputReader;
import org.eventb.core.seqprover.IReasonerInputWriter;
import org.eventb.core.seqprover.SerializeException;
import org.eventb.core.seqprover.xprover.XProverInput;

/**
 * Implementation of {@link XProverInput} for why3 prover connector.
 *
 * @author A. Iliasov
 *
 */
public class Why3Input extends XProverInput {

	private static final String SCENARIO = "scenario";
	private static final String CLOUD = "cloud";
	private String scenario = null; // proof scenario
	private boolean cloud ; // local or cloud call

	/**
	 * Creates a prover input for the why3 adapter
	 * @param restricted whether hypothesis selection is restricted to the current hypothesis or not
	 * @param timeOutDelay prover time out
	 * @param scenario a proof scenario name, pass null for default
	 * @param cloud true for using cloud provers, false for using a local why3 installation
	 */
	public Why3Input(boolean restricted, String scenario, boolean cloud) {
		super(restricted, 0); // control timeout ourselves
		if (scenario != null)
		this.scenario = (scenario != null) ? scenario : Why3Plugin.getDefaultScenario();
		this.cloud = cloud;
	}
	

	public Why3Input(IReasonerInputReader reader) throws SerializeException {
		super(reader);
		
		scenario = reader.getString(SCENARIO);
		cloud = Boolean.valueOf(reader.getString(CLOUD));
	}


	@Override
	protected void serialize(IReasonerInputWriter writer)
			throws SerializeException {
		super.serialize(writer);
		
		writer.putString(SCENARIO, scenario);
		writer.putString(CLOUD, Boolean.toString(cloud));
	}


	/**
	 * Prover scenario (which provers are called, in what order and with what timeouts)
	 * @return integer value representing prover effort
	 */
	public String getScenario() {
		return scenario;
	}

	/**
	 * Determines whether a cloud-based or local prover is used. Both options must be configured
	 * in the preferences
	 * @return true if a cloud call instance
	 */
	public boolean isCloud() {
		return cloud;
	}


}