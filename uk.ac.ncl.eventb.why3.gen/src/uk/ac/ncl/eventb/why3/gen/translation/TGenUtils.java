package uk.ac.ncl.eventb.why3.gen.translation;

import org.eclipse.sapphire.Element;

import uk.ac.ncl.eventb.why3.gen.GenException;
import uk.ac.ncl.eventb.why3.gen.opregistry.EventBOperator;
import uk.ac.ncl.eventb.why3.gen.opregistry.OperatorRegistry;
import uk.ac.ncl.eventb.why3.gen.ui.ConditionKind;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationCondition;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationRule;

public class TGenUtils {
	public static String insertCondition(TranslationCondition condition) throws GenException {
		check(condition);
		
		if (condition.isChecked().empty() || !condition.isChecked().content())
			throw new GenException("Condition " + condition.getId() + " is not statically checked");
		
		StringBuilder sb = new StringBuilder(); 
		
		if (condition.getKind().content() == ConditionKind.LEMMA) {
		//	if (condition.isValid().empty() || !condition.isValid().content())
		//		throw new GenException("Lemma " + condition.getId() + " is not verified");
			sb.append("\tlemma ");
		} else {
			sb.append("\taxiom ");
		}
		
		String property = condition.getBody().content().replaceAll("[\\t\\n\\r]", " ");
		
		sb.append(condition.getId());
		sb.append(" :\n");
		ppMultiline(sb, property, "\t\t");
		sb.append("\n");
		return sb.toString();
	}
	
	private static void ppMultiline(StringBuilder target, String text, String prefix) {
		String parts[] = text.split("\\n");
		for(String z: parts) {
			String t = z.trim();
			if (t.length() > 0) {
				target.append(prefix);
				target.append(t);
				target.append("\n");
			}
		}
	}

	public static String ruleString(TranslationRule rule) throws GenException {
		check(rule);
		if (rule.getSource().empty())
			throw new GenException("Rule source is undefined");
		if (rule.getTarget().empty())
			throw new GenException("Rule " + rule.getSource() + " target is undefined");
		
		EventBOperator operator = OperatorRegistry.getOperator(rule.getSource().content());
		String name = rule.getTarget().content();
		String definition = rule.getBody().empty() ? null : rule.getBody().content();
		return operator.toString(name, definition);		
	}
	
	public static void check(Element element) throws GenException {
		if (!element.validation().ok()) {
			throw new GenException("Translation aborted due to validation errors");
		}
	}
}
