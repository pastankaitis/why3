package uk.ac.ncl.eventb.why3.gen.opregistry;

import java.util.Arrays;
import java.util.Collection;

import org.eventb.core.ast.Formula;

import uk.ac.ncl.eventb.why3.gen.type.CLPowerType;
import uk.ac.ncl.eventb.why3.gen.type.CLProductType;
import uk.ac.ncl.eventb.why3.gen.type.CLType;
import uk.ac.ncl.eventb.why3.gen.type.CLTypeAny;
import uk.ac.ncl.eventb.why3.gen.type.CLTypeInteger;

public class OperatorRegistry {
	public static final Collection<EventBOperator> registry;
	public static final String[] operatorNames;
	public static final String[] operatorTheories;
	public static final int[] operatorTags;
	
	public static EventBOperator getOperator(String name) {
		for(EventBOperator op: registry) {
			if (op.getName().equals(name))
				return op;
		}
		
		return null;
	}
	
	
	
	private static EventBOperator operator(String name, String symbol, String theory, int tag, CLType ... types) {
		return new EventBOperator(EventBOperator.KIND.FUNCTION, name, symbol, theory, tag, types);
	}

	private static EventBOperator predicate(String name, String symbol, String theory, int tag, CLType ... types) {
		return new EventBOperator(EventBOperator.KIND.PREDICATE, name, symbol, theory, tag, types);
	}

	private static EventBOperator constant(String name, String symbol, String theory, int tag, CLType ... types) {
		return new EventBOperator(EventBOperator.KIND.CONSTANT, name, symbol, theory, tag, types);
	}	
	
	public static int operatorCount() {
		return registry.size();
	}

	public static int symbolToCode(String symbol) {
		return Arrays.binarySearch(operatorNames, symbol);
	}	

	public static int symbolToTag(String symbol) {
		int code = Arrays.binarySearch(operatorNames, symbol);
		if (code >=0)
			return codeToTag(code);
		else
			return -1;
	}		
	
	public static int tagToCode(int tag) {
		return Arrays.binarySearch(operatorTags, tag);
	}
	
	public static String codeToTheory(int code) {
		return operatorTheories[code];
	}		

	public static String codeToSymbol(int code) {
		return operatorNames[code];
	}	
	
	public static int codeToTag(int code) {
		return operatorTags[code];
	}		
	
	private static CLType rel(CLType a, CLType b) {
		return new CLPowerType(new CLProductType(a, b));
	}

	private static CLType tuple(CLType a, CLType b) {
		return new CLProductType(a, b);
	}

	private static CLType set(CLType a) {
		return new CLPowerType(a);
	}
	
