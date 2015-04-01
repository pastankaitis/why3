package uk.ac.ncl.eventb.why3.tactics;

import java.util.ArrayList;
import java.util.Collection;

import org.eventb.core.IPOSequent;
import org.eventb.core.seqprover.IProofTreeNode;
import org.eventb.core.seqprover.ITacticDescriptor;
import org.eventb.ui.prover.IUIDynTactic;
import org.eventb.ui.prover.IUIDynTacticProvider;

import uk.ac.ncl.eventb.why3.main.Why3Plugin;
import uk.ac.ncl.eventb.why3.prefpage.scen.ScenarioRegistry;

import com.why3.ws.scenario.Scenarios;

public class Why3DynTacticRestricted implements IUIDynTacticProvider {

	@Override
	public Collection<IUIDynTactic> getDynTactics(IProofTreeNode ptNode,
			IPOSequent poSequent) {
		final Collection<IUIDynTactic> tactics = new ArrayList<IUIDynTactic>();
		
		for(final String scen: Scenarios.SCENARIOS.keySet()) {
			final ITacticDescriptor desc = new ScenarioTacticDescriptor(true, scen, Why3Plugin.isCloud());

			tactics.add(new IUIDynTactic() {

				@Override
				public String getName() {
					return scen;
				}

				@Override
				public ITacticDescriptor getTacticDescriptor() {
					return desc;
				}
			});			
		}
		
		for(final String scen: ScenarioRegistry.getInstance().keySet()) {
			final ITacticDescriptor desc = new ScenarioTacticDescriptor(true, scen, Why3Plugin.isCloud());

			tactics.add(new IUIDynTactic() {

				@Override
				public String getName() {
					return scen;
				}

				@Override
				public ITacticDescriptor getTacticDescriptor() {
					return desc;
				}
			});			
		}		
		
		return tactics;
	}

}
