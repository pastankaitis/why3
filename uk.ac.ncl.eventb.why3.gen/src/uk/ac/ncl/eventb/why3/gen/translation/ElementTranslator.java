package uk.ac.ncl.eventb.why3.gen.translation;

import org.eclipse.sapphire.Element;

import uk.ac.ncl.eventb.why3.gen.GenException;
import uk.ac.ncl.eventb.why3.gen.MinimalContextProvider;
import uk.ac.ncl.eventb.why3.gen.opregistry.EventBOperator;
import uk.ac.ncl.eventb.why3.gen.opregistry.OperatorRegistry;
import uk.ac.ncl.eventb.why3.gen.ui.ConditionKind;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationCatalog;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationCondition;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationPattern;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationRule;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;

public class ElementTranslator {
	private StringBuilder imports;
	private StringBuilder types;
	private StringBuilder definitions;
	private StringBuilder hypothesis;
	private StringBuilder goal;	
	
	private ElementTranslator() {
		imports = new StringBuilder();
		types = new StringBuilder();
		definitions = new StringBuilder();
		hypothesis = new StringBuilder();
		goal = new StringBuilder();
	}

	public static TheoremTranslated fromElement(Element element) throws GenException {
		if (element instanceof TranslationRule) {
			return fromRule((TranslationRule) element);
		} else if (element instanceof TranslationCondition) {
			return fromCondition((TranslationCondition) element);
		} else {
			throw new GenException("Cannot translate this element: " + element.getClass().getSimpleName());
		}
	}

	
	public static TheoremTranslated fromRule(TranslationRule rule) throws GenException {
		TranslationCatalog catalog = (TranslationCatalog) rule.parent().element();
		ElementTranslator translator = new ElementTranslator();
		translator.translate(catalog, rule);
		return translator.getResult();
	}
	
	
	public static TheoremTranslated fromPattern(TranslationPattern pattern) throws GenException {
		return null;
	}	
	
	public static TheoremTranslated fromCondition(TranslationCondition condition) throws GenException {
		Element parent = condition.parent().element();
		TranslationCatalog catalog;
		if (parent instanceof TranslationCatalog) {
			catalog = (TranslationCatalog) parent;
		} else if (parent instanceof TranslationRule) {
			TranslationRule rule = (TranslationRule) parent;
			catalog = (TranslationCatalog) rule.parent().element();
		} else {
			throw new GenException("Malformed catalog structure");
		}
		ElementTranslator translator = new ElementTranslator();
		translator.translate(catalog, condition);
		return translator.getResult();
	}	
	
	
	private TheoremTranslated getResult() throws GenException {
		TheoremTranslated result = new TheoremTranslated(types.length() > 0 ? types.toString() : null, 
														definitions.length() > 0 ? definitions.toString() : null, 
														hypothesis.length() > 0 ? hypothesis.toString() : null, 
														goal.length() > 0 ? goal.toString() : null);
		if (imports.length() > 0) result.setImports(imports.toString());
		result.setContext(MinimalContextProvider.INSTANCE);
		return result;
	}	
	
	private void translate(TranslationCatalog catalog, TranslationRule rule) throws GenException {
		for(TranslationRule z: catalog.getRules()) {
			if (z == rule)
				break;
			insertRuleFull(z);
		}
		
		insertRule(rule);
	}
	
	private void translate(TranslationCatalog catalog, TranslationCondition condition) throws GenException {
		
		if (condition.parent().element() instanceof TranslationRule) {
			TranslationRule parent = (TranslationRule) condition.parent().element();
			
			for(TranslationRule rule: catalog.getRules()) {
				if (rule == parent)
					break;
				insertRuleFull(rule);
			}
			
			insertRule(parent);
			
			for(TranslationCondition tc: parent.getConditions()) {
				if (tc == condition)
					break;
				hypothesis.append(TGenUtils.insertCondition(tc));
			}
			
			insertGoal(condition);
		} else if (condition.parent().element() == catalog) {
			for(TranslationRule rule: catalog.getRules()) {
				insertRuleFull(rule);
			}
			
			for(TranslationCondition tc: catalog.getConditions()) {
				if (tc == condition)
					break;
				hypothesis.append(TGenUtils.insertCondition(tc));
			}
			
			insertGoal(condition);
		}
	}
	
	private void insertGoal(TranslationCondition condition) throws GenException {
		TGenUtils.check(condition);
		goal.append(condition.getBody().content());
	}

	private void insertRuleFull(TranslationRule rule) throws GenException {
		insertRule(rule);
		for(TranslationCondition tc: rule.getConditions()) {
			hypothesis.append(TGenUtils.insertCondition(tc));
		}
	}

	private void insertRule(TranslationRule rule) throws GenException {
		String ruledef = TGenUtils.ruleString(rule);
		definitions.append(ruledef);
	}
	

}
