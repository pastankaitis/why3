package uk.ac.ncl.eventb.why3.gen.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.sapphire.PossibleValuesService;

public class LibraryTheories extends PossibleValuesService {
	private static final Set<String> theories = new HashSet<String>(Arrays.asList(
		"set.SetComprehension",
		"set.Fset",
		"settheory.Interval",
		"settheory.PowerSet",
		"settheory.Image",
		"settheory.InverseDomRan",
		"settheory.Function",
		"settheory.PowerSet",
		"settheory.PowerRelation"
	));	
	@Override
	protected void compute(Set<String> values) {
		values.addAll(theories);

	}

}
