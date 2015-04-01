package uk.ac.ncl.eventb.why3.tactics;

import org.eventb.core.seqprover.ITactic;
import org.eventb.core.seqprover.eventbExtensions.AutoTactics.AbsractLazilyConstrTactic;
import org.eventb.core.seqprover.tactics.BasicTactics;

import uk.ac.ncl.eventb.why3.main.Why3Input;
import uk.ac.ncl.eventb.why3.main.Why3Reasoner;

public class Why3Tactic extends AbsractLazilyConstrTactic {
	private boolean restricted;
	private String scenario;
	private boolean cloud;

	public Why3Tactic(boolean restricted, String scenario, boolean cloud) {
		this.restricted = restricted;
		this.scenario = scenario;
		this.cloud = cloud;
	}

	@Override
	protected ITactic getSingInstance() {
		return BasicTactics.reasonerTac(new Why3Reasoner(), new Why3Input(
				restricted, scenario, cloud));
	}
}
