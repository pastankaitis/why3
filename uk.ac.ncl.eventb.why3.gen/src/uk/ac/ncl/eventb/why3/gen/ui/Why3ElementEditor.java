package uk.ac.ncl.eventb.why3.gen.ui;

import static org.eclipse.sapphire.ui.forms.PropertyEditorPart.DATA_BINDING;
import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdfill;
import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdhfill;
import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdhindent;
import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdhspan;
import static org.eclipse.sapphire.ui.forms.swt.GridLayoutUtil.gdwhint;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.sapphire.FilteredListener;
import org.eclipse.sapphire.ValuePropertyContentEvent;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.ToolTip;
import org.eventb.internal.core.ProofMonitor;
import org.rodinp.keyboard.ui.preferences.PreferenceConstants;

import uk.ac.ncl.eventb.why3.cloud.Why3CloudDriver;
import uk.ac.ncl.eventb.why3.driver.IProverDriver;
import uk.ac.ncl.eventb.why3.driver.Why3LocalDriver;
import uk.ac.ncl.eventb.why3.gen.GenException;
import uk.ac.ncl.eventb.why3.gen.opregistry.EventBOperator;
import uk.ac.ncl.eventb.why3.gen.opregistry.OperatorRegistry;
import uk.ac.ncl.eventb.why3.gen.translation.ElementTranslator;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.main.Why3CallResult.WHY3_RESULT;
import uk.ac.ncl.eventb.why3.main.Why3Plugin;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;


public class Why3ElementEditor extends ValuePropertyEditorPresentation implements IWhy3FormulaSource {
	private static final StyleRange[] EMPTY_STYLE = new StyleRange[0];		
	private StyledText text;
	private ToolBar toolbar;
	private AbstractBinding binding;
	private ToolTip tooltip;
	private Label statusLabel;
	private ToolItem check;
	private TranslationRule rule;
	private TranslationCondition condition;
	private ITranslationElement element;
	private Why3SyntaxChecker syntaxChecker;
	private IProverDriver driver;
	private boolean setupPhase = false;
	//private SyntaxHighlighter syntaxHighlighter;
	
	public Why3ElementEditor(FormComponentPart part, SwtPresentation parent, Composite composite) {
		super(part, parent, composite);
		element = (ITranslationElement) part.parent().getModelElement();
		if (element instanceof TranslationRule) {
			rule = (TranslationRule) element;
		} else if (element instanceof TranslationCondition) {
			condition = (TranslationCondition) element;
			if (Why3Plugin.isCloud()) {
				driver = new Why3CloudDriver();
			} else {
				driver = new Why3LocalDriver();
			}
		}

		if (rule == null) {
			syntaxChecker = new Why3SyntaxChecker(this, false);
		} else {
			syntaxChecker = new Why3SyntaxChecker(new RuleSourceProvider(), true);
		}
		
		//syntaxHighlighter = new SyntaxHighlighter(this);
	}
	
	private class RuleSourceProvider implements IWhy3FormulaSource {
		@Override
		public ITranslationElement getSourceElement() {
			return rule;
		}
		
		@Override
		public void markError(int from, int length, String message) {
			Why3ElementEditor.this.markError(from, length, message);
		}

		@Override
		public void markWarning(int from, int length, String message) {
			Why3ElementEditor.this.markWarning(from, length, message);
		}

		@Override
		public void markInfo(int from, int length, String message) {
			Why3ElementEditor.this.markInfo(from, length, message);
		}

		@Override
		public void noErrors() {
			element.setChecked(false);
		}

		@Override
		public void reportStart() {
			Why3ElementEditor.this.reset();
		}

		@Override
		public void reportEnd() {
			//syntaxHighlighter.syntaxHighlight();
			Why3ElementEditor.this.setStatus();
		}

		@Override
		public void colour(int from, int length, SYMBOL_CLASS symbol_class) {
			Why3ElementEditor.this.colour(from, length, symbol_class);
		}

		
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

		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (text.isDisposed())
					return;
				
				text.replaceStyleRanges(0, text.getText().length(), EMPTY_STYLE);
				
				if (setupPhase)
					return;
				
