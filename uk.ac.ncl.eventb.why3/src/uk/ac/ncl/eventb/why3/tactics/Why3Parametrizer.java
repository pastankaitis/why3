package uk.ac.ncl.eventb.why3.tactics;

import org.eventb.core.seqprover.IParameterValuation;
import org.eventb.core.seqprover.ITactic;
import org.eventb.core.seqprover.ITacticParameterizer;

public class Why3Parametrizer implements ITacticParameterizer {

	// label for the 'restricted' tactic parameter
	private static final String RESTRICTED = "restricted";

	// label for the 'cloud' tactic parameter
	private static final String CLOUD = "cloud";

	// label for the 'cloud' tactic parameter
	private static final String SCENARIO = "scenario";
	
	
	@Override
	public ITactic getTactic(IParameterValuation parameters) {
		final boolean restricted = parameters.getBoolean(RESTRICTED);
		final boolean cloud = parameters.getBoolean(CLOUD);
		final String scenario = parameters.getString(SCENARIO);
		return Utils.Why3Tactic(restricted, scenario, cloud);
	}
	
}