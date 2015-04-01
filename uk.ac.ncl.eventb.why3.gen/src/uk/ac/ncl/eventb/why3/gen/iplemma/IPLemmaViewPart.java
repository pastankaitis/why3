package uk.ac.ncl.eventb.why3.gen.iplemma;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eventb.internal.ui.prover.ProverContentOutline;
import org.eventb.internal.ui.prover.ProverUI;

import uk.ac.ncl.eventb.why3.gen.GenPlugin;
import uk.ac.ncl.eventb.why3.main.Why3Plugin;

public class IPLemmaViewPart extends ProverContentOutline {
	public static final String VIEW_ID = GenPlugin.PLUGIN_ID + ".iplemma.view";

	public IPLemmaViewPart() {
		super("Schematic Lemma is not applicable in this context");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.PageBookView#doCreatePage(org.eclipse.ui.IWorkbenchPart)
	 */
	//@Override
	@Override
	protected PageRec 
	doCreatePage(IWorkbenchPart part) {
		ProverUI proverui = (ProverUI) part;
		ILemmaPage page = new SchematicLemmaPage(proverui, proverui.getUserSupport());
		initPage(page);
		page.createControl(getPageBook());
		return new PageRec(part, page);
	}

}
