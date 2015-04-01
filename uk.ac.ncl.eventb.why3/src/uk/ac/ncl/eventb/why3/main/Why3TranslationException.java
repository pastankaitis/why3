package uk.ac.ncl.eventb.why3.main;

import org.eventb.core.ast.Formula;

/**
 * An exception class representing a failure in the translation of Event-B notation
 * to why3 theory language
 * @author A. Iliasov
 *
 */
public class Why3TranslationException extends Exception {
	private static final long serialVersionUID = -8159299015418068218L;
	private Formula<?> failedFormula;

	public Why3TranslationException(Formula<?> failedFormula, String message) {
		super(message);
		this.failedFormula = failedFormula;
	}

	public Formula<?> getFailedFormula() {
		return failedFormula;
	}
}
