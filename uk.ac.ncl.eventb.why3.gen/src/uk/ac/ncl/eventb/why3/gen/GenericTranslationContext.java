package uk.ac.ncl.eventb.why3.gen;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.ac.ncl.eventb.why3.gen.opregistry.OperatorRegistry;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationCatalog;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationCondition;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationRule;
import uk.ac.ncl.eventb.why3.translator.TranslationException;

public class GenericTranslationContext {
	private GenericTranslationContext parent;
	private TranslationCatalog catalog;
	private Map<Integer, TranslationRule> tagToRule;
	
	public GenericTranslationContext(TranslationCatalog catalog) throws TranslationException {
		this.catalog = catalog;
		tagToRule = new HashMap<Integer, TranslationRule>();
		for(TranslationRule rule: catalog.getRules()) {
			int tag = OperatorRegistry.symbolToTag(rule.getSource().content());
			if (tag == -1)
				throw new TranslationException("Unknown operator '" + rule.getSource().content() + "'");
			tagToRule.put(tag, rule);
		}
	}

	public GenericTranslationContext(GenericTranslationContext parent, TranslationCatalog catalog) throws TranslationException {
		this(catalog);
		this.parent = parent;
	}
	
	public TranslationRule getRuleForTag(int tag) {
		TranslationRule rule = tagToRule.get(tag);
		if (rule != null && parent != null)
			return parent.getRuleForTag(tag);
		
		return rule;
	}
	
	public TranslationRule getRuleForCode(int code) {
		int tag = OperatorRegistry.codeToTag(code);
		return getRuleForTag(tag);
	}
	
	public Iterator<TranslationCondition> getTranslationConditions() {
		return new ConditionsIterator();
	}	
	
	public class ConditionsIterator implements Iterator<TranslationCondition> {
	    private Iterator<TranslationCondition> iterator;
	    private GenericTranslationContext context;

	    public ConditionsIterator() {
	    	context = GenericTranslationContext.this;
	    	iterator = context.catalog.getConditions().iterator();
	    }

	    private void doswitch() {
	    	context = context.parent;
	    	if (context != null)
	    		iterator = context.catalog.getConditions().iterator();
	    }
	    
	    @Override
		public boolean hasNext() {
	    	if (iterator == null) {
	    		return false;
	    	} else if (iterator.hasNext()) {
	    		return true;
	    	} else {
	    		doswitch();
	    		return hasNext();
	    	}
	    }

	    @Override
		public TranslationCondition next() {
	    	return iterator.next();
	    }

	    @Override
		public void remove() {
	    	// ignore
	    }
	}	
}
