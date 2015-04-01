package uk.ac.ncl.eventb.why3.translator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eventb.core.ast.FreeIdentifier;
import org.eventb.core.ast.Predicate;
import org.eventb.core.ast.SourceLocation;
import org.eventb.core.basis.EventBElement;
import org.rodinp.core.IInternalElement;
import org.rodinp.core.basis.InternalElement;

import uk.ac.ncl.eventb.why3.main.BTheorem;
import uk.ac.ncl.eventb.why3.main.Why3Plugin;

public class TheoremTranslator implements ITypeReferenceKeeper {
	private StringBuilder goal;
	private StringBuilder hyp;
	private StringBuilder aux;
	private StringBuilder types;	
	private Translator translator;
	private INameMapper nameMapper;
	private TranslationTypesContext typesContext;
	private Collection<String> referencedTypes;
	private BTheorem theorem; 
	
	public TheoremTranslator(BTheorem theorem, boolean obfuscate) throws CoreException, TranslationException {
		goal = new StringBuilder();
		hyp = new StringBuilder();
		aux = new StringBuilder();
		types = new StringBuilder();
		this.theorem = theorem;
		IInternalElement element = getInternalElement(theorem);
		IInternalElement root = null;
		if (element != null)
			root = getInternalElement(theorem).getRoot();

		typesContext = TranslationTypesContext.getContext(root, obfuscate);
		nameMapper = new TNameMapper((TNameMapper) typesContext.getNameMapper(), obfuscate, true);
		translator = new Translator(aux, nameMapper, this);
		referencedTypes = new HashSet<String>();
	}
	
	public INameMapper getNameMapper() {
		return nameMapper;
	}

	public TranslationTypesContext getTypesContext() {
		return typesContext;
	}

	private void emitTypeDeclarations() throws TranslationException {
		for(String type: referencedTypes) {
			if (typesContext.getEnumeratedSets().containsKey(type)) {
				types.append("\ttype " + typesContext.getEnumeratedSets().get(type) + "\n");
			} else if (typesContext.getGivenTypes().contains(type)) {
				types.append("\ttype " + type + "\n");
			} else {
				System.err.println("<<why3>>: PANIC - undefined type " + type);
				throw new RuntimeException("<<why3>>: PANIC - undefined type " + type);
			}
		}
		
		int i = 1;
		for(FreeIdentifier fi: translator.getTypeIdentifiers()) {
			if (Why3Plugin.DEBUG) {
				hyp.append("\t(* this set identifier is a class (maximal set) *)\n");
			}
			hyp.append("\taxiom axm_class" + i++ + " : (maxset ");
			hyp.append(nameMapper.mapName(fi.getName()));
			hyp.append(")\n");
		}
		
	}
	
	public TheoremTranslated translate() throws CoreException, TranslationException {
		TheoremTranslated translated = new TheoremTranslated();

		goal.setLength(0);
		hyp.setLength(0);
		types.setLength(0);
		
		// emit hypothesis axioms
		int i = 1;
		for(Predicate p: theorem.getHypothesis()) {
			if (!typesContext.isExcluded(p)) {
				if (Why3Plugin.DEBUG) {
					hyp.append("\t(* ");
					hyp.append(p.toString());
					hyp.append(" *)\n");
				}
				
				try {
					translator.getMain().setLength(0);
					translator.translate(p);
					hyp.append("\taxiom axm" + i++ + " :");
					hyp.append(translator.getMain());
					hyp.append("\n");
				} catch (TranslationException e) {
					System.err.println("<<why3>>: failed to translate " + p + " : " + e.getMessage());
				}
			}
		}
		

		translator.getMain().setLength(0);
		
		if (theorem.getGoal() != null) {
			if (Why3Plugin.DEBUG) {
				goal.append("\t(* ");
				goal.append(theorem.getGoal().toString());
				goal.append(" *)\n");
			}
			try {
				translator.translate(theorem.getGoal());
				goal.append( translator.getMain() );
				translated.setGoal(goal.toString());
			} catch (TranslationException e) {
				System.err.println("<<why3>>: failed to translate " + theorem.getGoal() + " : " + e.getMessage());
				// unrecoverable
				return null;
			}
		} else {
			translated.setGoal("false");
		}
		
		
		try {
			translated.setDefinitions(translator.getIdentifierTranslation());
		} catch (TranslationException e) {
			// unrecoverable
			System.err.println("<<why3>>: translate error " + e.getMessage());
			return null;
		}

		emitTypeDeclarations();
		translated.setTypes(types.toString());
		translated.setHypothesis(hyp.toString());

		return translated;
	}
	
	private IInternalElement getInternalElement(BTheorem theorem) throws CoreException {
		//System.out.println("th origin: " + theorem.getOrigin());
		if (theorem.getOrigin() != null)
			return theorem.getOrigin();
		
		
		InternalElement goalie = getInternalElement(theorem.getGoal());
		if (goalie != null)
			return goalie;
		
		for(Predicate p: theorem.getHypothesis()) {
			InternalElement hypie = getInternalElement(p);
			if (hypie != null)
				return hypie;
		}

		return null;
	}

	private InternalElement getInternalElement(Predicate predicate) throws CoreException {
		if (predicate == null)
			return null;
		
		SourceLocation source = predicate.getSourceLocation();
		if (source != null && source.getOrigin() instanceof EventBElement) {
			return (EventBElement) source.getOrigin();
		} else {
			return null;
		}
	}	


	@Override
	public synchronized void referenceType(String typeName) {
		referencedTypes.add(typeName);
	}
		
}

/**
 * An enumerated with a translated name and elements
 * @author paulius
 *
 */
class EnumeratedSet {
	private String name;
	private Collection<String> elements;
	
	public EnumeratedSet(String name, Collection<String> elements) {
		super();
		this.name = name;
		this.elements = elements;
	}
	
	public String getName() {
		return name;
	}
	
	public Collection<String> getElements() {
		return elements;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(name);
		sb.append(" = ");
		
		Iterator<String> iter = elements.iterator();
		
		if (iter.hasNext()) {
			sb.append(iter.next());
		}
		
		while (iter.hasNext()) {
			sb.append(" | ");
			sb.append(iter.next());
		}
		
		return sb.toString();
	}
}
