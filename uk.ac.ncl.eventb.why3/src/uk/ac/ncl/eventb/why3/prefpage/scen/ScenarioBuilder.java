package uk.ac.ncl.eventb.why3.prefpage.scen;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.eventb.why3.driver.ProverIds;
import uk.ac.ncl.pparser.ErrInfo;
import uk.ac.ncl.pparser.alphabet;
import uk.ac.ncl.pparser.syntree;

import com.why3.ws.scenario.AndCompositionParallel;
import com.why3.ws.scenario.AndCompositionSequential;
import com.why3.ws.scenario.GoalNegationTransformer;
import com.why3.ws.scenario.IScenarioAction;
import com.why3.ws.scenario.ITheoremTransformer;
import com.why3.ws.scenario.IdTransformer;
import com.why3.ws.scenario.OrCompositionParallel;
import com.why3.ws.scenario.OrCompositionSequential;
import com.why3.ws.scenario.ProverCall;
import com.why3.ws.scenario.ScenarioException;
import com.why3.ws.scenario.TheoremNegation;
import com.why3.ws.scenario.Timeout;

public class ScenarioBuilder {

	public static IScenarioAction build(syntree parsed, boolean interactive, IScenarioWarningCollector warnings) throws ScenarioException {
		switch (parsed.op()) {
		case alphabet.AND_PAR: {
			IScenarioAction left = build(parsed.sibling(0), interactive, warnings);
			IScenarioAction right = build(parsed.sibling(1), interactive, warnings);
			if (left instanceof AndCompositionParallel) {
				AndCompositionParallel and_comp = (AndCompositionParallel) left;
				and_comp.addAction(right);
				return and_comp;
			} else {
				return new AndCompositionParallel(-1, left, right);
			}
		}
		case alphabet.AND_SEQ: {
			IScenarioAction left = build(parsed.sibling(0), interactive, warnings);
			IScenarioAction right = build(parsed.sibling(1), interactive, warnings);
			if (left instanceof AndCompositionSequential) {
				AndCompositionSequential and_comp = (AndCompositionSequential) left;
				and_comp.addAction(right);
				return and_comp;
			} else {
				return new AndCompositionSequential(left, right);
			}
		}
		case alphabet.OR_PAR: {
			IScenarioAction left = build(parsed.sibling(0), interactive, warnings);
			IScenarioAction right = build(parsed.sibling(1), interactive, warnings);
			if (left instanceof OrCompositionParallel) {
				OrCompositionParallel and_comp = (OrCompositionParallel) left;
				and_comp.addAction(right);
				return and_comp;
			} else {
				return new OrCompositionParallel(-1, left, right);
			}
		}
		case alphabet.OR_SEQ: {
			IScenarioAction left = build(parsed.sibling(0), interactive, warnings);
			IScenarioAction right = build(parsed.sibling(1), interactive, warnings);
			if (left instanceof OrCompositionSequential) {
				OrCompositionSequential and_comp = (OrCompositionSequential) left;
				and_comp.addAction(right);
				return and_comp;
			} else {
				return new OrCompositionSequential(left, right);
			}
		}
		case alphabet.UPTO: {
			IScenarioAction left = build(parsed.sibling(0), interactive, warnings);
			int timeout = (Integer) parsed.value().getValue();
			return new Timeout(left, timeout);
		}
		case alphabet.PROVER_CALL: {
			String proverId = parsed.value().toString();
			
			if (interactive)
				checkProverId(parsed, proverId, warnings);
			
			if (parsed.siblings() > 0) {
				ITheoremTransformer transformer = buildTransformer(parsed
						.sibling(0));
				return new ProverCall(transformer, proverId);
			} else {
				return new ProverCall(proverId);
			}
		}
		case alphabet.NOT: {
			IScenarioAction left = build(parsed.sibling(0), interactive, warnings);
			return new TheoremNegation(left);
		}
		default:
			throw new ScenarioException(parsed, "Invalid scenario syntax");
		}
	}

	private static void checkProverId(syntree parsed, String prover, IScenarioWarningCollector warnings)
			throws ScenarioException {
		if (!ProverIds.isPossibleId(prover))
			throw new ScenarioException(parsed,
					"Invalid prover name; possible names are "
							+ ProverIds.getPossibleIds());

		if (!ProverIds.isValidLocalId(prover))
			warnings.warning(
					"Not a valid local prover id; known local provers are "
							+ ProverIds.getLocalIds(), parsed);

		if (!ProverIds.isValidRemoteId(prover))
			warnings.warning(
					"Not a valid cloud prover id; known cloud provers are "
							+ ProverIds.getRemoteIds(), parsed);

	}

	private static ITheoremTransformer buildTransformer(syntree parsed)
			throws ScenarioException {
		switch (parsed.op()) {
		case alphabet.INPUT:
			return IdTransformer.INSTANCE;
		case alphabet.GOAL_NOT:
			return GoalNegationTransformer.INSTANCE;
		default:
			throw new ScenarioException(parsed, "Invalid theorem transformer");
		}
	}

	public static IScenarioAction parse(String string, boolean interactive, IScenarioWarningCollector warnings) throws ScenarioException {
		try {
			InputStream is = new ByteArrayInputStream(string.getBytes());
			List<ErrInfo> info = new ArrayList<ErrInfo>(20);
			syntree result = uk.ac.ncl.pparser.Parser.parse(is, info);
			if (!info.isEmpty()) {
				throw new ScenarioException(null, "Parse failed");
			}

			return build(result, interactive, warnings);
		} catch (Exception e) {
			throw new ScenarioException(null, e.getMessage());
		}
	}
	
	public static IScenarioAction parse(String string) throws ScenarioException {
		return parse(string, false, WarningIgnore.INSTANCE);
	}	
}

class WarningIgnore implements IScenarioWarningCollector {
	static final WarningIgnore INSTANCE = new WarningIgnore();
	
	private WarningIgnore() {}
	
	@Override
	public void warning(String warning, syntree parsed) throws ScenarioException {
		// do nothing
	}
}
