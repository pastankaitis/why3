package uk.ac.ncl.eventb.why3.gen.translation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import uk.ac.ncl.eventb.why3.gen.GenException;
import uk.ac.ncl.eventb.why3.gen.GenPlugin;
import uk.ac.ncl.eventb.why3.gen.ui.ConditionKind;
import uk.ac.ncl.eventb.why3.gen.ui.LinkKind;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationCatalog;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationCondition;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationLink;
import uk.ac.ncl.eventb.why3.gen.ui.TranslationRule;
import uk.ac.ncl.eventb.why3.translator.TNameMapper;
import uk.ac.ncl.eventb.why3.translator.TranslationException;

public class CatalogTranslator {
	private StringBuilder buffer;
	private TNameMapper nameMapper;
	
	public CatalogTranslator() {
		buffer = new StringBuilder();
		nameMapper = new TNameMapper(null, false, false);
	}

	public void reset() {
		buffer.setLength(0);
		nameMapper.reset();
	}

	public String getResult() {
		return buffer.toString();
	}
	
	public void translate(TranslationCatalog catalog) throws TranslationException, GenException {
		emitLinkedTheories(catalog);

		for(TranslationRule rule: catalog.getRules()) {
			translate(rule);
			buffer.append("\n");
		}
		
		buffer.append("theory Main\n");
		emitLibraryImports(catalog);
		emitRuleImports(catalog);
		for(TranslationCondition condition: catalog.getConditions()) {
			translate(condition);
		}
		buffer.append("end\n");
	}	

	private void emitLinkedTheories(TranslationCatalog catalog) throws TranslationException, GenException {
		for(TranslationLink link: catalog.getImports()) {
			if (link.getKind().content() == LinkKind.LINK) {
				org.eclipse.sapphire.modeling.Path z = link.getPath().content();
				IWorkspace workspace= ResourcesPlugin.getWorkspace();
				IPath location= Path.fromOSString(z.toFile().getAbsolutePath()); 
				IFile file = workspace.getRoot().getFileForLocation(location);
				TranslationCatalog other = GenPlugin.getDefault().getTGenRegistry().resolve(file);
				translate(other);
			}
		}
	}

	private void emitRuleImports(TranslationCatalog catalog) throws TranslationException {
		for(TranslationRule rule: catalog.getRules()) {
			buffer.append("\tuse export " + nameMapper.mapType(rule.getSource().content()));
			buffer.append("\n");
		}
	}
	
	
	private void emitLibraryImports(TranslationCatalog catalog) throws TranslationException {
		for(TranslationLink link: catalog.getImports()) {
			if (link.getKind().content() == LinkKind.LINK) {
				org.eclipse.sapphire.modeling.Path z = link.getPath().content();
				IWorkspace workspace= ResourcesPlugin.getWorkspace();
				IPath location= Path.fromOSString(z.toFile().getAbsolutePath()); 
				IFile file = workspace.getRoot().getFileForLocation(location);
				String theoryName = nameMapper.mapType(file.getName());
				buffer.append("\tuse export " + theoryName);
				buffer.append("\n");
			} else if (link.getKind().content() == LinkKind.LIBRARY) {
				buffer.append("\tuse export " + link.getLibrary().content());
				buffer.append("\n");
			}
		}
		
	}
	
	public void translate(TranslationRule rule) throws TranslationException, GenException {
		buffer.append("\t");
		buffer.append("(*");
		buffer.append("Theory derived from rule " + rule.getSource().content());
		buffer.append("*)\n");
		buffer.append("\t");
		buffer.append("theory ");
		buffer.append(nameMapper.mapType(rule.getSource().content()));
		buffer.append("\n");
		TranslationCatalog catalog = (TranslationCatalog) rule.parent().element();
		emitLibraryImports(catalog);
		buffer.append(TGenUtils.ruleString(rule));
		buffer.append("\n\n");
		for(TranslationCondition tc: rule.getConditions()) {
			translate(tc);
		}
		buffer.append("end\n");
	}
	
	public void translate(TranslationCondition condition) throws TranslationException, GenException {
		if (condition.getKind().content() != ConditionKind.TEST)
			buffer.append(TGenUtils.insertCondition(condition));
		
	}	
}
