package uk.ac.ncl.eventb.why3.driver;

public class Why3SyntaxCheckResult {
	public static final Why3SyntaxCheckResult NOERROR = new Why3SyntaxCheckResult();
	public static final Why3SyntaxCheckResult FAILED = new Why3SyntaxCheckResult("Failed");

	private int errorColStart;
	private int errorColEnd;
	private String errorMessage;

	private Why3SyntaxCheckResult() {}

	public Why3SyntaxCheckResult(String errorMessage) {
		this.errorColStart = 0;
		this.errorColEnd = -1;
		this.errorMessage = errorMessage;
	}
	
	public Why3SyntaxCheckResult(int errorColStart, int errorColEnd, String errorMessage) {
		super();
		this.errorColStart = errorColStart;
		this.errorColEnd = errorColEnd;
		this.errorMessage = errorMessage;
	}

	public static Why3SyntaxCheckResult getNoerror() {
		return NOERROR;
	}

	public int getErrorColStart() {
		return errorColStart;
	}

	public int getErrorColEnd() {
		return errorColEnd;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	
}
