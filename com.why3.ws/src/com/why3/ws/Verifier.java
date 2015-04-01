/**
 * 
 */
package com.why3.ws;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.why3.ws.connector.Why3CallResult;
import com.why3.ws.data.EventProcessor;
import com.why3.ws.data.IProofEventFactory;
import com.why3.ws.data.ProofAttemptEvent;
import com.why3.ws.data.SessionContext;
import com.why3.ws.data.SessionInitiatedDataEvent;
import com.why3.ws.scenario.IScenarioAction;
import com.why3.ws.scenario.Scenarios;

public class Verifier {

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.why3.ws.data.EventProcessor");
			
			System.out.println("Service startup");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * MAIN verification method
	 * 
	 * @param json
	 * @return result
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws JSONException
	 * @throws InterruptedException
	 * @throws NoSuchAlgorithmException
	 */
	public Why3CallResult process(HttpServletRequest request, JSONObject json) throws IOException, URISyntaxException, JSONException, InterruptedException, NoSuchAlgorithmException {
		// start verification time
		//long start = System.currentTimeMillis();

		SessionContext sessionContext = new SessionContext();
		
		//System.out.println("step 0: " + (System.currentTimeMillis() - start));
		
		EventProcessor.INSTANCE.queue(new SessionInitiatedDataEvent(request.getRemoteHost(), json, sessionContext));

		//System.out.println("step 1: " + (System.currentTimeMillis() - start));
		
		String theoremTypes = json.getString("types");
		String theoremDefinition = json.getString("definitions");
		String theoremHypo = json.getString("hypothesis");
		String theoremGoal = json.getString("goal");

		//System.out.println("step 2: " + (System.currentTimeMillis() - start));
		
		TheoremTranslated theorem = new TheoremTranslated(theoremTypes, theoremDefinition, theoremHypo, theoremGoal);

		//System.out.println("step 3: " + (System.currentTimeMillis() - start));
		
		long timeout = json.getLong("timeout");
		String scenario = json.getString("scenario");

		//System.out.println("step 4: " + (System.currentTimeMillis() - start));
		
		// try to get a scenario by name
		IScenarioAction scen = Scenarios.SCENARIOS.get(scenario);
		if (scen == null)
			scen = Scenarios.DEFAULT_SCENARIO;

		//System.out.println("step 5: " + (System.currentTimeMillis() - start));
		
		IProofEventFactory factory = new ProofAttemptEvent.ProofAttemptEventFactory(sessionContext);

		//System.out.println("step 6: " + (System.currentTimeMillis() - start));
		
		// run scenario
		Why3CallResult result = scen.execute(theorem, timeout, null, factory);

		//System.out.println("step 7: " + (System.currentTimeMillis() - start));
		
		
		return result;
	}
}
