
package uk.ac.ncl.eventb.why3.gen.iplemma;
import static org.eventb.internal.ui.prover.ProverUIUtils.getProofStateDelta;
import static org.eventb.internal.ui.prover.ProverUIUtils.getUserSupportDelta;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.eventb.core.EventBPlugin;
import org.eventb.core.ast.FreeIdentifier;
import org.eventb.core.ast.GivenType;
import org.eventb.core.ast.ISealedTypeEnvironment;
import org.eventb.core.ast.PowerSetType;
import org.eventb.core.ast.Predicate;
import org.eventb.core.ast.ProductType;
import org.eventb.core.ast.Type;
import org.eventb.core.pm.IProofState;
import org.eventb.core.pm.IProofStateDelta;
import org.eventb.core.pm.IUserSupport;
import org.eventb.core.pm.IUserSupportDelta;
import org.eventb.core.pm.IUserSupportManager;
import org.eventb.core.pm.IUserSupportManagerDelta;
import org.eventb.core.seqprover.IProofTreeNode;
import org.eventb.internal.core.ProofMonitor;
import org.eventb.internal.ui.prover.ProverUI;
import org.rodinp.keyboard.ui.preferences.PreferenceConstants;

import uk.ac.ncl.eventb.why3.cloud.Why3CloudDriver;
import uk.ac.ncl.eventb.why3.driver.IProverDriver;
import uk.ac.ncl.eventb.why3.driver.Why3LocalDriver;
import uk.ac.ncl.eventb.why3.gen.GenException;
import uk.ac.ncl.eventb.why3.gen.translation.ElementTranslator;
import uk.ac.ncl.eventb.why3.gen.ui.ITranslationElement;
import uk.ac.ncl.eventb.why3.gen.ui.IWhy3FormulaSource;
import uk.ac.ncl.eventb.why3.gen.ui.IconFactory;
import uk.ac.ncl.eventb.why3.gen.ui.StyleColours;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationCondition;
import uk.ac.ncl.eventb.why3.gen.ui.UndoRedoImpl;
import uk.ac.ncl.eventb.why3.gen.ui.Why3SyntaxChecker;
import uk.ac.ncl.eventb.why3.main.BTheorem;
import uk.ac.ncl.eventb.why3.main.Why3CallResult;
import uk.ac.ncl.eventb.why3.main.Why3Plugin;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslated;
import uk.ac.ncl.eventb.why3.translator.TheoremTranslator;
import uk.ac.ncl.eventb.why3.translator.TranslationException;
import uk.ac.ncl.eventb.why3.translator.TranslationTypesContext;


public class SchematicLemmaPage extends Page implements IPropertyChangeListener, ILemmaPage, IWhy3FormulaSource {
	private static final StyleRange[] EMPTY_STYLE = new StyleRange[0];			
	private static final IUserSupportManager USM = EventBPlugin.getUserSupportManager();
	protected final IUserSupport userSupport;
	private StyledText text;
	private Font font;
	private ToolItem check;
	private Label statusLabel;
	private Composite control;
	private Composite tools;
	private ToolTip tooltip;
	private boolean setupPhase = false;
	private Why3SyntaxChecker syntaxChecker;
	
	/**
	 * Constructor.
	 * 
	 * @param userSupport
	 *            the User Support associated with this Goal Page.
	 */
	public SchematicLemmaPage(ProverUI proverUI, IUserSupport userSupport) {
		super();
		this.userSupport = userSupport;
		syntaxChecker = new Why3SyntaxChecker(this, false);
		TranslationCondition test = TranslationCondition.TYPE.instantiate();
		test.setBody("hello");
		System.out.println(test.getBody().content());
		test.dispose();
	}
	
	@Override
	public void dispose() {
		USM.removeChangeListener(this);
		JFaceResources.getFontRegistry().removeListener(this);
		super.dispose();
	}
	
