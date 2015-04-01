package uk.ac.ncl.eventb.why3.prefpage.scen;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

import uk.ac.ncl.pparser.ErrInfo;
import uk.ac.ncl.pparser.syntree;

import com.why3.ws.scenario.ScenarioException;
import com.why3.ws.scenario.Scenarios;

public class SpecifyScenarioDialog extends Dialog implements IScenarioWarningCollector
{
	private static final StyleRange[] EMPTY_STYLE = new StyleRange[0];

	private Text txtName;
    private StyledText txtScen;
    private String title;
    private ScenarioRegistry _registry;
    private ToolTip tooltip;
    
    private String _name, _scen;
    private Color defBackground, defForeground;
    
    /**
     * Create the dialog.
     * @param parentShell
     */
    public SpecifyScenarioDialog(Shell parentShell, String title, ScenarioRegistry registry, String currentName)
    {
        super(parentShell);
        this.title = title;
        _registry = registry;
        _name = currentName;
        if (_name != null && _name.length() > 0)
            _scen = _registry.get(_name);
        else
            _scen = "";
        setShellStyle(SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
    }
    
    
    @Override
    protected void configureShell(Shell newShell)
    {
        super.configureShell(newShell);
        newShell.setText(title);
    }

    /**
     * Create contents of the dialog.
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = (GridLayout) container.getLayout();
        gridLayout.numColumns = 3;
        
        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("Name:");
        
        txtName = new Text(container, SWT.BORDER);
        txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        txtName.setText(_name);
        new Label(container, SWT.NONE);
        
        defBackground = txtName.getBackground();
        defForeground = txtName.getForeground();
        
		txtName.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event e) {
				try {
					txtName.setRedraw(false);
					validateName();
					txtName.setBackground(defBackground);
					txtName.setForeground(defForeground);
					txtName.setToolTipText(null);
				} catch (IllegalArgumentException e1) {
					txtName.setBackground(StyleColours.errorBackground);
					txtName.setForeground(StyleColours.errorForeground);
					txtName.setToolTipText(e1.getMessage());
				} finally {
					txtName.setRedraw(true);
				}
			}
		});        
        
        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblNewLabel_1.setText("Scenario:");
        
        txtScen = new StyledText(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
        txtScen.setLayoutData(new GridData(GridData.FILL_BOTH));
       // txtPath.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 3));
        txtScen.setText(_scen);
        
        tooltip = new ToolTip(txtScen.getShell(), SWT.ICON_ERROR);
        
        Font font = JFaceResources
				.getFont(org.rodinp.keyboard.ui.preferences.PreferenceConstants.RODIN_MATH_FONT);
		txtScen.setFont(font);
        
		txtScen.addListener(SWT.Modify, new Listener() {
			   public void handleEvent(Event e) {
				   parse();
			   }
			});		
		
		// handle error tooltips 
		txtScen.addListener(SWT.MouseMove, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// It is up to the application to determine when and how a link should be activated.
				// In this snippet links are activated on mouse down when the control key is held down 
				try {
					int offset = txtScen.getOffsetAtLocation(new Point (event.x, event.y));
					StyleRange style = txtScen.getStyleRangeAtOffset(offset);
					if (style != null) {
						if (style.data instanceof ErrInfo) {
							ErrInfo info = (ErrInfo) style.data;
							tooltip.setText(info.message);
							tooltip.setLocation(event.x, event.y);
							tooltip.setAutoHide(true);
							tooltip.setVisible(true);
						} else if (style.data instanceof String) {
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

		parse();
		
        return container;
    }
    
    private void validateName() {
    	String newName = txtName.getText().trim();
    	if (newName.length() < 2)
    		throw new IllegalArgumentException("Name is to short");

    	if (newName.length() > 50)
    		throw new IllegalArgumentException("Name is too long");
    	
    	if (!newName.equals(_name)) {
    		if (ScenarioRegistry.getInstance().get(newName) != null)
    			throw new IllegalArgumentException("Duplicate name");
    		
    		if (Scenarios.SCENARIOS.containsKey(newName))
    			throw new IllegalArgumentException("Duplicate name");
    	}
    }
    
    private void parse() {
		String string = txtScen.getText();
		if (string != null) {
			try {
				txtScen.setRedraw(false);
				InputStream is = new ByteArrayInputStream(string.getBytes());
				List<ErrInfo> info = new ArrayList<ErrInfo>(20);
				syntree result = uk.ac.ncl.pparser.Parser.parse(is, info);
				toggleOKButton(!info.isEmpty());
				txtScen.replaceStyleRanges(0, string.length(), EMPTY_STYLE);
				for (ErrInfo ei : info) {
					highlight(ei);
				}
				
				if (info.isEmpty()) {
					try {
						ScenarioBuilder.build(result, true, this);
					} catch (ScenarioException e) {
						highlightError(e.getMessageString(), e.getAst());	
						toggleOKButton(false);
					}
				}
				
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				txtScen.setRedraw(true);
			}
		}
	}
    
    private void toggleOKButton(boolean error) {
    	if (getButton(IDialogConstants.OK_ID) == null)
    		return;
    	
		if (error) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		} else {
			getButton(IDialogConstants.OK_ID).setEnabled(true);
		}
    }
    
    private void highlight(ErrInfo info) {
    	StyleRange styleRange = new StyleRange();
    	if (info.start < info.end) {
        	styleRange.start = info.start;
        	styleRange.length = info.end - info.start;
    	} else {
        	styleRange.start = 0;
        	styleRange.length = txtScen.getText().length();
    	}
    	styleRange.foreground = StyleColours.errorForeground;
    	styleRange.background = StyleColours.errorBackground;
    	styleRange.data = info;
    	txtScen.setStyleRange(styleRange);
    }
    
    private void highlightError(String error, syntree parsed) {
    	StyleRange styleRange = new StyleRange();
    	styleRange.start = parsed.getStart();
    	styleRange.length = parsed.getEnd() - parsed.getStart();
    	styleRange.foreground = StyleColours.errorForeground;
    	styleRange.background = StyleColours.errorBackground;
    	styleRange.data = error;
    	txtScen.setStyleRange(styleRange);
    }    

    private void highlightWarning(String warning, syntree parsed) {
    	StyleRange styleRange = new StyleRange();
    	styleRange.start = parsed.getStart();
    	styleRange.length = parsed.getEnd() - parsed.getStart();
    	styleRange.underline = true;  	
    	styleRange.underlineStyle = SWT.UNDERLINE_SQUIGGLE;
    	styleRange.underlineColor = StyleColours.warningSquiggle;  	
    	styleRange.data = warning;
    	txtScen.setStyleRange(styleRange);
    }    

	@Override
	public void warning(String warning, syntree parsed) {
		highlightWarning(warning, parsed);
	}    
    
    @Override
    public boolean close()
    {
        _name = txtName.getText();
        _scen = txtScen.getText();
        return super.close();
    }
    
    public String getName()
    {
        return _name;
    }
    
    public String getPath()
    {
        return _scen;
    }

    /**
     * Create contents of the button bar.
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize()
    {
        return new Point(500, 400);
    }


}
