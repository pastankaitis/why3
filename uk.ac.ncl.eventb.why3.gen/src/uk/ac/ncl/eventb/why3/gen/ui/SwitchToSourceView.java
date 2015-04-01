package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.ui.Presentation;
import org.eclipse.sapphire.ui.SapphireActionHandler;


public class SwitchToSourceView extends SapphireActionHandler {

	@Override
	protected Object run(Presentation context) {
		TranslationRule rule = (TranslationRule) context.part().parent().getModelElement();
		if (rule.getGUIMode().empty()) // assume "edit"
			rule.setGUIMode("source"); 
		else if (rule.getGUIMode().content().equals("edit")) 
			rule.setGUIMode("source"); 
		else if (rule.getGUIMode().content().equals("source")) 
			rule.setGUIMode("edit"); 
		return null;
	}

}
