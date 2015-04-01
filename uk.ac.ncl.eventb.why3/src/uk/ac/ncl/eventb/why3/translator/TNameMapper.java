package uk.ac.ncl.eventb.why3.translator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TNameMapper implements INameMapper {
	
	
	private boolean obfuscate = true;
	private boolean prefix = true;	
	private Map<String, Identifier> map;  // free identifiers
	private Map<String, Identifier> bmap; // bound identifiers
	private Map<String, Identifier> tmap;  // types
	private static final String IDENTIFIER_PREFIX = "id_";
	private static final String BOUND_IDENTIFIER_PREFIX = "b_";
	private static final String TYPELITERAL_PREFIX = "T_";
	private static final String TYPE_PREFIX = "tp_";
	private static final int OBF_IDENTIFIER_LENGTH = 10;
	private Random random;
	private TNameMapper parent;
	private boolean noNewTypes = false;
	
	public TNameMapper(TNameMapper parent, boolean obfuscate, boolean prefix) {
		this.parent = parent;
		this.obfuscate = obfuscate;
		this.prefix = prefix;
		map = new HashMap<String, Identifier>();
		bmap = new HashMap<String, Identifier>();
		tmap = new HashMap<String, Identifier>();
		random = new Random(System.currentTimeMillis());
	} 
	
	public boolean isNoNewTypes() {
		return noNewTypes;
	}

	public void setNoNewTypes(boolean noNewTypes) {
		this.noNewTypes = noNewTypes;
	}

	private Identifier getMapping(String identifier) {
		Identifier result = map.get(identifier);
		if (result == null && parent != null)
			return parent.getMapping(identifier);
		else
			return result; 
	}
	
	private Identifier getBoundMapping(String identifier) {
		Identifier result = bmap.get(identifier);
		if (result == null && parent != null)
			return parent.getBoundMapping(identifier);
		else
			return result; 
	}	

	private Identifier getTypeMapping(String identifier) {
		Identifier result = tmap.get(identifier);
		if (result == null && parent != null)
			return parent.getTypeMapping(identifier);
		else
			return result; 
	}	
	
	public String map(String identifier) {
		Identifier ii = getMapping(identifier);
		if (ii != null)
			return ii.name;
		else
			return null;
	}
	
	@Override
	public String mapFreeIdentifier(String identifier) {
		if (isKnown(identifier)) {
			Identifier ii = getMapping(identifier);
			assert (ii.getKind() == KIND.FREE);
			return ii.getName();
		} else {
			String newName = generateIdentifierName(identifier);
			map.put(identifier, new Identifier(newName, KIND.FREE));
			return newName;
		}
	}
	
	@Override
	public String mapBoundIdentifier(String identifier) {
		if (isKnownBoundName(identifier)) {
			Identifier ii = getBoundMapping(identifier);
			assert (ii.getKind() == KIND.BOUND);
			return ii.getName();
		} else {
			String newName = generateBoundIdentifierName(identifier);
			bmap.put(identifier, new Identifier(newName, KIND.BOUND));
			return newName;
		}
	}	

	@Override
	public String mapType(String identifier) throws TranslationException {
		if (isKnownType(identifier)) {
			Identifier ii = getTypeMapping(identifier);
			assert (ii.getKind() == KIND.TYPE);
			return ii.getName();
		} else {
			if (isNoNewTypes())
				throw new TranslationException("Unknown type " + identifier);
			String newName = generateType(identifier);
			tmap.put(identifier, new Identifier(newName, KIND.TYPE));
			return newName;
		}
	}	
	
	@Override
	public String mapTypeLiteral(String identifier) {
		if (isKnown(identifier)) {
			Identifier ii = getMapping(identifier);
			assert (ii.getKind() == KIND.TYPE_LITERAL);
			return ii.getName();
		} else {
			String newName = generateTypeLiteral(identifier);
			map.put(identifier, new Identifier(newName, KIND.TYPE_LITERAL));
			return newName;
		}
	}		
	
	@Override
	public boolean isKnown(String name) {
		return map.containsKey(name) || (parent != null && parent.isKnown(name));
	}
	
	public boolean isKnownBoundName(String name) {
		return bmap.containsKey(name) || (parent != null && parent.isKnownBoundName(name));
	}	

	public boolean isKnownType(String name) {
		return tmap.containsKey(name) || (parent != null && parent.isKnownType(name));
	}	
	
	
	@Override
	public KIND getKind(String name) {
		Identifier ii = getMapping(name);
		if (ii == null)
			return KIND.FREE;
		else
			return ii.getKind();
	}	
	
	private String _generate(String base) {
		String candidate = base;
		int c = 1;
		while (map(candidate) != null) {
			candidate = base + "__" + c;
		}
		
		return candidate;
	}
	
	private String generateTypeLiteral(String identifier) {
		return _generate(_generateTypeLiteral(identifier));
	}
	
	private String _generateTypeLiteral(String identifier) {
		if (obfuscate) {
			StringBuffer sb = new StringBuffer();
			sb.append(Character.toChars(random.nextInt(90 - 65) + 65)[0]);
			fill(sb, OBF_IDENTIFIER_LENGTH - 1);
			return sb.toString();
		} else {
			String candidate = prefix ? (TYPELITERAL_PREFIX + sanitize(identifier)) : sanitize(identifier);
			return candidate;
		}
	}	
	
	private String generateType(String identifier) {
		return _generate(_generateType(identifier));
	}	
	
	private String _generateType(String identifier) {
		if (obfuscate) {
			StringBuffer sb = new StringBuffer();
			sb.append(Character.toChars(random.nextInt(122 - 97) + 97)[0]);
			fill(sb, OBF_IDENTIFIER_LENGTH - 1);
			return sb.toString();
		} else {
			String candidate = prefix ? (TYPE_PREFIX + sanitize(identifier)) : sanitize(identifier);
			return candidate;
		}
	}		
	
	private String generateIdentifierName(String identifier) {
		return _generate(_generateIdentifierName(identifier));
	}	
	
	private String _generateIdentifierName(String identifier) {
		if (obfuscate) {
			StringBuffer sb = new StringBuffer();
			sb.append(Character.toChars(random.nextInt(122 - 97) + 97)[0]);
			fill(sb, OBF_IDENTIFIER_LENGTH - 1);
			return sb.toString();
		} else {
			String candidate = prefix ? (IDENTIFIER_PREFIX + sanitize(identifier)) : sanitize(identifier);
			return candidate;
		}
	}
	
	private String generateBoundIdentifierName(String identifier) {
		return _generate(_generateBoundIdentifierName(identifier));
	}	
	
	private String _generateBoundIdentifierName(String identifier) {
		if (obfuscate) {
			StringBuffer sb = new StringBuffer();
			sb.append(Character.toChars(random.nextInt(122 - 97) + 97)[0]);
			fill(sb, OBF_IDENTIFIER_LENGTH - 1);
			return sb.toString();
		} else {
			String candidate = prefix ? (BOUND_IDENTIFIER_PREFIX + sanitize(identifier)) : sanitize(identifier);
			return candidate;
		}
	}	
	
	private String sanitize(String text) {
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (!Character.isLetterOrDigit(c) && c != '_') {
				sb.append('_');
			} else {
				sb.append(c);
			}
		}
		
		return sb.toString();
	}

	private void fill(StringBuffer sb, int length) {
		for(int i = 0; i < length; i++) {
			switch(random.nextInt(3)) {
			case 0:
				sb.append(Character.toChars(random.nextInt(57 - 48) + 48)[0]);
				break;
			case 1:
				sb.append(Character.toChars(random.nextInt(90 - 65) + 65)[0]);
				break;
			case 2:
				sb.append(Character.toChars(random.nextInt(57 - 48) + 48)[0]);
				break;
			}
		}		
	}
	
	class Identifier {
		private String name;
		private KIND kind;
		public Identifier(String name, KIND kind) {
			super();
			this.name = name;
			this.kind = kind;
		}
		public String getName() {
			return name;
		}
		public KIND getKind() {
			return kind;
		}
	}

	public void reset() {
		map.clear();
		noNewTypes = false;
	}
	
	@Override
	public String mapName(String name) {
		return map(name);
	}
	
	@Override
	public String mapLocallyBoundName(String name) {
		return mapBoundIdentifier(name);
	}

	@Override
	public void removeLocallyBoundName(String name) {
		map.remove(name);
	}

	@Override
	public void injectNameMapping(String from, String to) throws TranslationException {
//		if (!isKnown(from)) {
			map.put(from, new Identifier(to, KIND.FREE));
//		} else {
//			throw new TranslationException("Duplicate mapping");
//		}
		
	}

	@Override
	public void injectTypeMapping(String from, String to) throws TranslationException {
//		if (!isKnownType(from)) {
			tmap.put(from, new Identifier(to, KIND.TYPE));
//		} else {
//			throw new TranslationException("Duplicate mapping");
//		}
		
	}
	
}