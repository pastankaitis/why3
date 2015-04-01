package uk.ac.ncl.eventb.why3.prefpage.scen;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class StyleColours {
	public static final Color errorBackground;  
	public static final Color errorForeground;  

	public static final Color warningBackground;  
	public static final Color warningForeground;  

	public static final Color warningSquiggle;  	
	static {
		Display display = PlatformUI.getWorkbench().getDisplay();
		errorBackground 	= new Color(display, 180, 0, 0);
		errorForeground 	= new Color(display, 255, 255, 255);

		warningBackground 	= new Color(display, 120, 120, 0);
		warningForeground 	= new Color(display, 255, 255, 255);

		warningSquiggle 	= new Color(display, 120, 120, 0);
		
	}
}