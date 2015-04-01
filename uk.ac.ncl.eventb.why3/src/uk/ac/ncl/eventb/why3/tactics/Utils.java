package uk.ac.ncl.eventb.why3.tactics;

import org.eventb.core.seqprover.ITactic;
import org.eventb.core.seqprover.tactics.BasicTactics;

import uk.ac.ncl.eventb.why3.main.Why3Input;
import uk.ac.ncl.eventb.why3.main.Why3Reasoner;

public class Utils {
	
	public static ITactic Why3Tactic(boolean restricted, String scenario, boolean cloud) {
		return BasicTactics.reasonerTac(
				new Why3Reasoner(),
				new Why3Input(restricted, scenario, cloud));
	}
}
