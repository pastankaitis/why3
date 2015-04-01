package uk.ac.ncl.eventb.why3.gen.rules;
import org.eclipse.sapphire.FilteredListener;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.ValuePropertyContentEvent;
import org.eclipse.sapphire.modeling.el.Function;
import org.eclipse.sapphire.modeling.el.FunctionContext;
import org.eclipse.sapphire.modeling.el.FunctionResult;
import org.eclipse.sapphire.ui.PartFunctionContext;

import uk.ac.ncl.eventb.why3.gen.opregistry.EventBOperator;
import uk.ac.ncl.eventb.why3.gen.opregistry.OperatorRegistry;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationRule;

public final class RuleDescribeFunction extends Function {
    @Override
    public String name() {
        return "rule:Describe";
    }

    @Override
    public FunctionResult evaluate(final FunctionContext context) {
        return new FunctionResult(this, context) {
        	private TranslationRule rule;
        	
            @Override
			protected void init()
            {
            	rule = (TranslationRule) ((PartFunctionContext) context()).element();
        		final Listener propertyListener = new FilteredListener<ValuePropertyContentEvent>() {
        			@Override
        			protected void handleTypedEvent(final ValuePropertyContentEvent event) {
        				refresh();
        			}
        		};
        		rule.getSource().attach(propertyListener);
        		rule.getTarget().attach(propertyListener);
        		rule.getBody().attach(propertyListener);
            }
            
            @Override
            protected Object evaluate() {
            	if (!rule.getSource().empty() && !rule.getTarget().empty()) {
            		EventBOperator operator = OperatorRegistry.getOperator(rule.getSource().content());
            		String name = rule.getTarget().content();
            		String definition = rule.getBody().empty() ? null : rule.getBody().content();
            		return operator.toString(name, definition);
            	} else {
            		return null;
            	}
            }
        };
    }

}