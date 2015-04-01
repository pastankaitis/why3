package uk.ac.ncl.eventb.why3.tactics;

import org.eventb.core.seqprover.ITactic;
import org.eventb.core.seqprover.ITacticDescriptor;

public class ScenarioTacticDescriptor implements ITacticDescriptor {
	private boolean restricted;
	private String scenario;
	private boolean cloud;
	private ITactic tactic;
	
	public ScenarioTacticDescriptor(boolean restricted, String scenario, boolean cloud) {
		this.restricted = restricted;
		this.scenario = scenario;
		this.cloud = cloud;
		this.tactic = new Why3Tactic(restricted, scenario, cloud);
	}
	
	@Override
	public String getTacticID() {
		return scenario + "/" + (restricted ? "restricted" : "unrestricted") + "/" + (cloud ? "cloud" : "local");
	}

	@Override
	public String getTacticName() {
		return scenario + "/" + (restricted ? "restricted" : "unrestricted") + "/" + (cloud ? "cloud" : "local");
	}

	@Override
	public String getTacticDescription() {
		return (restricted ? "restricted " : "unrestricted ") + "scenario " +
				scenario + " running " + (cloud ? "on cloud" : "locally");
	}

	@Override
	public boolean isInstantiable() {
		return true;
	}

	@Override
	public ITactic getTacticInstance() {
		return tactic;
	}
}
