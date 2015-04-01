package com.why3.ws.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class ToolConnection<T> implements Runnable {
    private InputStream stderr;
    private InputStream stdout;
    private BufferedReader bout;
    private BufferedReader berr;
    private Process process;
    
    private String tool_path;
    protected StringBuffer log;
    protected T status;
    protected boolean hasErrors = false;
    
	protected ToolConnection(String tool_path, T initial_status) throws IOException {
		this.tool_path = tool_path;
		this.status = initial_status;
		
		if (tool_path == null) {
			throw new IOException("Tool path  " + tool_path + " is not valid");
		}
		
		log = new StringBuffer();
	}
	
	/**
	 * Command line arguments like path to the script file
	 * @return
	 * @throws IOException 
	 */
	public abstract String getToolArguments() throws IOException;
	
	@Override
	public void run()
	{
		hasErrors = false;
		try {
			String s = readErrLine();
			while (s != null) {
				s = readErrLine();
			}
			
			berr.close();
			stderr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public boolean hasErrors() {
		return hasErrors;
	}
	
	public T getStatus()
	{
		return status;
	}
	
	public String getReport()
	{
		return log.toString();
	}

	public void stop() {
		try {
			if (process != null) {
				// System.out.println("Killing process " + getFullPath());
				stdout.close();
				stderr.close();
				process.destroy();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public void process() throws IOException
	{
		process = mkProcess();
		stderr = process.getErrorStream();
		stdout = process.getInputStream();
		bout = new BufferedReader(new InputStreamReader(stdout));
		berr = new BufferedReader(new InputStreamReader(stderr));
		
		Thread thread = new Thread(this);
		thread.start();
		
		try {
			String s = readLine();
			while (s != null) {
				processLine(s);
				s = readLine();
				
			}
			bout.close();
			stdout.close();
			
			finalReport();
			
		} catch (IOException e) {
	
			e.printStackTrace();
			log.append("\n***\n");
			log.append("EXCEPTION:" + e.getMessage() + "\n");
			log.append("***\n");
		}
	}
		
	public abstract void finalReport();

	public abstract void processLine(String line);
	
	
	public String getFullPath() throws IOException
	{
		return tool_path + " " + getToolArguments();
	}
	
	private Process mkProcess() throws IOException
	{
		String path = getFullPath();
		Process process = Runtime.getRuntime().exec(path);
		return process;
	}
	    
	private String readLine() throws IOException {
		String line = bout.readLine();
		return line;
	} 

	private String readErrLine() throws IOException {
		try {
			String line = berr.readLine();
			if (line != null) {
				System.err.println(line);
				hasErrors = true;
			}
			return line;
		} catch (Throwable e) {
			return null;
		}
	} 	
      
}
