package uk.ac.ncl.eventb.why3.prefpage;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import com.why3.ws.scenario.Scenarios;

import uk.ac.ncl.eventb.why3.main.Why3Plugin;
import uk.ac.ncl.eventb.why3.prefpage.scen.ScenarioRegistry;


public class Why3PreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public Why3PreferencePage() {
		super(GRID);
		setPreferenceStore(Why3Plugin.getDefault().getPreferenceStore());
		setDescription("Why3 adapter preferences");
	}
	
	private String[][] compileScenarioNames() {
		int size = ScenarioRegistry.getInstance().size() + Scenarios.SCENARIOS.size();
		String[][] result = new String[size][2];
		
		int i = 0;
		for(String k: ScenarioRegistry.getInstance().keySet()) {
			result[i][0] = k;
			result[i][1] = k;
			i++;
		}
		
		for(String k: Scenarios.SCENARIOS.keySet()) {
			result[i][0] = k;
			result[i][1] = k;
			i++;
		}
		
		return result;
		
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new FileFieldEditor(PreferenceConstants.P_WHY3_PATH, 
				"&Path to why3:", getFieldEditorParent()));
		
		addField(new IntegerFieldEditor(PreferenceConstants.P_LOCAL_TO, "Overall local timeout (secs):", getFieldEditorParent()));		
		addField(new RadioGroupFieldEditor(
				PreferenceConstants.P_LOCAL_OR_CLOUD,
			"Local or remote mode",
			1,
			new String[][] { { "Local", "local" }, {
				"Cloud", "cloud" }
		}, getFieldEditorParent()));
		
		addField(new BooleanFieldEditor(PreferenceConstants.P_OBFUSCATE, "Obfuscate", getFieldEditorParent()));
		
		addField(new StringFieldEditor(PreferenceConstants.P_CLOUD_URI, "Cloud URI:", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceConstants.P_DEF_SCN, "Default scenario:",
				compileScenarioNames(),
				getFieldEditorParent())
				);
		addField(new IntegerFieldEditor(PreferenceConstants.P_CLOUD_TO, "Overall cloud timeout (secs):", getFieldEditorParent()));		
		
		addField(new StringFieldEditor(PreferenceConstants.P_LOCAL_LOG, "Log file (local):", getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}