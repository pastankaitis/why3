package uk.ac.ncl.eventb.why3.main;

import java.text.DecimalFormat;



/**
 * Invocation result for why3 connector
 *
 * @author A. Iliasov
 */
public class Why3CallResult {
	public static final Why3CallResult INSTANCE_CANCEL = new Why3CallResult(WHY3_RESULT.CANCEL, "", 0);
	public static final Why3CallResult INSTANCE_PANIC = new Why3CallResult(WHY3_RESULT.PANIC, "", 0);
	public static final Why3CallResult INSTANCE_UNKNOWN = new Why3CallResult(WHY3_RESULT.UNKNOWN, "", 0);
	public static final Why3CallResult INSTANCE_FAILURE = new Why3CallResult(WHY3_RESULT.TOOL_FAILURE, "", 0);
	
	public static final int TOOL_FAILURE_CODE = 1; 
	public static final int TIMEOUT_CODE = 2;
	public static final int PANIC_CODE = 3;
	public static final int CANCEL_CODE = 4;
	public static final int UNKNOWN_CODE = 5;
	public static final int INVALID_CODE = 6; 
	public static final int VALID_CODE = 7; 
	
	/**
	 * The result of a run.
	 */
	public enum WHY3_RESULT {
							TOOL_FAILURE("Failure", TOOL_FAILURE_CODE), // failed calling local or cloud tool, proof has not started 
							TIMEOUT("Timeout", TIMEOUT_CODE), // all tool reported time out
							PANIC("Panic", PANIC_CODE), // tools returned conflicting results: two tools reporting 'valid' and 'invalid'
							CANCEL("Cancel", CANCEL_CODE), // cancelled by user
							UNKNOWN("Unknown", UNKNOWN_CODE), // some tools reported 'unknown' result; others timeout
							INVALID("Invalid", INVALID_CODE), // at least one tool reported 'invalid'; the rest 'unknown' or 'timeout' 
							VALID("Valid", VALID_CODE) // at least one tool reported 'valid'; the rest 'unknown' or 'timeout'
							;
							String id;
							int code;
							private WHY3_RESULT(String id, int code) {
								this.code = code;
								this.id = id;
							}
							
							public static WHY3_RESULT fromString(String x) {
								if (x.equals("Failure"))
									return TOOL_FAILURE;
								else if (x.equals("Timeout"))
									return TIMEOUT;
								else if (x.equals("Panic"))
									return PANIC;
								else if (x.equals("Cancel"))
									return CANCEL;
								else if (x.equals("Unknown"))
									return UNKNOWN;
								else if (x.equals("Invalid"))
									return INVALID;
								else if (x.equals("Valid"))
									return VALID;
								else
									return TOOL_FAILURE;
									
							}
		
							/**
							 * Returns the negated form of the result
							 * @return
							 */
							public WHY3_RESULT negate() {
								switch(this) {
								case VALID:
									return INVALID;
								case INVALID:
									return VALID;
								default:
									return this;
								}
							}							
							
							public WHY3_RESULT adjudicate(WHY3_RESULT result) {
								switch(this) {
								case UNKNOWN:
									return result;
								case VALID:
									if (result == INVALID)
										return PANIC;
									break;
								case INVALID:
									if (result == VALID)
										return PANIC;
									break;
								default:
									return this;
								}
								return this;
							}							
							
							public boolean isDefinite() {
								return this == VALID || this == INVALID;
							}
							
							public int getCode() {
								return code;
							}

							@Override
							public String toString() {
								return id;
							}
							
		}
	
	private WHY3_RESULT call_result;
	private String tool; // why3 tool responsible for a definite result ('unknown', 'invalid', 'valid')
	private long time, time2;
	private boolean local = true;
	private static final DecimalFormat df = new DecimalFormat("#.00"); 
	
	public Why3CallResult (WHY3_RESULT call_result, String tool, long time) {
		
		this.call_result = call_result;
		this.tool = tool;
		this.time = time;
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	/**
	 * Adjudicates results of two prover calls on the same theorem
	 * @param result
	 * @return
	 */
	public Why3CallResult adjudicate(Why3CallResult result) {
		switch(this.call_result) {
		case UNKNOWN:
			return result;
		case VALID:
			if (result.call_result == WHY3_RESULT.INVALID)
				return INSTANCE_PANIC;
			break;
		case INVALID:
			if (result.call_result == WHY3_RESULT.VALID)
				return INSTANCE_PANIC;
			break;
		default:
			return this;
		}
		return this;
	}		
	
	/**
	 * Returns the negated form of the result
	 * @return
	 */
	public Why3CallResult negate() {
		switch(this.call_result) {
		case VALID:
			return new Why3CallResult(WHY3_RESULT.INVALID, tool, time);
		case INVALID:
			return new Why3CallResult(WHY3_RESULT.VALID, tool, time);
		default:
			return this;
		}
	}		
	
	/**
	 * Returns the proof status why3 invocation.
	 * 
	 * @return proof status
	 */
	public WHY3_RESULT getStatus() { 
		return call_result;
	}

	/**
	 * Returns the why3 tool responsible for a definite result ('unknown', 'invalid', 'valid')
	 * @return TPTP or SMT tool name
	 */
	public String getTool() {
		return tool;
	}

	public long getTime2() {
		return time2;
	}

	public void setTime2(long time2) {
		this.time2 = time2;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("why3");
		
		if (!isLocal()) {
			sb.append("/cloud");
		}
		
		if (tool != null && tool.trim().length() > 0) {
			sb.append("/");
			sb.append(tool);
		}
		
		if (time >= 0) {
			sb.append("/");
			sb.append(df.format((time)/1000.0));
			sb.append("s");
			if (time2 >= time) {
				sb.append("[");
				sb.append(df.format((time2)/1000.0));
				sb.append("s");
				sb.append("]");
			}
		}
		
		
		return sb.toString();
	}


}