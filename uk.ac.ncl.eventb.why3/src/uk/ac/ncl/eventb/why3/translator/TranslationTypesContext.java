package uk.ac.ncl.eventb.why3.translator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eventb.core.IContextRoot;
import org.eventb.core.IMachineRoot;
import org.eventb.core.ISCAxiom;
import org.eventb.core.ISCCarrierSet;
import org.eventb.core.ISCContextRoot;
import org.eventb.core.ISCInternalContext;
import org.eventb.core.ISCMachineRoot;
import org.eventb.core.ast.Expression;
import org.eventb.core.ast.Formula;
import org.eventb.core.ast.FreeIdentifier;
import org.eventb.core.ast.ITypeEnvironment;
import org.eventb.core.ast.MultiplePredicate;
import org.eventb.core.ast.Predicate;
import org.eventb.core.ast.SetExtension;
import org.rodinp.core.IInternalElement;

import uk.ac.ncl.eventb.why3.main.Why3Plugin;

public class TranslationTypesContext {
	private static final TranslationTypesContext EMPTY_OBFS = new TranslationTypesContext(true);
	private static final TranslationTypesContext EMPTY_NON_OBFS = new TranslationTypesContext(false);
	
	private static Set<CachedResource> cache1;	// obfuscated
	private static Set<CachedResource> cache2;  // non-obfuscated
	
	private Collection<String> givenTypes;
	private Collection<Predicate> excluded;
	private Map<String, EnumeratedSet> enums;
	private INameMapper nameMapper;
	
	
	static {
		cache1 = new HashSet<CachedResource>();
		cache2 = new HashSet<CachedResource>();
	}

	private TranslationTypesContext(boolean obfuscate) {
		nameMapper = new TNameMapper(null, obfuscate, true);
		enums = new HashMap<String, EnumeratedSet>();
		givenTypes = new HashSet<String>();
		excluded = new HashSet<Predicate>();
	}
	
	private TranslationTypesContext(IInternalElement sourceRoot, boolean obfuscate) throws CoreException, TranslationException {
		nameMapper = new TNameMapper(null, obfuscate, true);
		enums = new HashMap<String, EnumeratedSet>();
		givenTypes = new HashSet<String>();
		excluded = new HashSet<Predicate>();
		buildEnumeratedSets(sourceRoot);
	}

	public static synchronized TranslationTypesContext getContext(IInternalElement sourceRoot, boolean obfuscate) throws CoreException, TranslationException {
		if (sourceRoot == null) {
			if (obfuscate) {
				return EMPTY_OBFS;
			} else {
				return EMPTY_NON_OBFS;
			}

		} else {
			if (obfuscate) {
				return _getContext(sourceRoot, cache1, obfuscate);
			} else {
				return _getContext(sourceRoot, cache2, obfuscate);
			}
		}
	}
	
	public boolean isExcluded(Predicate predicate) {
		return excluded.contains(predicate);
	}

	private static TranslationTypesContext _getContext(IInternalElement sourceRoot, Set<CachedResource> cache, boolean obfuscate) throws CoreException, TranslationException {
		CachedResource toRemove = null;
		CachedResource toAdd = null;
		for (CachedResource cr : cache) {
			if (cr.matches(sourceRoot)) {
				if (cr.isValid(sourceRoot)) {
					if (Why3Plugin.DEBUG)
						System.out.println("<<why3>>: using cached context for resource " + sourceRoot.toString());
					return cr.getCache();
				} else {
					if (Why3Plugin.DEBUG)
						System.out.println("<<why3>>: invalidating cached context for resource " + sourceRoot.toString());
					TranslationTypesContext translationContext = new TranslationTypesContext(sourceRoot, obfuscate);
					CachedResource ncr = new CachedResource(sourceRoot, translationContext);
					toRemove = cr;
					toAdd = ncr;
					break;
				}
			}
		}

		if (toRemove != null) {
			cache.remove(toRemove);
			cache.add(toAdd);
			return toAdd.getCache();
		} else {
			if (Why3Plugin.DEBUG)
				System.out.println("<<why3>>: creating new cached context for resource " + sourceRoot.toString());
			TranslationTypesContext translationContext = new TranslationTypesContext(sourceRoot, obfuscate);
			CachedResource ncr = new CachedResource(sourceRoot, translationContext);
			cache.add(ncr);
			return translationContext;
		}
		
	}
	
	public void insertGivenType(String typeName) {
		givenTypes.add(typeName);
	}
	
	public Collection<String> getGivenTypes() {
		return givenTypes;
	}
	
	public Map<String, EnumeratedSet> getEnumeratedSets() {
		return enums;
	}
	
