package com.why3.ws.scenario;

import uk.ac.ncl.pparser.syntree;

public class ScenarioException extends Exception {
	private static final long serialVersionUID = -6120278415462995690L;
	private syntree ast;
	private String message;
	public ScenarioException(syntree ast, String message) {
		super();
		this.ast = ast;
		this.message = message;
	}
	
	public syntree getAst() {
		return ast;
	}

	public String getMessageString() {
		return message;
	}
}