	@Override
	public void createControl(Composite parent) {
		setupPhase = true;
		font = JFaceResources.getFont(PreferenceConstants.RODIN_MATH_FONT);
		
		control = new Composite(parent, SWT.NO_BACKGROUND);
		control.setLayout(new GridLayout(1, false));
		
//		Composite main = new Composite(control, SWT.NULL);
//		main.setLayout(new GridLayout(1, false));
		
		
		JFaceResources.getFontRegistry().addListener(this);
		
		text = new StyledText(control,  SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_BACKGROUND);
		text.setFont(font);
		text.setEditable(true);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.setAlwaysShowScrollBars(false);
		
		tooltip = new ToolTip(text.getShell(), SWT.ICON_ERROR);
		
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
			
				syntaxChecker.check();
			}
		});		
		
		// handle error tooltips
		text.addListener(SWT.MouseMove, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (text.isDisposed())
					return;

				tooltip.setVisible(false);
				
				try {
					int offset = text.getOffsetAtLocation(new Point(event.x, event.y));
					StyleRange style = text.getStyleRangeAtOffset(offset);
					if (style != null) {
						if (style.data instanceof String) {
							tooltip.setText((String) style.data);
							tooltip.setLocation(event.x, event.y);
							tooltip.setAutoHide(true);
							tooltip.setVisible(true);
						}
					}
				} catch (IllegalArgumentException e) {
					//e.printStackTrace();
					// no character under event.x, event.y
				}
			}
		});			
		
		
		fillToolbar(control);
		
		reactOnChange();
		contributeToActionBars();
		control.pack();
		USM.addChangeListener(this);
		setupPhase = false;
	}

	private void fillToolbar(Composite parent) {
		tools = new Composite(parent, SWT.NO_BACKGROUND);
		tools.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		tools.setLayout(new GridLayout(2, false));
		
		
		statusLabel = new Label(tools, SWT.NO_BACKGROUND);
		statusLabel.setImage(IconFactory.proof_unknown);
		statusLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		
		ToolBar toolbar = new ToolBar(tools, SWT.FLAT | SWT.WRAP | SWT.RIGHT);	
		toolbar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
		
		check = new ToolItem(toolbar, SWT.FLAT | SWT.PUSH);
		check.setText("check");
		check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ProverRun op = new ProverRun(null);
					new ProgressMonitorDialog(control.getShell()).run(true, true, op);
					Why3CallResult result = op.getResult();
					// condition.setStatus(result.getStatus().getCode());
					// setStatus();
				} catch (InvocationTargetException | InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		parent.pack();
	}	
	
	private boolean reactOnChange() {
		final IProofState ps = userSupport.getCurrentPO();
		if (ps != null) {
			return reactOnChange(ps.getCurrentNode());
		} else {
			return reactOnChange(null);
		}
	}

	private boolean reactOnChange(final IProofTreeNode node) {
		if (node == null) {
			text.append("No current goal");
			tools.setVisible(false);
			text.setEditable(false);
			return false;
		} else if (!node.isOpen()) {
			text.append("Current goal is closed");
			text.setEditable(false);
			tools.setVisible(false);
			return false;
		}
	
		text.setText(makeLemmaText(node));
		text.setEditable(true);
		tools.setVisible(true);
		return true;
	}
	
	private String makeLemmaText(final IProofTreeNode node) {
		final Predicate goal = node.getSequent().goal();
		final ISealedTypeEnvironment typenv = node.getSequent().typeEnvironment();
		try {
			return generateTemplate(goal, typenv);
		} catch (CoreException | TranslationException e) {
			return e.toString();
		}
	}
	
	private String generateTemplate(Predicate predicate, ISealedTypeEnvironment types) throws CoreException, TranslationException {
		Map<FreeIdentifier, String> fiMap = new HashMap<FreeIdentifier, String>();
		Map<GivenType, String> gsMap = new HashMap<GivenType, String>();
		NameGenerator nameGen = new NameGenerator();
		
		for(FreeIdentifier fi: predicate.getFreeIdentifiers()) {
			Type type = fi.getType();
			if (fi.isATypeExpression()) {
				if (type.getBaseType() instanceof GivenType) {
					gsMap.put((GivenType) type.getBaseType(), nameGen.getTypeName()); 
				}
			}
			
			if (type instanceof PowerSetType) {
				PowerSetType ps = (PowerSetType) type;
				if (ps.getBaseType() instanceof ProductType) {
					fiMap.put(fi, nameGen.getRelName());
				} else {
					fiMap.put(fi, nameGen.getSetName());
				}
			} else {
				fiMap.put(fi, nameGen.getName());
			}
		}

		BTheorem btheorem = new BTheorem(predicate);
		TheoremTranslator tran = new TheoremTranslator(btheorem, false);
		TranslationTypesContext typecontext = tran.getTypesContext();
		
		for(FreeIdentifier fi: fiMap.keySet())
			tran.getNameMapper().injectNameMapping(fi.getName(), fiMap.get(fi));

		for(GivenType gs: gsMap.keySet()) {
			tran.getNameMapper().injectTypeMapping(gs.getName(), gsMap.get(gs));
			typecontext.insertGivenType(gsMap.get(gs));
		}
		
		TheoremTranslated translated = tran.translate();
		return translated.toString();
	}

	class NameGenerator {
		private char typeIndex = 'a';
		private int typeIndex2 = 1;
		private int setIndex = 1;
		private int relIndex = 1;
		private int simpleIndex = 1;
		
		public NameGenerator() {
			
		}
		
		public String getTypeName() {
			if (typeIndex == 'z' + 1) {
				return "'tt" + typeIndex2++;
			} else {
				return "'" + typeIndex++;
			}
		}
		
		public String getSetName() {
			return "s" + setIndex++; 
		}

		public String getRelName() {
			return "r" + relIndex++; 
		}

		public String getName() {
			return "x" + simpleIndex++; 
		}
	}
	
	/**
	 * Setup the action bars
	 */
	private void contributeToActionBars() {
		final IPageSite site = getSite();
		final IActionBars bars = site.getActionBars();

		// Setup global actions
//		UIUtils.addGlobalActionHandler(site, bars, ActionFactory.NEXT);
//		UIUtils.addGlobalActionHandler(site, bars, ActionFactory.PREVIOUS);

		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());

		// Commit changes
		bars.updateActionBars();
	}

	/**
	 * Fill the local pull down.
	 * 
	 * @param menuManager
	 *            the menu manager
	 */
	private void fillLocalPullDown(IMenuManager menuManager) {
		menuManager.add(new Separator());
	}

	/**
	 * Fill the context menu.
	 * <p>
	 * 
	 * @param menuManager
	 *            the menu manager
	 */
	void fillContextMenu(IMenuManager menuManager) {
		// Other plug-ins can contribute there actions here
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	/**
	 * Fill the local toolbar.
	 * 
	 * @param barManager
	 *            the toolbar manager
	 */
	private void fillLocalToolBar(IToolBarManager barManager) {
		// Do nothing
	}

	@Override
	public Control getControl() {
		return control;
	}

	@Override
	public void setFocus() {
		control.setFocus();
	}

	@Override
	public void userSupportManagerChanged(IUserSupportManagerDelta delta) {

		// Do nothing if the page is disposed.
		if (control.isDisposed())
			return;

		// Trying to get the changes for the current user support.
		final IUserSupportDelta affectedUserSupport = getUserSupportDelta(delta, userSupport);

		// Do nothing if there is no change for this current user support.
		if (affectedUserSupport == null)
			return;

		// If the user support has been removed, do nothing. This will be handle
		// by the main proof editor.
		final int kind = affectedUserSupport.getKind();
		if (kind == IUserSupportDelta.REMOVED) {
			return; // Do nothing
		}


		Display display = control.getDisplay();
		final Control c = control;
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (c.isDisposed())
					return;
				
				// Handle the case where the user support has changed.
				if (kind == IUserSupportDelta.CHANGED) {
					int flags = affectedUserSupport.getFlags();

					if ((flags & IUserSupportDelta.F_CURRENT) != 0) {
						// The current proof state is changed, reupdate the page
						IProofState ps = userSupport.getCurrentPO();
						if (ps != null) {
							reactOnChange(ps.getCurrentNode());
							return;
						} 
						reactOnChange(null);
						return;
					} 
					
					if ((flags & IUserSupportDelta.F_STATE) != 0) {
						// If the changes occurs in some proof states.	
						IProofState proofState = userSupport.getCurrentPO();
						// Trying to get the change for the current proof state. 
						final IProofStateDelta affectedProofState = getProofStateDelta(
								affectedUserSupport, proofState);
						if (affectedProofState != null) {
							// If there are some changes
							int psKind = affectedProofState.getKind();
							
							if (psKind == IProofStateDelta.CHANGED) {
								// If there are some changes to the proof state.
								int psFlags = affectedProofState.getFlags();

								if ((psFlags & IProofStateDelta.F_NODE) != 0
										|| (psFlags & IProofStateDelta.F_PROOFTREE) != 0) {
									reactOnChange(proofState.getCurrentNode());
								}
							}
						}
					}
				}
			}
		});
	}
//	
//	void setInformation(final IUserSupportInformation[] information) {
//		if (statusManager == null) {
//			statusManager = new ProofStatusLineManager(this.getSite()
//					.getActionBars());
//		}
//		statusManager.setProofInformation(information);
//	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (text == null || text.isDisposed()) {
			return;
		}
		if (event.getProperty().equals(PreferenceConstants.RODIN_MATH_FONT)) {
			font = JFaceResources
					.getFont(PreferenceConstants.RODIN_MATH_FONT);			
			text.setFont(font);
			reactOnChange();
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
	}	

	@Override
	public void reportStart() {
	}

	@Override
	public void reportEnd() {
	}

	@Override
	public ITranslationElement getSourceElement() {
		// TODO Auto-generated method stub
		return null;
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
				IProverDriver driver;
				if (Why3Plugin.isCloud()) {
					driver = new Why3CloudDriver();
				} else {
					driver = new Why3LocalDriver();
				}
				
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
