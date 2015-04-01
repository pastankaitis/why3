package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.Unique;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

public interface PatternParameter extends Element {
	ElementType TYPE = new ElementType(PatternParameter.class);

	@XmlBinding(path = "id")
	@Required
	@Unique
	ValueProperty PROP_ID = new ValueProperty(TYPE, "Id");
	Value<String> getId();
	void setId(String value);	
	
	@XmlBinding(path = "type")
	@Required
	@Service( impl = EventSymbolTranslator.class)
	ValueProperty PROP_TYPE = new ValueProperty(TYPE, "Type");
	Value<String> getType();
	void setType(String type);
}
