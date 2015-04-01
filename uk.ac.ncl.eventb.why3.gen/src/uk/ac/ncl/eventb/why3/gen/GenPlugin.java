package uk.ac.ncl.eventb.why3.gen;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class GenPlugin extends AbstractUIPlugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "uk.ac.ncl.eventb.why3.gen"; //$NON-NLS-1$

	public static final String EDITOR_ID = "uk.ac.ncl.eventb.why3.gen.editor"; //$NON-NLS-1$
	
	// The shared instance
	private static GenPlugin plugin;
	
	private TGenRegistry tgenregistry;

	/**
	 * The constructor
	 */
	public GenPlugin() {
		tgenregistry = new TGenRegistry();
	}

	public TGenRegistry getTGenRegistry() {
		return tgenregistry;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static GenPlugin getDefault() {
		return plugin;
	}

	@Override
	public void earlyStartup() {
		// TODO Auto-generated method stub
		
	}

}
