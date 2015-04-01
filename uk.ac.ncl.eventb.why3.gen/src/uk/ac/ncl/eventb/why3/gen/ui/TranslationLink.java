package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.DefaultValue;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

public interface TranslationLink extends Element, ICommented {
	ElementType TYPE = new ElementType(TranslationLink.class);	

	@XmlBinding(path = "kind")
	@Type( base = LinkKind.class )
    @DefaultValue( text = "Link" )
	@Required
	ValueProperty PROP_KIND = new ValueProperty(TYPE, "Kind");
	Value<LinkKind> getKind();
	void setKind(LinkKind value);	
	
	@XmlBinding(path = "path")
	@Type( base = org.eclipse.sapphire.modeling.Path.class )
	@Required
	ValueProperty PROP_PATH = new ValueProperty(TYPE, "Path");
	Value<org.eclipse.sapphire.modeling.Path> getPath();
	void setPath(org.eclipse.sapphire.modeling.Path value);
	
	@XmlBinding(path = "library")
	@Service( impl = LibraryTheories.class)
	@Required
	ValueProperty PROP_LIBRARY = new ValueProperty(TYPE, "Library");
	Value<String> getLibrary();
	void setLibrary(String value);
}
