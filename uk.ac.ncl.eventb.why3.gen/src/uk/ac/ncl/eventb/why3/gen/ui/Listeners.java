package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.FilteredListener;
import org.eclipse.sapphire.ValuePropertyContentEvent;
import org.rodinp.keyboard.core.RodinKeyboardCore;

public class Listeners {
	class EventBTypeListener extends FilteredListener<ValuePropertyContentEvent> {
		@Override
		protected void handleTypedEvent(final ValuePropertyContentEvent event) {
			String before = event.after();
			String after = RodinKeyboardCore.translate(before);
			if (!before.equals(after)) {
				PatternParameter parameter = (PatternParameter) event.property().element();
				parameter.setType(after);
			}
		}
	}
	
}
