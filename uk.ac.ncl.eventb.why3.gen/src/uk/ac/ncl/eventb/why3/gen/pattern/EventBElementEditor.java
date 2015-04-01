package uk.ac.ncl.eventb.why3.gen.pattern;

import static org.eclipse.sapphire.ui.forms.PropertyEditorPart.DATA_BINDING;
import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdfill;
import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdhfill;
import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdhindent;
import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdhspan;
import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdwhint;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.sapphire.ui.forms.FormComponentPart;
import org.eclipse.sapphire.ui.forms.PropertyEditorPart;
import org.eclipse.sapphire.ui.forms.swt.AbstractBinding;
import org.eclipse.sapphire.ui.forms.swt.PropertyEditorPresentation;
import org.eclipse.sapphire.ui.forms.swt.PropertyEditorPresentationFactory;
import org.eclipse.sapphire.ui.forms.swt.SwtPresentation;
import org.eclipse.sapphire.ui.forms.swt.ValuePropertyEditorPresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolTip;
import org.eventb.core.ast.FormulaFactory;
import org.eventb.core.ast.GivenType;
import org.eventb.core.ast.ITypeEnvironment;
import org.eventb.core.ast.Type;
import org.eventb.internal.core.typecheck.TypeEnvironmentBuilder;
import org.rodinp.keyboard.ui.RodinKeyboardUIPlugin;
import org.rodinp.keyboard.ui.preferences.PreferenceConstants;

import uk.ac.ncl.eventb.why3.gen.ui.PatternParameter;
import uk.ac.ncl.eventb.why3.gen.ui.StyleColours;
import uk.ac.ncl.eventb.why3.gen.ui.StyledTextFieldBinding;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationPattern;
import uk.ac.ncl.eventb.why3.gen.ui.UndoRedoImpl;


public class EventBElementEditor extends ValuePropertyEditorPresentation implements IEventBFormulaSource {
	//private static final StyleRange[] EMPTY_STYLE = new StyleRange[0];		
	private static final ITypeEnvironment EMPTY_TE = new TypeEnvironmentBuilder(FormulaFactory.getDefault());
	private StyledText text;
	private AbstractBinding binding;
	private ToolTip tooltip;
	private boolean setupPhase = false;
	private TranslationPattern pattern;
	
	public EventBElementEditor(FormComponentPart part, SwtPresentation parent, Composite composite) {
		super(part, parent, composite);	
	}
	
	@Override
	protected void createContents(final Composite parent) {
		setupPhase = true;
		tooltip = new ToolTip(parent.getShell(), SWT.ICON_ERROR);
		final boolean scaleVertically = part().getScaleVertically();
		text = new StyledText(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		text.setLayoutData(gdhindent(gdwhint(gdhspan((scaleVertically ? gdfill() : gdhfill()), 2), 100), 14));
		text.setEditable(true);
		
		text.setAlwaysShowScrollBars(false);
		
		text.setFont(JFaceResources.getFont(PreferenceConstants.RODIN_MATH_FONT));

		
		// enable undo/redo
		new UndoRedoImpl(text);

		text.addModifyListener(RodinKeyboardUIPlugin.getDefault().getRodinModifyListener());
		 
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (setupPhase)
					return;
			}
		});		
		
		// completion
		text.addVerifyListener( new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent event) {
				if (setupPhase)
					return;

				if (event.end - event.start == 0 && event.start > 0) {
					// todo
				}
			}
			
		});		
		
		// handle error tooltips 
		text.addListener(SWT.MouseMove, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// It is up to the application to determine when and how a link should be activated.
				// In this snippet links are activated on mouse down when the control key is held down 
				try {
					int offset = text.getOffsetAtLocation(new Point (event.x, event.y));
					StyleRange style = text.getStyleRangeAtOffset(offset);
					if (style != null) {
						if (style.data instanceof String) {
							tooltip.setText((String) style.data);
							tooltip.setLocation(event.x, event.y);
							tooltip.setAutoHide(true);
							tooltip.setVisible(true);
						}
					} else {
						tooltip.setVisible(false);
					}
				} catch (IllegalArgumentException e) {
					// e.printStackTrace();
					// no character under event.x, event.y
				}
			}
		});			
		
		binding = new StyledTextFieldBinding(this, text);
		text.setData(DATA_BINDING, binding);
		addControl(text);
		setupPhase = false;
	}
	
    public static final class Factory extends PropertyEditorPresentationFactory
    {
        @Override
        public PropertyEditorPresentation create( final PropertyEditorPart part, final SwtPresentation parent, final Composite composite )
        {
        	return new EventBElementEditor( part, parent, composite );
        }
    }

	@Override
	public void markError(int from, int length, String message) {
		if (text.isDisposed())
			return;
		
		if (from >= 0 && length > 0 && from + length <= text.getText().length()) {
			StyleRange styleRange = new StyleRange();
			styleRange.start = from;
			styleRange.length = length;
			styleRange.underline = true;
			styleRange.underlineColor = StyleColours.red;
			styleRange.underlineStyle = SWT.UNDERLINE_ERROR;
			styleRange.data = message;
			text.setStyleRange(styleRange);
		} else {
			StyleRange styleRange = new StyleRange();
			styleRange.start = 0;
			styleRange.length = text.getText().length();
			styleRange.underline = true;
			styleRange.underlineColor = StyleColours.red;
			styleRange.underlineStyle = SWT.UNDERLINE_ERROR;
			styleRange.data = message;
			text.setStyleRange(styleRange);
		}
	}

	@Override
	public void markWarning(int from, int length, String message) {
		if (text.isDisposed())
			return;
		
		if (from >= 0 && length > 0 && from + length <= text.getText().length()) {
			StyleRange styleRange = new StyleRange();
			styleRange.start = from;
			styleRange.length = length;
			styleRange.underline = true;
			styleRange.underlineColor = StyleColours.orange;
			styleRange.underlineStyle = SWT.UNDERLINE_SQUIGGLE;
			styleRange.data = message;
			text.setStyleRange(styleRange);
		}
	}

	@Override
	public void markInfo(int from, int length, String message) {
		if (text.isDisposed())
			return;

		if (from >= 0 && length > 0 && from + length <= text.getText().length()) {
			StyleRange styleRange = new StyleRange();
			styleRange.start = from;
			styleRange.length = length;
			styleRange.underline = true;
			styleRange.underlineColor = StyleColours.blue;
			styleRange.underlineStyle = SWT.UNDERLINE_SINGLE;
			styleRange.data = message;
			text.setStyleRange(styleRange);
		}
	}
	
	
	@Override
	public void noErrors() {

	}	

	@Override
	public void reportStart() {

	}

	@Override
	public void reportEnd() {

	}

	@Override
	public String getSource() {
		return text.getText();
	}

	@Override
	public ITypeEnvironment getITypeEnvironment() {
		if (pattern != null) {
			try {
				TypeEnvironmentBuilder newte = new TypeEnvironmentBuilder(FormulaFactory.getDefault());
				for(PatternParameter pp: pattern.getTypes()) {
					Type type = PatternUtils.parseType(pp.getType().content());
					if (type == null)
						return null;
					for (GivenType gt: type.getGivenTypes()) {
						if (newte.getType(gt.getName()) == null)
							newte.addGivenSet(gt.getName());
					}
					newte.addName(pp.getId().content(), type);
				}
				return newte;
			} catch (CoreException e) {
				return null;
			}
		} else {
			return EMPTY_TE;
		}
	}	
}
