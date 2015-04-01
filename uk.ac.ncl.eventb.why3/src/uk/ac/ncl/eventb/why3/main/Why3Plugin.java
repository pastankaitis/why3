package uk.ac.ncl.eventb.why3.main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import uk.ac.ncl.eventb.why3.cloud.PlatformFingerprint;
import uk.ac.ncl.eventb.why3.prefpage.PreferenceConstants;

import com.why3.ws.scenario.Scenarios;


public class Why3Plugin extends AbstractUIPlugin {
	public static final boolean DEBUG = true;
	private static Logger logger;	
	private static long uuid;
	private static boolean hasUUID = false;	
	
	public Why3Plugin() {
	}

	public static Why3Plugin getDefault() {
		return plugin;
	}	
	
	/**
	 * The shared instance.
	 */
	private static Why3Plugin plugin;
	
	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		try {
			uuid = PlatformFingerprint.getUUID();
			hasUUID = true;
		} catch (Throwable e) {
			System.err.println("Could not compute UUID: " + e.getMessage());
		} 
		setupLogger();
	}	

	
	/**
	 * Returns the system unique (hopefuly) identifier based on mac address
	 * @return unique UUID
	 */
	public static long getUUID() {
		return uuid;
	}

	/**
	 * Returns the number of bits that make up the uuid, the longer the better
	 * @return
	 */
	public static int getUUIDStrength() {
		return PlatformFingerprint.getBits();
	}
	
	public static void logInfo(String message) {
		logger.info(message);
	}

	public static void logError(String message) {
		logger.severe(message);
	}
	
	public static Logger getLogger() {
		return logger;
	}
	
	private void setupLogger() {
		logger = Logger.getLogger("Why3ErrorLog");
		try {
			String logPath = getLocalLog();
			if (logPath == null || logPath.trim().length() == 0)
				return;
			FileHandler fh = new FileHandler(logPath);
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			
			// suppress output to console
			if (!DEBUG)
				logger.setUseParentHandlers(false);
		} catch (IOException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns user-configured URI to why3 cloud server
	 * @return
	 */
	public static String getCloudURI(String service) {
		return plugin.getPreferenceStore().getString(PreferenceConstants.P_CLOUD_URI) + "/" + service;
	}
	
	public static String getLocalLog() {
		return plugin.getPreferenceStore().getString(PreferenceConstants.P_LOCAL_LOG);
	}	
	
	public static String getDefaultScenario() {
		String def = plugin.getPreferenceStore().getString(PreferenceConstants.P_DEF_SCN);
		if (def != null && def.trim().length() > 0) 
			return def;
		else
			return Scenarios.DEFAULT_SCENARIO_NAME;
	}		
	
	public static int getCloudTimeout() {
		return plugin.getPreferenceStore().getInt(PreferenceConstants.P_CLOUD_TO);
	}

	public static int getLocalTimeout() {
		return plugin.getPreferenceStore().getInt(PreferenceConstants.P_LOCAL_TO);
	}
	
	/**
	 * Returns user-configured to local why3
	 * @return
	 */
	public static String getWhy3LocalPath() {
		return plugin.getPreferenceStore().getString(PreferenceConstants.P_WHY3_PATH);
	}
	
	public static boolean hasWhy3CloudPath() {
		String path = plugin.getPreferenceStore().getString(PreferenceConstants.P_CLOUD_URI).trim();
		if (path == null || path.length() == 0 || !path.contains("com.why3.ws/rest"))
			return false;
		
		return true;
	}	
	
	public static boolean hasWhy3LocalPath() {
		String path = getWhy3LocalPath();
		if (path == null || path.length() == 0 || !path.contains("why3"))
			return false;
		
		return true;
	}		
	
	public static boolean isCloud() {
		return plugin.getPreferenceStore().getString(PreferenceConstants.P_LOCAL_OR_CLOUD).equals("cloud");
	}
	
	public static boolean isObfuscate() {
		return plugin.getPreferenceStore().getBoolean(PreferenceConstants.P_OBFUSCATE);
	}	
}
