package uk.ac.ncl.eventb.why3.main;


/**
 * An exception class representing a failure tool invocation
 * @author A. Iliasov
 *
 */
public class Why3ToolException extends Exception {
	private static final long serialVersionUID = -6952792689024747284L;

	public Why3ToolException(String message) {
		super(message);
	}
}