	static {
		CLType a = new CLTypeAny("a");
		CLType b = new CLTypeAny("b");
		CLType c = new CLTypeAny("c");
		CLType d = new CLTypeAny("d");
		CLType bool = new CLTypeAny("bool");
		
		CLType a_set = set(a);
		CLType bool_set = set(bool);
		CLType a_powerset = set(set(a));
		CLType b_set = set(b);
		CLType a_b_rel = rel(a, b);
		CLType b_a_rel = rel(b, a);
		CLType b_c_rel = rel(b, c);
		CLType a_c_rel = rel(a, c);
		CLType b_d_rel = rel(b, d);
		CLType a_a_rel = rel(a, a);

		// rel 'a ('b, 'c) 
		CLType a_b_c_rel = rel(a, tuple(b, c));
		CLType a_a_b_rel = rel(a, tuple(a, b));
		CLType b_a_b_rel = rel(b, tuple(a, b));
		
		//  (rel ('a, 'b) ('c, 'd)) 
		CLType a_b_c_d_rel = rel(tuple(a, b), tuple(c, d));
		CLType a_c_b_d_rel = rel(tuple(a, c), tuple(b, d));
		registry = Arrays.asList(
				operator("Cardinal", "card", "eventbgen.Cardinality", Formula.KCARD, CLTypeAny.POWER_INSTANCE, CLTypeInteger.INSTANCE),
				operator("Unary Intersection", "inter", "eventbgen.Intersection", Formula.KINTER, a_powerset, a_set),
				operator("Powerset 1", "ℙ1", "eventbgen.ProperSubset", Formula.POW1, a_set, a_powerset),
				operator("Power Set", "ℙ", "eventbgen.PowerSet", Formula.POW, a_set, a_powerset),
				operator("Max", "max", "eventbgen.Maximum", Formula.KMAX, CLTypeInteger.POWER_INSTANCE, CLTypeInteger.INSTANCE),
				operator("Range", "ran", "eventbgen.Range", Formula.KRAN, a_b_rel, b_set),
				operator("Min", "min", "eventbgen.Minimum", Formula.KMIN, CLTypeInteger.POWER_INSTANCE, CLTypeInteger.INSTANCE),
				operator("Domain", "dom", "eventbgen.Domain", Formula.KDOM, a_b_rel, a_set),
				operator("Unary Union", "union", "eventbgen.Union", Formula.KUNION, a_powerset, a_set),
				operator("Converse", "∼",  "eventbgen.Inverse", Formula.CONVERSE, a_b_rel, b_a_rel),
				operator("Up To", "‥", "eventbgen.Interval", Formula.UPTO, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE, CLTypeInteger.POWER_INSTANCE),
				predicate("Finite", "finite", "eventbgen.Finite", Formula.KFINITE, CLTypeAny.POWER_INSTANCE),
				operator("Total Relation", "", "eventbgen.TotalRelation", Formula.TREL, a_set, b_set, a_b_rel),
				operator("Total Injection", "↣", "eventbgen.TotalInjection", Formula.TINJ, a_set, b_set, a_b_rel),
				operator("Surjective Relation", "", "eventbgen.SurjectiveRelation", Formula.SREL, a_set, b_set, a_b_rel),
				operator("Total Bijection", "⤖", "eventbgen.TotalBijection", Formula.TBIJ, a_set, b_set, a_b_rel),
				operator("Partial Injection", "⤔", "eventbgen.PartialInjection", Formula.PINJ, a_set, b_set, a_b_rel),
				operator("Partial Function", "⇸", "eventbgen.PartialFunction", Formula.PFUN, a_set, b_set, a_b_rel),
				operator("Surjective Total Relation", "", "eventbgen.TotalSurjectiveRelation", Formula.STREL, a_set, b_set, a_b_rel),
				operator("Relation", "↔", "eventbgen.Relation", Formula.REL, a_set, b_set, a_b_rel),
				operator("Total Function", "→", "eventbgen.TotalFunction", Formula.TREL, a_set, b_set, a_b_rel),
				operator("Partial Surjection", "⤀", "eventbgen.PartialSurjection", Formula.PSUR, a_set, b_set, a_b_rel),
				operator("Total Surjection", "↠", "eventbgen.TotalSurjection", Formula.TSUR, a_set, b_set, a_b_rel),
				operator("Fun Image", "(..)", "eventbgen.Apply", Formula.FUNIMAGE, a_b_rel, a, b),
				operator("Relational Image", "[..]", "eventbgen.Image", Formula.RELIMAGE, a_b_rel, a_set, b_set),
				operator("Range Subtraction", "⩥", "eventbgen.RangeSubtraction", Formula.RANSUB, a_b_rel, b_set, a_b_rel),
				operator("Backward Composition", "∘", "eventbgen.BackwardComposition", Formula.BCOMP, b_c_rel, a_b_rel, a_b_rel),
				operator("Range Restriction", "▷", "eventbgen.RangeRestriction", Formula.RANRES, a_b_rel, b_set, a_b_rel),
				operator("Binary Intersection", "∩", "eventbgen.Intersection", Formula.BINTER, a_set, a_set, a_set),
				operator("Binary Union", "∪", "eventbgen.Union", Formula.BUNION, a_set, a_set, a_set),
				operator("Parallel Product", "∥", "eventbgen.ParalleProduct", Formula.PPROD, a_c_rel, b_d_rel, a_c_b_d_rel), 
				operator("Set Minus", "∖", "eventbgen.Difference", Formula.SETMINUS, a_set, a_set, a_set),
				operator("Forward Composition", ";", "eventbgen.ForwardComposition", Formula.FCOMP, a_b_rel, b_c_rel, a_c_rel),
    			operator("Direct Product", "⊗", "eventbgen.DirectProduct", Formula.DPROD, a_b_rel, a_c_rel, a_b_c_rel),
				operator("Domain Restriction", "◁", "eventbgen.DomainRestriction", Formula.DOMRES, a_set, a_b_rel),
				operator("Domain Subtraction", "⩤", "eventbgen.DomainSubtraction", Formula.DOMSUB, a_set, a_b_rel),
				operator("Cartesian Product", "×", "eventbgen.CartesianProduct", Formula.CPROD, a_set, b_set, a_b_rel),
				predicate("False", "FALSE", "bool.Bool",  Formula.FALSE),
				predicate("True", "TRUE", "bool.Bool", Formula.TRUE),
				operator("Identity", "id", "eventbgen.Identity", Formula.KID_GEN, a_a_rel),
				operator("Projection 2", "prj2", "eventbgen.Projection2", Formula.KPRJ2_GEN, b_a_b_rel),
				operator("Projection 1", "prj1", "eventbgen.Projection1", Formula.KPRJ1_GEN, a_a_b_rel),				
				constant("Empty Set", "∅", "eventbgen.EmptySet", Formula.EMPTYSET, a_set),
				operator("Natural1", "ℕ1", "eventbgen.Natural1", Formula.NATURAL1, CLTypeInteger.POWER_INSTANCE),
				operator("Bool", "bool", "eventbgen.Boolean", Formula.BOOL, bool_set),
				predicate("Stricht Subset", "⊂", "eventbgen.ProperSubset", Formula.SUBSET, a_set, a_set),
				predicate("Lower Than", "<", "int.Int", Formula.LT, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE),
				predicate("Lower or Equal", "≤", "int.Int", Formula.LEQV, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE),
				predicate("In", "∈", "eventbgen.Membership", Formula.IN, a, a_set),		
				predicate("Greater or Equal", "≥", "int.Int", Formula.GE, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE),
				predicate("Greater Than", ">", "int.Int", Formula.GT, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE),
				predicate("Equal", "==", "eventbgen.Equality", Formula.EQUAL, a_set, a_set),
				predicate("Subset", "⊆", "eventbgen.Subset", Formula.SUBSETEQ, a_set, a_set),
				operator("Modulo", "mod", "int.EuclideanDivision", Formula.MOD, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE),
				operator("Negative Literal", "a negative integer literal", "eventbgen.UnaryMinus", Formula.UNMINUS, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE),
				operator("Minus", "−", "int.Int", Formula.MINUS, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE),
				operator("Integer Division", "÷", "int.EuclideanDivision", Formula.DIV, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE),
				operator("Integer Exponentiation", "^", "eventbgen.Exponentiation", Formula.EXPN, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE),
				operator("Multiplication", "∗", "eventbgen.Multiplication", Formula.MUL,  CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE, CLTypeInteger.INSTANCE)
				
	
				
				
				
		);

		/*
		 * new EventBOperator("Backward Composition", "∘", ?, ? new
		 * EventBOperator("Range Restriction", "▷", ?, ? new 
		 * EventBOperator("Binary Intersection", "∩", ?, ? new
		 * EventBOperator("Binary Union", "∪", ?, ? new
		 * EventBOperator("Parallel Product", "∥", ?, ? new
		 * EventBOperator("Set Minus", "∖", ?, ? new
		 * EventBOperator("Forward Composition", ";", ?, ? new
		 * EventBOperator("Direct Product", "⊗", ?, ? new
		 * EventBOperator("Overload", "", ?, ? new
		 * EventBOperator("Domain Restriction", "◁", ?, ? new
		 * EventBOperator("Domain Subtraction", "⩤", ?, ? new
		 * EventBOperator("Cartesian Product", "×", ?, ? new
		 * EventBOperator("Predecessor", "pred", ?, ? new
		 * EventBOperator("False", "FALSE", ?, ? new EventBOperator("Identity",
		 * "id", ?, ? new EventBOperator("Bool Type", "BOOL", ?, ? new
		 * EventBOperator("True", "TRUE", ?, ? new EventBOperator("Integer",
		 * "ℤ", ?, ? new EventBOperator("Successor", "succ", ?, ? new
		 * EventBOperator("Projection 2", "prj2", ?, ? new
		 * EventBOperator("Projection 1", "prj1", ?, ? new
		 * EventBOperator("Empty Set", "∅", ?, ? new EventBOperator("Natural1",
		 * "ℕ1", ?, ? new EventBOperator("Natural", "ℕ", ?, ? new
		 * EventBOperator("for all", "∀", ?, ? new EventBOperator("exists", "∃",
		 * ?, ? new EventBOperator("To Bool", "bool", ?, ? new
		 * EventBOperator("Subset", "⊂", ?, ? new EventBOperator("Not Subset",
		 * "⊄", ?, ? new EventBOperator("Lower Than", "<", ?, ? new
		 * EventBOperator("Not In", "∉", ?, ? new
		 * EventBOperator("lower or equal", "≤", ?, ? new EventBOperator("In",
		 * "∈", ?, ? new EventBOperator("Not Equal", "≠", ?, ? new
		 * EventBOperator("Not Subset or Equal", "⊈", ?, ? new
		 * EventBOperator("Greater or Equal", "≥", ?, ? new
		 * EventBOperator("greater than", ">", ?, ? new EventBOperator("equal",
		 * "=", ?, ? new EventBOperator("Subset or Equal", "⊆", ?, ? new
		 * EventBOperator("Modulo", "mod", ?, ? new
		 * EventBOperator("a negative integer literal",
		 * "a negative integer literal", ?, ? new EventBOperator("Minus", "−",
		 * ?, ? new EventBOperator("Integer Division", "÷", ?, ? new
		 * EventBOperator("Integer Exponentiation", "^", ?, ? new
		 * EventBOperator("mul", "∗", ?, ? new EventBOperator("plus", "+", ?, ?
		 */
		operatorNames = new String[registry.size()];
		operatorTags = new int[registry.size()];
		operatorTheories = new String[registry.size()];
		
		int i = 0;
		for(EventBOperator operator: registry) {
			operatorNames[i] = operator.getSymbol();
			operatorTags[i] = operator.getTag();
			operatorTheories[i] = operator.getFallBackTheory();
			i++;
		}
	}
}
