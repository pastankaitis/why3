package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class StyleColours {
	public static final Color errorBackground;  
	public static final Color errorForeground;  

	public static final Color warningBackground;  
	public static final Color warningForeground;  

	public static final Color warningSquiggle;  	
	public static final Color green;  	
	public static final Color orange;  	
	public static final Color black;  	
	public static final Color red;  	
	public static final Color blue;  	
	public static final Color lightGray;  	
	public static final Color kwd;  
	public static final Color gray;  	
	
	public static final Styler errorStyler;
	public static final Styler warningStyler;
	public static final Styler literalStyler;
	public static final Styler typeStyler;
	public static final Styler subTypeStyler;
	public static final Styler commentStyler;
	
	static {
		Display display = PlatformUI.getWorkbench().getDisplay();
		errorBackground 	= new Color(display, 180, 0, 0);
		errorForeground 	= new Color(display, 255, 255, 255);

		warningBackground 	= new Color(display, 120, 120, 0);
		warningForeground 	= new Color(display, 255, 255, 255);

		warningSquiggle 	= new Color(display, 120, 120, 0);
		green 				= new Color(display, 0, 140, 0);
		orange 				= new Color(display, 255, 140, 00);
		black 				= new Color(display, 0, 0, 0);
		red 				= new Color(display, 200, 0, 0);
		blue 				= new Color(display, 40, 40, 160);
		lightGray 			= new Color(display, 200, 200, 200);
		kwd 				= new Color(display, 60, 20, 90);
		gray 			= new Color(display, 80, 80, 80);
		
		errorStyler 		= new Styler() {
			@Override
			public void applyStyles(TextStyle textStyle) {
				 textStyle.underline = true;
			     textStyle.underlineStyle = SWT.UNDERLINE_ERROR;
			     textStyle.underlineColor = JFaceResources.getColorRegistry().get(JFacePreferences.ERROR_COLOR);				
			}
		};
		
		warningStyler 		= new Styler() {
			@Override
			public void applyStyles(TextStyle textStyle) {
				 textStyle.underline = true;
			     textStyle.underlineStyle = SWT.UNDERLINE_SQUIGGLE;
			     textStyle.underlineColor = warningSquiggle;				
			}
		};		

		commentStyler 		= new Styler() {
			@Override
			public void applyStyles(TextStyle textStyle) {
				 textStyle.underline = false;
			     textStyle.foreground = orange;
			     if (textStyle instanceof StyleRange) {
			    	 StyleRange sr = (StyleRange) textStyle;
			    	 sr.fontStyle = SWT.ITALIC;
			     }
			}
		};		
		
		literalStyler 		= new Styler() {
			@Override
			public void applyStyles(TextStyle textStyle) {
				 textStyle.underline = false;
			     textStyle.foreground = green;
			     if (textStyle instanceof StyleRange) {
			    	 StyleRange sr = (StyleRange) textStyle;
			    	 sr.fontStyle = SWT.BOLD;
			     }
			}
		};
		
		
		typeStyler 		= new Styler() {
			@Override
			public void applyStyles(TextStyle textStyle) {
				 textStyle.underline = false;
			     textStyle.foreground = black;
			     if (textStyle instanceof StyleRange) {
			    	 StyleRange sr = (StyleRange) textStyle;
			    	 sr.fontStyle = SWT.BOLD;
			     }
			}
		};		

		subTypeStyler 		= new Styler() {
			@Override
			public void applyStyles(TextStyle textStyle) {
				 textStyle.underline = false;
			     textStyle.foreground = red;
			     textStyle.borderStyle = SWT.BORDER_DASH;
			     textStyle.borderColor = black;
			}
		};		
		
		
	}
}