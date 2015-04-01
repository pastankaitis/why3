package uk.ac.ncl.eventb.why3.driver;

import java.io.File;
import java.io.IOException;

import uk.ac.ncl.eventb.why3.main.Why3CallResult.WHY3_RESULT;
import uk.ac.ncl.eventb.why3.main.Why3Plugin;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;

public class Why3SyntaxCheck extends ToolConnection<WHY3_RESULT>  {
	private final static String ERROR_PREFIX = "File ";
	private TheoremTranslated model;
	private File fileinput;

	public Why3SyntaxCheck(TheoremTranslated input) throws IOException {
		super(Why3Plugin.getWhy3LocalPath(), WHY3_RESULT.UNKNOWN);
		this.model = input;
	}
	
	@Override
	public String getToolArguments() {
		return "prove --type-only " + fileinput.getAbsolutePath();
	}	
	
	public Why3SyntaxCheckResult check() {
		
		fileinput = getFileInput(model, "tran.why");

		if (fileinput == null)
			return new Why3SyntaxCheckResult("Translation failure");
		
		try {
			process(null);
		} catch (Throwable e) {
			e.printStackTrace();
			return new Why3SyntaxCheckResult("Tool failure: " + e.getMessage());
		}

		if (errorMessage != null)
			return new Why3SyntaxCheckResult(errorColStart, errorColEnd, errorMessage);
		else
			return Why3SyntaxCheckResult.NOERROR;
	}	
	
	@Override
	public void finalReport() {

	}

	@Override
	public void processLine(String line) {
		// TODO Auto-generated method stub
		
	}		

	private int errorColStart = -1;
	private int errorColEnd = -1;
	private String errorMessage = null;
	@Override
	public void processErrorLine(String line) {
		if (errorMessage != null)
			return;
		if (line.startsWith(ERROR_PREFIX) && line.endsWith(":")) {
			line = line.substring(0, line.length() - 1).trim();
			String parts[] = line.split(",");
			for(String p: parts) {
				String subparts[] = p.trim().split("\\s+");
				if (subparts.length == 2) {
					if (subparts[0].equals("characters")) {
						String subsubparts[] = subparts[1].trim().split("-");
						if (subsubparts.length == 2) {
							errorColStart = Integer.valueOf(subsubparts[0]);
							errorColEnd = Integer.valueOf(subsubparts[1]);
						}
					}
				}
			}
		} else {
			errorMessage = line;
		}
	}	
	
	public static synchronized File getFileInput(TheoremTranslated input, String suffix)
	{
		// pretty-print into a file
		try {
			File fileinput = mktempFile("proof_file", suffix);
			FileUtil.setFileContents(input.toString(), fileinput);
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

	
}
