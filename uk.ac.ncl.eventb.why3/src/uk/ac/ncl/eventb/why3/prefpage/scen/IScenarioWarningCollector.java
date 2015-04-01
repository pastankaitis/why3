package uk.ac.ncl.eventb.why3.prefpage.scen;

import com.why3.ws.scenario.ScenarioException;

import uk.ac.ncl.pparser.syntree;

public interface IScenarioWarningCollector {
	void warning(String warning, syntree parsed) throws ScenarioException;
}