				if (condition != null) {
					syntaxChecker.check();
				} else if (rule != null) {
					// compute string offset
					EventBOperator operator = OperatorRegistry.getOperator(rule.getSource().content());
	        		String name = rule.getTarget().content();
	        		String raw = operator.toString(name, null);
					syntaxChecker.check(raw.length() + 2);
				}
				
				
			}
		});		
		
		// handle error tooltips 
		text.addListener(SWT.MouseMove, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (text.isDisposed())
					return;
				
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
		
		if (condition != null) {
			final Composite comp = new Composite(parent, SWT.NO_BACKGROUND);
			comp.setLayoutData(gdhindent(gdwhint(gdhspan(gdhfill(), 2), 100), 14));
			comp.setLayout(new GridLayout(2, false));
			
			
			statusLabel = new Label(comp, SWT.NONE);
			statusLabel.setImage(IconFactory.proof_unknown);
			statusLabel.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));
		
			toolbar = new ToolBar(comp, SWT.FLAT | SWT.WRAP | SWT.RIGHT);	
			toolbar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
			fillToolbar();
			addControl(toolbar);

			comp.setVisible(isLemma());
			// kind (axiom/lemma) listener
			final org.eclipse.sapphire.Listener propertyListener = new FilteredListener<ValuePropertyContentEvent>() {
				@Override
				protected void handleTypedEvent(final ValuePropertyContentEvent event) {
					if (!comp.isDisposed())
							comp.setVisible(isLemma());
				}
			};
			condition.getKind().attach(propertyListener);			
		}
		
		if (element.isChecked().empty() || (!element.isChecked().empty() && !element.isChecked().content())) {
			syntaxChecker.check();
		} else {
			setStatus();
		}
		//syntaxHighlighter.syntaxHighlight();
		setupPhase = false;
	}
	
	private boolean isLemma() {
		if (condition == null)
			return false;
		
		if (condition.getKind().empty())
			return false;
		
		return condition.getKind().content() == ConditionKind.LEMMA || condition.getKind().content() == ConditionKind.TEST;	
	}
	
	private void fillToolbar() {
		check = new ToolItem(toolbar, SWT.FLAT | SWT.PUSH);
		check.setText("check");
		check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (condition != null) {
						ProverRun op = new ProverRun(condition);
						new ProgressMonitorDialog(toolbar.getShell()).run(true, true, op);
						Why3CallResult result = op.getResult();
						condition.setStatus(result.getStatus().getCode());
						setStatus();
					}
				} catch (InvocationTargetException | InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	

	private void reset() {
		element.isChecked().clear();
		if (condition != null && !condition.getStatus().empty())
			condition.getStatus().clear();
	}

	
	private void setStatus() {
		if (text.isDisposed() || toolbar == null || !toolbar.isVisible())
			return;
		
		if (!element.isChecked().empty() && element.isChecked().content()) {
			if (condition != null) {
				if (!condition.getStatus().empty()) {
					switch(condition.getStatus().content()) {
					case Why3CallResult.VALID_CODE:
						statusLabel.setImage(IconFactory.proof_valid);
						check.setEnabled(false);
						break;
					case Why3CallResult.INVALID_CODE:
						statusLabel.setImage(IconFactory.proof_invalid);
						check.setEnabled(false);
						break;
					case Why3CallResult.PANIC_CODE:
						statusLabel.setImage(IconFactory.proof_bug);
						check.setEnabled(false);
						break;
					case Why3CallResult.TOOL_FAILURE_CODE:
						statusLabel.setImage(IconFactory.proof_error);
						check.setEnabled(true);
						break;
					default:
						statusLabel.setImage(IconFactory.proof_unknown);
						break;
					}
				} else {
					statusLabel.setImage(IconFactory.proof_unknown);
					check.setEnabled(true);
				}
			}
		} else {
			if (condition != null) {
				check.setEnabled(false);
				check.setToolTipText("Syntax or typing error");
				statusLabel.setImage(IconFactory.proof_unknown);
			}
		}
	}	
	
    public static final class Factory extends PropertyEditorPresentationFactory
    {
        @Override
        public PropertyEditorPresentation create( final PropertyEditorPart part, final SwtPresentation parent, final Composite composite )
        {
        	return new Why3ElementEditor( part, parent, composite );
        }
    }

	@Override
	public void markError(int from, int length, String message) {
		if (text.isDisposed())
			return;
		
		element.setChecked(false);
		
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
	public void colour(int from, int length, SYMBOL_CLASS kind) {
		if (from >= 0 && length > 0 && from + length <= text.getText().length() && kind != SYMBOL_CLASS.OTHER && text.getStyleRanges(from, length).length == 0) {
			StyleRange styleRange = new StyleRange();
			styleRange.start = from;
			styleRange.length = length;
			//styleRange.underline = false;
		
			switch(kind) {
			case BUILTIN_TYPE:
				styleRange.foreground = StyleColours.orange;
				break;
			case CONTRIBUTED_TYPE:
				styleRange.foreground = StyleColours.blue;
				break;
			case BUILTIN_CONSTANT:
				styleRange.foreground = StyleColours.blue;
				break;
			case KEYWORD:
				//styleRange.fontStyle = SWT.BOLD;
				styleRange.foreground = StyleColours.kwd;
				break;
			case CONTRIBUTED_CONSTANT:
				styleRange.foreground = StyleColours.blue;
				styleRange.fontStyle = SWT.BOLD;
				break;				
			case RULE_CONSTANT:
				styleRange.foreground = StyleColours.black;
				styleRange.underline = true;
				styleRange.fontStyle = SWT.BOLD;
				break;			
			case LITERAL:
				styleRange.foreground = StyleColours.green;
				break;	
			case COMMENT:
				styleRange.foreground = StyleColours.gray;
				break;
			}
			text.setStyleRange(styleRange);
		}	
	}		
	
	@Override
	public void noErrors() {
		element.setChecked(true);
		element.setCachedTriggers(Arrays.asList(new Long(1), new Long(2)));
	}	

	@Override
	public void reportStart() {
		reset();
	}

	@Override
	public void reportEnd() {
		//syntaxHighlighter.syntaxHighlight();
		setStatus();
	}
	
	@Override
	public ITranslationElement getSourceElement() {
		return element;
	}
	
	class ProverRun implements IRunnableWithProgress {
		private Why3CallResult result;
		private TranslationCondition lemma;
		
		public ProverRun(TranslationCondition lemma) {
			this.lemma = lemma;
		}

		@Override
		public void run(IProgressMonitor monitor) throws InterruptedException {
			try {
				ProofMonitor pmonitor = new ProofMonitor(monitor);
				pmonitor.setTask("Checking direct case");
				// positive case
				TheoremTranslated input = ElementTranslator.fromCondition(lemma);
				Why3CallResult result1 = driver.check(input, Why3Plugin.getDefaultScenario(), pmonitor);

				pmonitor.setTask("Checking negative case");
				// negative case
				TheoremTranslated input2 = input.getNegatedForm();
				Why3CallResult result2 = driver.check(input2, Why3Plugin.getDefaultScenario(), pmonitor);

				// adjudicate into a single result
				result = result1.adjudicate(result2.negate());
			} catch (GenException e) {
				e.printStackTrace();
				result = Why3CallResult.INSTANCE_FAILURE;
			}
		}

		public Why3CallResult getResult() {
			return result;
		}
	}	
}
