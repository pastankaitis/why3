package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.services.ValidationService;

public class RuleValidationService extends ValidationService {

	@Override
	protected Status compute() {
		final TranslationRule rule = (TranslationRule) context(Element.class);
		
		return Status.createOkStatus();
	}

}
