package uk.ac.ncl.eventb.why3.gen.ui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.sapphire.PossibleValuesService;

import uk.ac.ncl.eventb.why3.gen.opregistry.EventBOperator;
import uk.ac.ncl.eventb.why3.gen.opregistry.OperatorRegistry;

public class EventBOperators extends PossibleValuesService {
	private static final Set<String> operators;
	static {
		operators = new HashSet<String>();
		for(EventBOperator operator: OperatorRegistry.registry) {
			operators.add(operator.getName());
		}
	}
	
	@Override
	protected void compute(Set<String> values) {
		values.addAll(operators);
	}

}