	public INameMapper getNameMapper() {
		return nameMapper;
	}
/*
	SourceLocation source = theorem.getGoal().getSourceLocation();
	if (source != null && source.getOrigin() instanceof EventBElement) {
		EventBElement element = (EventBElement) source.getOrigin();
*/
	
	private void buildEnumeratedSets(IInternalElement element) throws CoreException, TranslationException {
		if (element.getRoot() instanceof IContextRoot) {
			IContextRoot context = (IContextRoot) element.getRoot();
			ISCContextRoot sc_context = context.getSCContextRoot();
			ITypeEnvironment typenv = sc_context.getTypeEnvironment();
			// ISCContextRoot context_root =
			// sc_context.getSCExtendsClauses()[0].getAbstractSCContext();
			for (ISCAxiom axiom : sc_context.getSCAxioms()) {
				for (ISCCarrierSet set : sc_context.getSCCarrierSets()) {
					String translated = nameMapper.mapType(set.getIdentifierString());
					givenTypes.add(translated);
				}
				processPotentialPartioningAxiom(typenv, axiom);
			}

		} else {
			IMachineRoot machine = (IMachineRoot) element.getRoot();
			ISCMachineRoot sc_machine = machine.getSCMachineRoot();
			ITypeEnvironment typenv = sc_machine.getTypeEnvironment();
			for (ISCInternalContext zz : sc_machine.getSCSeenContexts()) {
				if (Why3Plugin.DEBUG)
					System.out.println("<<why3>>: processing context " +zz.getComponentName());
				for (ISCCarrierSet set : zz.getSCCarrierSets()) {
					String translated = nameMapper.mapType(set.getIdentifierString());
					givenTypes.add(translated);
				}
				for (ISCAxiom axiom : zz.getSCAxioms()) {
					processPotentialPartioningAxiom(typenv, axiom);
				}
			}
		}

	}
	
	private void processPotentialPartioningAxiom(ITypeEnvironment typenv, ISCAxiom _axiom) throws CoreException, TranslationException {
		
		Predicate axiom = _axiom.getPredicate(typenv);
		if (axiom.getTag() == Formula.KPARTITION) {
			MultiplePredicate mp = (MultiplePredicate) axiom;
			List<String> enum_literals = new ArrayList<String>(mp.getChildCount() - 1); 
			if (mp.getChildCount() > 1 && mp.getChildren()[0] instanceof FreeIdentifier) {
				FreeIdentifier sfi = (FreeIdentifier) mp.getChildren()[0];
				if (sfi.isATypeExpression()) { // onlyu try if identifier denotes a given set
					for(int i = 1; i < mp.getChildCount(); i++) {
						Expression z = mp.getChildren()[i];
						if (z.getTag() != Formula.SETEXT)
							return;
						
						SetExtension se = (SetExtension) z;
						if (se.getChildCount() != 1)
							return;
						
						if (!(se.getChild(0) instanceof FreeIdentifier))
							return;
						
						FreeIdentifier elit = (FreeIdentifier) se.getChild(0);
						
						String original = elit.getName();
						String mapped = nameMapper.mapTypeLiteral(original);
						
						enum_literals.add(mapped);
					}

					String original = sfi.getName();
					String mapped = nameMapper.mapType(original);
					
					EnumeratedSet enumset = new EnumeratedSet(mapped, enum_literals);
					enums.put(mapped, enumset);
					
					excluded.add(axiom);
					
					// System.out.println("Added enum " + enumset);
				}
			}
		}		
	}	
	
}

class CachedResource {
	private IInternalElement resource;
	private long modificationTime; 
	private long expirationTime; 
	private TranslationTypesContext cache;
	
	public CachedResource(IInternalElement resource, TranslationTypesContext cache) {
		this.resource = resource;
		this.cache = cache;
		IFile file = resource.getResource();
		if (file != null && file.exists()) {
			modificationTime = file.getModificationStamp();
			expirationTime = -1;
		} else {
			modificationTime = -1;
			expirationTime = System.currentTimeMillis() + 5000; // valid for next 5 seconds
		}
	}
	
	public TranslationTypesContext getCache() {
		return cache;
	}

	public boolean matches(IInternalElement resource) {
		return this.resource.equals(resource);
	}
	
	public boolean isValid(IInternalElement resource) {
		if (matches(resource)) {
				if (modificationTime != -1) {
					IFile file = resource.getResource();
					return modificationTime == file.getModificationStamp();
				} else {
					return expirationTime <= System.currentTimeMillis();
				}
		} else {
			return false;
		}
		
		
	}
}
