package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

public interface Trigger extends Element {
	ElementType TYPE = new ElementType(Trigger.class);
	
	@XmlBinding(path = "spec")
	@Required
	@Service( impl = EventSymbolTranslator.class)
	ValueProperty PROP_SPEC = new ValueProperty(TYPE, "Spec");
	Value<String> getSpec();
	void setSpec(String type);
}
