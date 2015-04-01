package uk.ac.ncl.eventb.why3.driver;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eventb.core.seqprover.IProofMonitor;

import uk.ac.ncl.eventb.why3.main.BTheorem;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.main.Why3Plugin;
import uk.ac.ncl.eventb.why3.tactics.ScenarioCache;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslator;

import com.why3.ws.scenario.IScenarioAction;
import com.why3.ws.scenario.Scenarios;


public class Why3LocalDriver implements IProverDriver {
	
	@Override
	public Why3CallResult check(BTheorem theorem, String scenario, IProofMonitor monitor) {
		try {
			if (Why3Plugin.hasWhy3LocalPath()) {
				IScenarioAction scen = Scenarios.SCENARIOS.get(scenario);
				if (scen == null)
					scen = ScenarioCache.INSTANCE.getScenarioByName(scenario);

				if (scen == null) {
					// MessageDialog.openError(null, "Why3 adapter", "Invalid scenario name: " + scenario);
					return Why3CallResult.INSTANCE_FAILURE;
				}
				
				if (Why3Plugin.DEBUG) {
					System.out.println("Starting scenario: " + scen);
				}					
				
				TheoremTranslator tt = new TheoremTranslator(theorem, Why3Plugin.isObfuscate());
				TheoremTranslated input = tt.translate();
				
				Why3CallResult result = scen.execute(input, Why3Plugin.getLocalTimeout(), monitor, null);

				if (Why3Plugin.DEBUG) {
					System.out.println("Finished scenario: " + scen);
				}	
				
				return result;
			} else {
				// TODO: check if interactive or log
				MessageDialog.openError(null, "Why3 adapter", "The path to local why3 is not configured properly");
				return Why3CallResult.INSTANCE_FAILURE;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return Why3CallResult.INSTANCE_FAILURE;
		}
	}

	@Override
	public Why3CallResult check(TheoremTranslated input, String scenario, IProofMonitor monitor) {
		try {
			if (Why3Plugin.hasWhy3LocalPath()) {
				IScenarioAction scen = Scenarios.SCENARIOS.get(scenario);
				if (scen == null)
					scen = ScenarioCache.INSTANCE.getScenarioByName(scenario);

				if (scen == null) {
					// MessageDialog.openError(null, "Why3 adapter", "Invalid scenario name: " + scenario);
					return Why3CallResult.INSTANCE_FAILURE;
				}
				
				if (Why3Plugin.DEBUG) {
					System.out.println("Starting scenario: " + scen);
				}					
				
				Why3CallResult result = scen.execute(input, Why3Plugin.getLocalTimeout(), monitor, null);

				if (Why3Plugin.DEBUG) {
					System.out.println("Finished scenario: " + scen);
				}	
				
				return result;
			} else {
				// TODO: check if interactive or log
				MessageDialog.openError(null, "Why3 adapter", "The path to local why3 is not configured properly");
				return Why3CallResult.INSTANCE_FAILURE;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return Why3CallResult.INSTANCE_FAILURE;
		}
	}	


}
