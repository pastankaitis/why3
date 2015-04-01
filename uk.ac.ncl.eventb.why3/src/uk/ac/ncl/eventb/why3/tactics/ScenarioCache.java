package uk.ac.ncl.eventb.why3.tactics;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.eventb.why3.prefpage.scen.ScenarioBuilder;
import uk.ac.ncl.eventb.why3.prefpage.scen.ScenarioRegistry;

import com.why3.ws.scenario.IScenarioAction;
import com.why3.ws.scenario.ScenarioException;

public class ScenarioCache {
	public static final ScenarioCache INSTANCE = new ScenarioCache();

	private List<ScenarioCached> scenarioCache;

	private ScenarioCache() {
		scenarioCache = new ArrayList<ScenarioCached>(50);
	}

	public synchronized IScenarioAction getScenarioByName(String id) {
		// check if in cache
		try {
			for (ScenarioCached s : scenarioCache) {
				IScenarioAction r = s.match(id);
				if (r != null)
					return r;
			}

			ScenarioCached s = new ScenarioCached(id);
			scenarioCache.add(s);

			return s.getBody();
		} catch (TacticException e) {
			e.printStackTrace();
			return null;
		}
	}

	List<ScenarioCached> getScenarioCache() {
		return scenarioCache;
	}
	
}

class ScenarioCached {
	private IScenarioAction body;
	private String scenarioName;
	private int scenarioHash;

	ScenarioCached(String scenId) throws TacticException {
		builAction(scenId);
	}

	private void builAction(String scenId) throws TacticException {
		String scenario = ScenarioRegistry.getInstance().get(scenId);
		if (scenario == null)
			throw new TacticException("Invalid scenario name: " + scenId);

		try {
			body = ScenarioBuilder.parse(scenario);
			scenarioName = scenId;
			scenarioHash = scenario.hashCode();
		} catch (ScenarioException e) {
			throw new TacticException(e.getMessage());
		}
	}

	public IScenarioAction match(String scenId) throws TacticException {
		if (scenId.equals(scenarioName)) {
			String scenario = ScenarioRegistry.getInstance().get(scenId);
			int hashCode = scenario.hashCode();
			if (hashCode == scenarioHash) {
				return body;
			} else {
				builAction(scenId);
				return body;
			}
		}

		return null;
	}

	IScenarioAction getBody() {
		return body;
	}

	String getScenarioName() {
		return scenarioName;
	}

	
	
}