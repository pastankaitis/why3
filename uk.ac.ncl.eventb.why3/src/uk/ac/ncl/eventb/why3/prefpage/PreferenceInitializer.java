package uk.ac.ncl.eventb.why3.prefpage;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import uk.ac.ncl.eventb.why3.main.Why3Plugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Why3Plugin.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_WHY3_PATH, "/usr/local/bin/why3");
		store.setDefault(PreferenceConstants.P_LOCAL_TO, 10);
		store.setDefault(PreferenceConstants.P_CLOUD_TO, 10);
		store.setDefault(PreferenceConstants.P_DEF_SCN, "fast");
		store.setDefault(PreferenceConstants.P_LOCAL_OR_CLOUD, "local");
		store.setDefault(PreferenceConstants.P_OBFUSCATE, false);
		store.setDefault(PreferenceConstants.P_CLOUD_URI, "http://ec2-54-194-206-149.eu-west-1.compute.amazonaws.com/com.why3.ws/rest/service/run");
	}

}
