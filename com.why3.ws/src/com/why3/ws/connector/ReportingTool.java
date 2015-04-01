package com.why3.ws.connector;

import java.io.IOException;
import java.util.Map;

public abstract class ReportingTool<T> extends ToolConnection<T> {
	private String args;
	private Map<String, T> status_map;
	
	protected ReportingTool(String tool_path, String args, T initial_status, Map<String, T> status_map) throws IOException {
		super(tool_path, initial_status);
		this.args = args;
		this.status_map = status_map;
	}

	@Override
	public String getToolArguments() {
		return args;
	}

	public T statusReport(String line) {
		for(String pattern: status_map.keySet()) {
			if (line.contains(pattern)) {
				return status_map.get(pattern);
			}
		}

		return null;
	}

	@Override
	public void processLine(String line) {
		T st = statusReport(line);
		if (st != null)
			status = st;
		
		parseReport(line);
	}

	public abstract void parseReport(String line);
	
	public void log(String message)
	{
		log.append(message);
		log.append("\n");
	}

}