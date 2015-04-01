package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.services.ValidationService;
import org.eventb.core.ast.Type;
import org.rodinp.keyboard.core.RodinKeyboardCore;

import uk.ac.ncl.eventb.why3.gen.pattern.PatternUtils;

public class ParameterValidationService extends ValidationService {

	@Override
	protected Status compute() {
		final PatternParameter parameter = (PatternParameter) context(Element.class);
		
		if (!parameter.getType().empty()) {
			String before = parameter.getType().content();
			String after = RodinKeyboardCore.translate(before);
			if (!before.equals(after)) {
				parameter.setType(after);
				return Status.createOkStatus();
			}
		}

		
		if (parameter.getId().empty() || parameter.getType().empty())
			return Status.createOkStatus();

		try {
			Type type = PatternUtils.parseType(parameter.getType().content());
			if (type == null)
				return Status.createErrorStatus("Invalid type expression");
		} catch (CoreException e) {
			return Status.createErrorStatus("Parse error: " + e.toString());
		}

		return Status.createOkStatus();
	}

}
