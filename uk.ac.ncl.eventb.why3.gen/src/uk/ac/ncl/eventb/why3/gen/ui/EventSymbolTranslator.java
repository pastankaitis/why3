package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.services.ValueNormalizationService;
import org.rodinp.keyboard.core.RodinKeyboardCore;

public class EventSymbolTranslator extends ValueNormalizationService {

	@Override
	public String normalize(String before) {
		if (before == null || before.length() == 0)
			return before;
		
		return RodinKeyboardCore.translate(before);
	}

}
