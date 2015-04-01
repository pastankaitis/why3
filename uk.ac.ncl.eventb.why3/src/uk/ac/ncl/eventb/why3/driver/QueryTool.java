package uk.ac.ncl.eventb.why3.driver;

import java.io.IOException;

public abstract class QueryTool extends ToolConnection<Boolean> {
	private String args;
	
	protected QueryTool(String tool_path, String args) throws IOException {
		super(tool_path, Boolean.FALSE);
		this.args = args;
	}

	@Override
	public String getToolArguments() {
		return args;
	}
	
	public void log(String message)
	{
		log.append(message);
		log.append("\n");
	}

}
