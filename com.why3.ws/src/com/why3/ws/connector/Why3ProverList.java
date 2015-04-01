package com.why3.ws.connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class queries a local why3 installation for a list of installed external provers
 * @author alex
 *
 */
public class Why3ProverList extends QueryTool {
	private static final String LIST_PROVERS = "--list-provers";
	private List<Why3ToolInfo> tools;
	private static List<Why3ToolInfo> toolsCached = null;
	private static long timeCached = 0;
	private static final long CACHE_TIME = 0; //3600000; // one hour in milliseconds
	
	private enum STATE {INITIAL, LIST};
	private STATE state = STATE.INITIAL;
	
	private Why3ProverList() throws IOException {
		super(Properties.WHY3_PATH, LIST_PROVERS);
		tools = new ArrayList<Why3ToolInfo>(20);
	}

	/**
	 * Implements a caching (for a period of time) querying of why3 installed prover list.
	 * @return List of prover names and their versions
	 */
	public static synchronized List<Why3ToolInfo> getToolList() {
		if (toolsCached == null || (System.currentTimeMillis() - timeCached >= CACHE_TIME)) {
			Why3ProverList instance;
			try {
				instance = new Why3ProverList();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
			if (instance.hasErrors)
				return null;
			
			toolsCached = instance.getToolInfo();
			timeCached = System.currentTimeMillis();
			return toolsCached;
		} else {
			return toolsCached;
		}
	}
	
	private List<Why3ToolInfo> getToolInfo() {
		try {
			process();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tools;
	}
	
	@Override
	public void finalReport() {
		// do nothing
	}

	@Override
	public void processLine(String line) {
		switch(state) {
		case INITIAL:
			if (line.startsWith("Known provers:"))
				state = STATE.LIST;
			break;
		case LIST:
			line = line.trim().toLowerCase();
			String[] parts = line.split("\\s");
			if (parts.length == 2) {
				String name = parts[0];
				String version = parts[1];
				if (version.length() > 2) {
					version = version.substring(1, version.length() - 1);
					Why3ToolInfo info = new Why3ToolInfo(name, version);
					tools.add(info);
				}
			}
		}
	}

}