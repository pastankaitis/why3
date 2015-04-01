package uk.ac.ncl.eventb.why3.cloud;

import org.eventb.core.seqprover.IProofMonitor;

import uk.ac.ncl.eventb.why3.driver.IProverDriver;
import uk.ac.ncl.eventb.why3.driver.Why3Lemma;
import uk.ac.ncl.eventb.why3.main.BTheorem;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.main.Why3Plugin;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslator;


public class Why3CloudDriver implements IProverDriver {
	
	@Override
	public Why3CallResult check(BTheorem theorem, String scenario, IProofMonitor monitor) {
		try {
			TheoremTranslated model = getModel(theorem);
			if (model == null)
				return Why3CallResult.INSTANCE_FAILURE;
			
			long start = System.currentTimeMillis();
			String cloudURI = Why3Plugin.getCloudURI("run");
			CloudClient client = new CloudClient(cloudURI);
			Why3CallResult result = client.submit(model, scenario, Why3Plugin.getCloudTimeout(), monitor);
			long vtime = System.currentTimeMillis() - start;
			result.setTime2(vtime);
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
			return Why3CallResult.INSTANCE_FAILURE;
		}
	}	
	
	private TheoremTranslated getModel(BTheorem theorem) {
		try {
			TheoremTranslator tt = new TheoremTranslator(theorem, Why3Plugin.isObfuscate());
			return tt.translate();			
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}		
	}

	@Override
	public Why3CallResult check(TheoremTranslated model, String scenario, IProofMonitor monitor) {
		try {
			long start = System.currentTimeMillis();
			String cloudURI = Why3Plugin.getCloudURI("run");
			CloudClient client = new CloudClient(cloudURI);
			Why3CallResult result = client.submit(model, scenario, Why3Plugin.getCloudTimeout(), monitor);
			long vtime = System.currentTimeMillis() - start;
			result.setTime2(vtime);
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
			return Why3CallResult.INSTANCE_FAILURE;
		}
	}
}
