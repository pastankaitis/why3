package uk.ac.ncl.eventb.why3.tactics;

import org.eventb.core.seqprover.ITactic;
import org.eventb.core.seqprover.eventbExtensions.AutoTactics.AbsractLazilyConstrTactic;
import org.eventb.core.seqprover.tactics.BasicTactics;

import uk.ac.ncl.eventb.why3.main.Why3Input;
import uk.ac.ncl.eventb.why3.main.Why3Plugin;
import uk.ac.ncl.eventb.why3.main.Why3Reasoner;

public class Why3DefaultScenarioRestricted extends AbsractLazilyConstrTactic {

	@Override
	protected ITactic getSingInstance() {
		return BasicTactics.reasonerTac(
				new Why3Reasoner(),
				new Why3Input(true, Why3Plugin.getDefaultScenario(), Why3Plugin.isCloud()));
	}

}