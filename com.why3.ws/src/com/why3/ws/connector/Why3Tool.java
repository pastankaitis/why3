package com.why3.ws.connector;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.why3.ws.TheoremTranslated;
import com.why3.ws.connector.Why3CallResult.WHY3_RESULT;


public class Why3Tool extends ReportingTool<WHY3_RESULT>  {
	private static Map<String, WHY3_RESULT> status_map;

	private TheoremTranslated model;
	private File fileinput;
	private String prover; 
	private long timelimit;
	
	static {
		status_map = new HashMap<String, WHY3_RESULT>(5);
		status_map.put( " g0 : Valid ", WHY3_RESULT.VALID);
		status_map.put( " g0 : Invalid ", WHY3_RESULT.INVALID);
		status_map.put( " g0 : Unknown ", WHY3_RESULT.UNKNOWN);
		status_map.put( " g0 : Timeout ", WHY3_RESULT.TIMEOUT);
		status_map.put( " g0 : Failure ", WHY3_RESULT.TOOL_FAILURE);
	}
	
	public Why3Tool(String prover, long timelimit, TheoremTranslated theorem) throws IOException {
		super(Properties.WHY3_PATH, "", WHY3_RESULT.UNKNOWN, status_map);
		this.model = theorem;
		this.prover = prover;
		this.timelimit = timelimit;
	}

	@Override
	public String getToolArguments() {
		return super.getToolArguments() +" prove -P " + prover + " -t " + timelimit + " " + fileinput.getAbsolutePath() + " -T POx -G g0";
	}	
	
	@Override
	public void parseReport(String line) {
		// do nothing
	}

	public Why3CallResult check() {
		
		long start = System.currentTimeMillis();
		fileinput = getFileInput(model, "dsl.why");
		
		if (fileinput == null)
			return Why3CallResult.INSTANCE_FAILURE;		
		
		try {
			process();
		} catch (Throwable e) {
			e.printStackTrace();
			return Why3CallResult.INSTANCE_FAILURE;
		}

		WHY3_RESULT result = getStatus();
		long time  = System.currentTimeMillis() - start;
		
		// System.out.println("<<why3>>[" + getToolArguments() + "]: " + prover + " with to " + timelimit + " milliseconds: " + result);

		return new Why3CallResult(result, prover, time);

		/*
		if (result.isDefinite())
			return new Why3CallResult(result, prover, time);
		else
			return Why3CallResult.INSTANCE_UNKNOWN;
		*/
	}
	
	@Override
	public void stop() {
		status = WHY3_RESULT.CANCEL;
		super.stop();
	}
	
	@Override
	public void finalReport() {
	}
	
	// fixed-size LRU cache
	private static Map<TheoremTranslated, File> cache = lruCache(5000);
	
	
	public static synchronized File getFileInput(TheoremTranslated input, String suffix)
	{
		// try cache first
		File f = cache.get(input);
		if (f != null)
			return f;
		
		// pretty-print into a file
		try {
			File fileinput = mktempFile("proof_file", suffix);
			fileinput.deleteOnExit();
			FileUtil.setFileContents(input.toString(), fileinput);
			cache.put(input, fileinput);
			return fileinput;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private static File mktempFile(String prefix, String suffix) throws IOException
	{
		return File.createTempFile(prefix, suffix, null);
	}		
		
	private static <K,V> Map<K,V> lruCache(final int maxSize) {
	    return new LinkedHashMap<K, V>(maxSize*4/3, 0.75f, true) {
	        @Override
	        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
	            return size() > maxSize;
	        }
	    };
	}	
	
}
