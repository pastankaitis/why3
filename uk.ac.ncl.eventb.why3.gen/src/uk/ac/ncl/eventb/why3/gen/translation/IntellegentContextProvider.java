package uk.ac.ncl.eventb.why3.gen.translation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import uk.ac.ncl.eventb.why3.gen.GenException;
import uk.ac.ncl.eventb.why3.gen.GenericTranslationContext;
import uk.ac.ncl.eventb.why3.gen.TriggerContext;
import uk.ac.ncl.eventb.why3.gen.opregistry.OperatorRegistry;
import uk.ac.ncl.eventb.why3.gen.ui.ConditionDependency;
import uk.ac.ncl.eventb.why3.gen.ui.ITranslationElement;
import uk.ac.ncl.eventb.why3.gen.ui.RuleDependency;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationCondition;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationRule;
import uk.ac.ncl.eventb.why3.translator.IContextProvider;
import uk.ac.ncl.eventb.why3.translator.TranslationException;

public class IntellegentContextProvider implements IContextProvider {
	private GenericTranslationContext context;
	private TriggerContext triggerContext;
	private CatalogTranslator catalogTranslator;
	
	public IntellegentContextProvider(TriggerContext triggerContext, GenericTranslationContext context) {
		this.triggerContext = triggerContext;
		this.context = context;
		this.catalogTranslator = new CatalogTranslator();
	}
	
	@Override
	public void addTheoryImports(StringBuilder sb) {
		Set<String> theories = new HashSet<String>();
		for(int i = 0; i < OperatorRegistry.operatorCount(); i++) {
			if (triggerContext.dependsOnFallBackOperator(i)) {
				theories.add(OperatorRegistry.codeToTheory(i));
			}
		}
		
		for(String theory: theories) {
			sb.append("\tuse import ");
			sb.append(theory);
			sb.append("\n");
		}
	}

	@Override
	public void addInplaceTheories(StringBuilder sb) throws TranslationException {
		List<TranslationRule> rules = new ArrayList<TranslationRule>();
		for(int i = 0; i < OperatorRegistry.operatorCount(); i++) {
			if (triggerContext.dependsOn(i)) {
				TranslationRule rule = context.getRuleForCode(i);
				assert(rule != null);
				if (!rules.contains(rule)) {
					addDependentRules(rules, rule);
					rules.add(rule);
				}
			}
		}
		
		List<TranslationCondition> conditions = new ArrayList<TranslationCondition>();
		Iterator<TranslationCondition> iterator = context.getTranslationConditions();
		while (iterator.hasNext()) {
			TranslationCondition condition = iterator.next();
			if (triggerContext.match(condition.getCachedTriggers().content())) {
				addDependentRules(rules, condition);
				addDependentConditions(conditions, condition);
				conditions.add(condition);
			}
		}
		
		try {
			// emit rules
			for(TranslationRule rule: rules) {
				catalogTranslator.translate(rule);
			}
			
			// emit conditions
			for(TranslationRule rule: rules) {
				catalogTranslator.translate(rule);
			}
		} catch (GenException e) {
			throw new TranslationException(e.toString());
		}
		
	}		
	
	private void addDependentConditions(List<TranslationCondition> conditions, TranslationCondition condition) {
		for(ConditionDependency conddep: condition.getConditionDependencies()) {
			TranslationCondition cond = conddep.getConditionDependency().target();
			if (cond != null && !conditions.contains(cond))
				conditions.add(cond);
		}
	}

	private void addDependentRules(List<TranslationRule> rules, ITranslationElement source) {
		for(RuleDependency ruledep: source.getRuleDependencies()) {
			TranslationRule rule = ruledep.getRuleDependency().target();
			if (rule != null && !rules.contains(rule))
				rules.add(rule);
		}
	}


}
