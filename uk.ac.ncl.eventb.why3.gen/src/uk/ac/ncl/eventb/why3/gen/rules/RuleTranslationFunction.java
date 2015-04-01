package uk.ac.ncl.eventb.why3.gen.rules;
import org.eclipse.sapphire.FilteredListener;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.ValuePropertyContentEvent;
import org.eclipse.sapphire.modeling.el.Function;
import org.eclipse.sapphire.modeling.el.FunctionContext;
import org.eclipse.sapphire.modeling.el.FunctionResult;
import org.eclipse.sapphire.ui.PartFunctionContext;

import uk.ac.ncl.eventb.why3.gen.GenException;
import uk.ac.ncl.eventb.why3.gen.translation.CatalogTranslator;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationRule;
import uk.ac.ncl.eventb.why3.translator.TranslationException;

public final class RuleTranslationFunction extends Function {
    @Override
    public String name() {
        return "rule:Translate";
    }

    @Override
    public FunctionResult evaluate(final FunctionContext context) {
        return new FunctionResult(this, context) {
        	private TranslationRule rule;
        	private CatalogTranslator translator;
        	
            @Override
			protected void init()
            {
            	translator = new CatalogTranslator();
            	rule = (TranslationRule) ((PartFunctionContext) context()).element();
            	
        		final Listener propertyListener = new FilteredListener<ValuePropertyContentEvent>() {
        			@Override
        			protected void handleTypedEvent(final ValuePropertyContentEvent event) {
        				//if ("source".equals(rule.getGUIMode().content()))
        						refresh();
        			}
        		};
//        		rule.getSource().attach(propertyListener);
//        		rule.getTarget().attach(propertyListener);
//        		rule.getBody().attach(propertyListener);            	
//        		rule.getConditions().attach(propertyListener);
        		rule.getGUIMode().attach(propertyListener);
            }
            
            @Override
            protected Object evaluate() {
            	try {
					if (!rule.getSource().empty() && !rule.getTarget().empty() && rule.validation().ok()) {
						translator.reset();
						translator.translate(rule);
						return translator.getResult().replaceAll("\\n", "<br>");
					} else {
						return null;
					}
				} catch (TranslationException | GenException e) {
					e.printStackTrace();
					return null;
				}
            }
        };
    }

}