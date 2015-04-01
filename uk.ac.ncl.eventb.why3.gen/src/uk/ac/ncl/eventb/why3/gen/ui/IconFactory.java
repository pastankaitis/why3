package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.graphics.Image;

import uk.ac.ncl.eventb.why3.gen.GenPlugin;

public class IconFactory {

	public static final Image proof_valid;
	public static final Image proof_invalid;
	public static final Image proof_unknown;
	public static final Image proof_error;
	public static final Image proof_bug;
	
	static {
		proof_valid = ResourceManager.getPluginImage(GenPlugin.PLUGIN_ID, "icons/check_32.png"); 
		proof_invalid = ResourceManager.getPluginImage(GenPlugin.PLUGIN_ID, "icons/cross_32.png"); 
		proof_unknown = ResourceManager.getPluginImage(GenPlugin.PLUGIN_ID, "icons/question_32.png"); 
		proof_error = ResourceManager.getPluginImage(GenPlugin.PLUGIN_ID, "icons/exlamation_32.png"); 
		proof_bug = ResourceManager.getPluginImage(GenPlugin.PLUGIN_ID, "icons/bug_error.png"); 
	}

}
