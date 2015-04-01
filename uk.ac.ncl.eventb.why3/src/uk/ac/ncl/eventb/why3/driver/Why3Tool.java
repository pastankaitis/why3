package uk.ac.ncl.eventb.why3.driver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eventb.core.seqprover.IProofMonitor;

import uk.ac.ncl.eventb.why3.main.BTheorem;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.main.Why3CallResult.WHY3_RESULT;
import uk.ac.ncl.eventb.why3.main.Why3Plugin;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslator;
import uk.ac.ncl.eventb.why3.translator.TranslationException;

public class Why3Tool extends ReportingTool<WHY3_RESULT>  {
	private TheoremTranslated theorem;
	private File fileinput;
	private String prover;
	private long timelimit;
	
	private static Map<String, WHY3_RESULT> status_map;
	
	static {
		status_map = new HashMap<String, WHY3_RESULT>(5);
		status_map.put( " g0 : Valid ", WHY3_RESULT.VALID);
		status_map.put( " g0 : Invalid ", WHY3_RESULT.INVALID);
		status_map.put( " g0 : Unknown ", WHY3_RESULT.UNKNOWN);
		status_map.put( " g0 : Timeout ", WHY3_RESULT.TIMEOUT);
		status_map.put( " g0 : Failure ", WHY3_RESULT.TOOL_FAILURE);
	}
	
	protected Why3Tool(BTheorem theorem) throws IOException, CoreException, TranslationException {
		super(Why3Plugin.getWhy3LocalPath(), "", WHY3_RESULT.UNKNOWN, status_map);
		TheoremTranslator tt = new TheoremTranslator(theorem, Why3Plugin.isObfuscate());
		this.theorem = tt.translate();
	}

	public Why3Tool(String prover_id, long timeout, TheoremTranslated input) throws IOException {
		super(Why3Plugin.getWhy3LocalPath(), "", WHY3_RESULT.UNKNOWN, status_map);
		this.prover = prover_id;
		this.theorem = input;
	}

	@Override
	public String getToolArguments() {
		return super.getToolArguments() +"prove -P " + prover + " -t " + timelimit + " " + fileinput.getAbsolutePath() + " -T POx -G g0";
	}	
	
	@Override
	public void parseReport(String line) {
		// do nothing
	}
	
	public Why3CallResult check(long timeout, IProofMonitor monitor) {
		
		monitor.setTask("Calling " + prover);
		this.timelimit = timeout;
		
		long start = System.currentTimeMillis();
		fileinput = getFileInput(theorem, "dsl.why");
		
		if (monitor.isCanceled())
			return Why3CallResult.INSTANCE_CANCEL;
		
		if (fileinput == null)
			return Why3CallResult.INSTANCE_FAILURE;		
		
		try {
			process(monitor);
		} catch (Throwable e) {
			e.printStackTrace();
			return Why3CallResult.INSTANCE_UNKNOWN;
		}

		WHY3_RESULT result = getStatus();
		long time  = System.currentTimeMillis() - start;
		
		if (Why3Plugin.DEBUG) {
			if (getStatus() == WHY3_RESULT.VALID) {
				System.out.print("/" + getStatus());
				System.out.print("/" + prover);
				System.out.println("/" + (time - start) / 1000.0);
			}
		}		
		if (result.isDefinite())
			return new Why3CallResult(result, prover, time);
		else
			return Why3CallResult.INSTANCE_UNKNOWN;
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

	@Override
	public void processErrorLine(String line) {
		// do nothing
		
	}		
}
