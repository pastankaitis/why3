package uk.ac.ncl.eventb.why3.translator;

public interface IContextProvider {
	/**
	 * Inserts definition of theories on which the translation of some current condition depends on. These theories
	 * appear in the same output file and typically dynamically generated 
	 * @param sb string builder to write into
	 * @throws GenException 
	 * @throws TranslationException 
	 */
	public void addInplaceTheories(StringBuilder sb) throws TranslationException;	
	
	/**
	 * Inserts import clauses for theories on which the translation of some current condition depends on. These are statically defined
	 * theories shared among many conditions and placed in external files (/theories folder of why3) 
	 * @param sb
	 */
	public void addTheoryImports(StringBuilder sb) throws TranslationException;	
}
