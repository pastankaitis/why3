package uk.ac.ncl.eventb.why3.translator;


public interface INameMapper {
	public enum KIND {FREE, BOUND, TYPE, TYPE_LITERAL};	
	
	public String mapLocallyBoundName(String name); 
	public void removeLocallyBoundName(String name); 
	public String mapName(String name); 
	public String mapFreeIdentifier(String identifier);
	public String mapBoundIdentifier(String identifier);
	public String mapType(String identifier) throws TranslationException;	
	public String mapTypeLiteral(String identifier);
	
	/**
	 * Inserts a mapping between some source name and a target name. The mapping may not override an existing mapping
	 * @param from a source name
	 * @param to a target name
	 * @throws TranslationException if a mapping from source name is already defined
	 */
	public void injectNameMapping(String from, String to) throws TranslationException;

	/**
	 * Inserts a mapping between some source type name and a target type name. The mapping may not override an existing mapping
	 * @param from a source name
	 * @param to a target name
	 * @throws TranslationException if a mapping from source name is already defined
	 */
	public void injectTypeMapping(String from, String to) throws TranslationException;
	
	
	public boolean isKnown(String name);
	public KIND getKind(String name);
}
