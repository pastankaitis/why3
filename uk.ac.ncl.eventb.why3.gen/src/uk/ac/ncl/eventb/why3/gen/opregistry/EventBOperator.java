package uk.ac.ncl.eventb.why3.gen.opregistry;

import uk.ac.ncl.eventb.why3.gen.type.CLProductType;
import uk.ac.ncl.eventb.why3.gen.type.CLType;
import uk.ac.ncl.eventb.why3.gen.type.CLTypeBool;
import uk.ac.ncl.eventb.why3.gen.type.CLTypeInteger;

/**
 * Definition of an Event-B operator
 * @author alex
 *
 */
public class EventBOperator {
	public enum KIND {
		FUNCTION,
		PREDICATE,
		CONSTANT
	}
	
	private String name;
	private String symbol;
	private String fallBackTheory;
	private int tag;
	private CLType[] types;
	private KIND kind;
	
	/**
	 * Create a new operator descriptor
	 * @param name operator name (human-readable)
	 * @param symbol operator Event-B symbol
	 * @param tag Event-B formula tag
	 * @param types list of operand types followed by the return type
	 */
	public EventBOperator(KIND kind, String name, String symbol, String fallBackTheory, int tag, CLType ... types) {
		this.kind = kind;
		this.name = name;
		this.symbol = symbol;
		this.tag = tag;
		this.types = types;
		this.fallBackTheory = fallBackTheory;
	}

	/**
	 * Predefined theory defining constants and lemmas supporting this operator
	 * @return why3 theory name
	 */
	public String getFallBackTheory() {
		return fallBackTheory;
	}

	public KIND getKind() {
		return kind;
	}

	/**
	 * Human-friendly operator name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Plain text or UTF symbol of the operator
	 * @return
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Event-B formula tag
	 * @return
	 */
	public int getTag() {
		return tag;
	}

	/**
	 * Operator types; the last one denotes return type
	 * @return
	 */
	public CLType[] getTypes() {
		return types;
	}
	
	public String toString(String id, String definition) {
		StringBuffer sb = new StringBuffer();
		
		int shift = 0;
		switch(kind) {
			case FUNCTION:
			sb.append("function");
			shift = 1;
			break;
			case PREDICATE:
			sb.append("predicate");
			break;
			case CONSTANT:
			sb.append("constant");
			break;
		}
   		
   		sb.append(" ");
		sb.append(id);
		
		String definitionString = null;
		if (definition != null && definition.trim().length() > 0) {
			definitionString = definition.trim();
			if (definitionString.length() == 0)
				definitionString = null;
		}
		ParameterNames pnames = new ParameterNames();
		sb.append(" ");
		for(int i = 0; i < getTypes().length - shift; i++) {
			if (i > 0)
				sb.append(" ");
			if (definitionString != null) {
				sb.append("(");
				sb.append(pnames.getId(getTypes()[i]));
				sb.append(" : ");
				sb.append(getTypes()[i].toWhy3Type());
				sb.append(")");
			} else {
				sb.append(getTypes()[i].toWhy3Type());
			}
		}
		
		if (kind == KIND.FUNCTION) {
			sb.append(" : ");
			sb.append(getTypes()[getTypes().length - 1].toWhy3Type());
		}
		
		if (definitionString != null) {
			sb.append(" = ");
			sb.append(definitionString);
		}		
		sb.append(" ");
		sb.append("(*");
		sb.append(name);
		sb.append("*)");
		return sb.toString();
		
	}

	private static final String[] number 	= {"n", "m", "k"};
	private static final String[] set 		= {"s", "u", "v"};
	private static final String[] relation 	= {"r", "p", "q"};
	private static final String[] bool 		= {"x", "y", "z"};
	private static final String[] tuple 	= {"t", "d", "l"};
	class ParameterNames {
		private int _number = 0;
		private int _set = 0;
		private int _relation = 0;
		private int _bool = 0;
		private int _tuple = 0;
		private int _else = 0;
		
		public ParameterNames() {
			
		}
		
		public String getId(CLType type) {
			if (type == CLTypeBool.INSTANCE) {
				return bool[_bool++];
			} else if (type == CLTypeInteger.INSTANCE) {
				return number[_number++];
			} else if (type.isRelation()) {
				return relation[_relation++];
			} else if (type.isSet()) {
				return set[_set++];
			} else if (type instanceof CLProductType) {
				return tuple[_tuple++];
			} else {
				return "par" + ++_else;
			}
		}
	}
}
