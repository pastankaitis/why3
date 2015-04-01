package uk.ac.ncl.eventb.why3.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.sapphire.ElementList;

import uk.ac.ncl.eventb.why3.gen.opregistry.OperatorRegistry;
import uk.ac.ncl.eventb.why3.gen.ui.ITranslationElement;
import uk.ac.ncl.eventb.why3.gen.ui.Trigger;
import uk.ac.ncl.eventb.why3.gen.ui.TriggerKind;

public class Triggers {
	private static final long GOAL_FLAG = 1<<63;
	
	public List<Long> computeTriggerCache(ITranslationElement element) throws GenException {
		ElementList<Trigger> list = element.getTriggers();
		long plainbitset = 0;
		
		if (element.getTriggerKind().content() != TriggerKind.CUSTOM)
			return null;

		if (!element.isTriggerGoalOnly().empty()) {
			plainbitset = element.isTriggerGoalOnly().content() ? GOAL_FLAG : 0;
		}
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>(list.size());
		for(Trigger t: list) {
			String v = t.getSpec().content().trim();
			if (v.indexOf(',') >= 0) {
				String[] parts = v.split(",");
				if (parts.length != 2)
					throw new GenException("Invalid trigger pattern '" + v + "'");
				String p1 = parts[0].trim();
				String p2 = parts[1].trim();
				if (p1.length() == 0 || p2.length() == 0)
					throw new GenException("Invalid trigger pattern '" + v + "'");
				int code1 = OperatorRegistry.symbolToCode(p1);
				if (code1 < 0 || code1 > 63)
					throw new GenException("Invalid trigger symbol '" + p1 + "'");
				int code2 = OperatorRegistry.symbolToCode(p2);
				if (code2 < 0 || code2 > 63)
					throw new GenException("Invalid trigger symbol '" + p2 + "'");
				map.put(code1, code2);
			} else {
				int code = OperatorRegistry.symbolToCode(v);
				if (code < 0 || code > 63) {
					throw new GenException("Invalid trigger symbol '" + v + "'");
				}
				plainbitset |= 1 << code;
			}
		}
		
		List<Long> result = new ArrayList<Long>(map.size() + 1);
		result.add(plainbitset);
		
		for(Integer key: map.keySet()) {
			Integer value = map.get(key);
			int from = key;
			int to = value;
			long z = ((from << 6) | to) << 6;
			result.add(z);
		}
		
		return result;
	}
	
	
}
