package uk.ac.ncl.eventb.why3.translator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import static org.eventb.core.ast.QuantifiedUtil.catenateBoundIdentLists;
import org.eventb.core.ast.*;

/**
All tom files must be called Tran*.t !
*/

public class Translator {
  private String[] boundNames = new String[]{};

  private StringBuilder typeDeclarations;
  private StringBuilder aux;
  private StringBuilder sb;
  private TypeTranslator tt;
  private TypeTranslator tt_aux;
  private Collection<FreeIdentifier> identifiers;
  private Collection<FreeIdentifier> typeIdentifiers;

  private INameMapper nameMapper;	
  private ITypeReferenceKeeper treferenceKeeper;  

  %include {FormulaV2.tom}
	
  public Translator(StringBuilder aux, INameMapper nameMapper, ITypeReferenceKeeper treferenceKeeper) {
    this.typeDeclarations = new StringBuilder();
    this.treferenceKeeper = treferenceKeeper;
    this.aux = aux;
    this.nameMapper = nameMapper;
  	sb = new StringBuilder();
  	tt_aux = new TypeTranslator(typeDeclarations, aux, nameMapper, treferenceKeeper);
  	tt = new TypeTranslator(typeDeclarations, sb, nameMapper, treferenceKeeper);
  	
  	identifiers = new HashSet<FreeIdentifier>();
  	typeIdentifiers = new HashSet<FreeIdentifier>();
  }	
  
	private String[] addBound(String[] addedBoundNames) {
		if (addedBoundNames.length == 0) {
			return boundNames;
		}
		return catenateBoundIdentLists(boundNames, addedBoundNames);
	}
	

	private String[] addBound(BoundIdentDecl[] boundIdentDecls) {
		String[] bn = new String[boundIdentDecls.length];
		
		for(int i = 0; i < bn.length; i++) {
			bn[i] = boundIdentDecls[i].getName();
		}
		
		return addBound(bn);
	}	
	
	private static String resolveIndex(int index, String[] boundIdents) {
		if (index < boundIdents.length) {
			return boundIdents[boundIdents.length - index - 1];
		}
		return null;
	}	  

  public StringBuilder getMain() {
  	return sb;
  }

  public String getIdentifierTranslation() throws TranslationException {
  
    for(FreeIdentifier fi: identifiers) {
    	emitIdentifier(fi);
    }
  
    for(FreeIdentifier fi: typeIdentifiers) {
    	emitIdentifier(fi);
    }
      
  	return aux.toString();
  }

  public Collection<FreeIdentifier> getTypeIdentifiers() throws TranslationException {
  	return typeIdentifiers;
  }

  public void translate(Predicate predicate) throws TranslationException {
    %match (Predicate predicate) {
     
      BTRUE()			-> {prettyprint("true");}
      BFALSE()			-> {prettyprint("false");}
    
    /* Simple Predicate */
      Finite (X)		-> { prefixUnaryExpression("finite", `X);}
    
	/* Binary Predicate */
      Limp  (A, B) 		-> { infixBinaryPredicate("->", `A, `B); } 
      Leqv (A, B) 		-> { infixBinaryPredicate("<->", `A, `B);}
     
   /*Relational Predicate*/  
      Lt (A, B)			-> { infixBinaryExpression("<", `A, `B); }
      Le (A, B)			-> { infixBinaryExpression("<=", `A, `B); }
      Gt (A, B)			-> { infixBinaryExpression(">", `A, `B); }
      Ge (A, B)			-> { infixBinaryExpression(">=", `A, `B); }
      NotEqual (A, B)	-> { infixBinaryExpression("<>", `A, `B); }
	  Equal (A, B) 		-> { infixBinaryExpression("=", `A, `B); } 
	  In (A, B) 		-> { prefixBinaryExpression("mem", `A, `B);}
	  NotIn (A, B)		-> { prefixBinaryExpression("not mem", `A, `B);}	
	  Subset (A, B)		-> { prefixBinaryExpression("subsetprop", `A, `B);}
      NotSubset (A, B)  -> { prefixBinaryExpression("not subsetprop", `A, `B);}
	  SubsetEq (A, B)  	-> { prefixBinaryExpression("subset", `A, `B);}
      NotSubsetEq (A, B) -> { prefixBinaryExpression("not subset", `A, `B);}
	        
	/*Associative Predicate*/
      Land (X)			-> { foldLeftInfixPredicate("/\\", `X); }
      Lor (X)			-> { foldLeftInfixPredicate("\\/", `X); }
      
  	 
  	 /*Unary Predicate*/
  	  Not(X)			-> { prefixUnaryPredicate("not", `X);}
  	  
     /*Quantified Predicate*/
   
      ForAll (A, B) 	-> { QuantifiedPredicate("forall", `A, `B);} 
      Exists (A, B) 	-> { QuantifiedPredicate("exists", `A, `B);} 
   
    }
  }
  
  public void translate(Expression expression) throws TranslationException {
    %match (Expression expression) {
      IntegerLiteral(X)  	-> {translateInteger(`X); }
	  X@FreeIdentifier(_)	-> {translateIdentifier((FreeIdentifier)`X); }
	  X@BoundIdentifier(_)  -> {translateBoundIdentifier((BoundIdentifier)`X); }
	
	  SetExtension (X) 		-> { setextension(`X, 0); }
	
	  /* Associate expressions */
      Plus (X) 			 	-> { foldLeftInfix("+", `X); }
      Mul (X) 			 	-> { foldLeftInfix("*", `X); }
      BUnion (X) 			-> { foldLeftPrefix("union", `X); }   
      BInter (X) 			-> { foldLeftPrefix("inter", `X); }           
      Fcomp (X) 			-> { foldLeftPrefix("fcomp", `X); }
      Bcomp (X) 			-> { foldLeftPrefix("bcomp", `X); }
      Ovr (X) 				-> { foldLeftInfix("<+", `X); }
      
      /* Unary Expression*/
      Union(X)			 -> { prefixUnaryExpression("gunion", `X); }	
      Inter(X)			 -> { prefixUnaryExpression("ginter", `X); } 
 	  Dom (X)			 -> { prefixUnaryExpression("dom", `X); }
      Ran (X)			 -> { prefixUnaryExpression("ran", `X); }
      Converse (X)		 -> { prefixUnaryExpression("inverse", `X);}
      Max (X)		 	 -> { prefixUnaryExpression("max", `X);}   
      Min (X)		 	 -> { prefixUnaryExpression("min", `X);}   
      Card (X)		     -> { prefixUnaryExpression("card", `X);}
      Pow (X)		     -> { prefixUnaryExpression("pow", `X);}
      Pow1 (X)		     -> { prefixUnaryExpression("pow1", `X);}
      
	  /* Binary Expression*/
      Div (A, B) 			 -> { prefixBinaryExpression("div", `A, `B); }
      Mod (A, B)			 -> { prefixBinaryExpression("mod", `A, `B); }
      Expn (A, B)			 -> { prefixBinaryExpression("power", `A, `B); }
      Minus (A, B)			 -> { infixBinaryExpression("-", `A, `B); }
      Mapsto (A, B) 		 -> { infixBinaryExpression(", ", `A, `B);}
      Rel (A, B) 			 -> { prefixBinaryExpression2("pow","cprod", `A, `B);}
      UpTo (A, B) 			 -> { prefixBinaryExpression("mk", `A, `B);}
      SetMinus (A, B)		 -> { prefixBinaryExpression("diff", `A, `B);}
      FunImage (A, B) 		 -> { prefixBinaryExpression("apply", `A, `B);}
      RelImage (A, B) 		 -> { prefixBinaryExpression("image", `A, `B);}
      Pfun (A, B)			 -> { infixBinaryExpression("+->", `A, `B); }
      Tfun (A, B)			 -> { infixBinaryExpression("-->", `A, `B); }
      Trel (A, B)			 -> { infixBinaryExpression("<<->", `A, `B); }
      Srel (A, B)			 -> { infixBinaryExpression("<->>", `A, `B); }
      Strel (A, B)			 -> { infixBinaryExpression("<<->>", `A, `B); }
      Pinj (A, B)			 -> { infixBinaryExpression(">+>", `A, `B); }
      Tinj (A, B)			 -> { infixBinaryExpression(">->", `A, `B); }
      Psur(A, B)			 -> { infixBinaryExpression("+->>", `A, `B); }
      Tsur (A, B)			 -> { infixBinaryExpression("-->>", `A, `B); }
      Tbij (A, B)			 -> { infixBinaryExpression(">->>", `A, `B); }
      Cprod (A, B)			 -> { prefixBinaryExpression("cprod", `A, `B); }
      Pprod (A, B)			 -> { prefixBinaryExpression("pprod", `A, `B); }
      Dprod (A, B)			 -> { prefixBinaryExpression("dprod", `A, `B); }

      DomRes (A, B)			 -> { infixBinaryExpression("<|", `A, `B); }
      DomSub (A, B)			 -> { infixBinaryExpression("<<|", `A, `B); }
      RanRes (A, B)			 -> { infixBinaryExpression("|>", `A, `B); }
      RanSub (A, B)			 -> { infixBinaryExpression("|>>", `A, `B); }
      
      /* Qunatified expression */
      Cset (A, B, C)		 ->	{QuantifiedExpression("bsetc", `A, `B, `C); }		
      Qinter (A, B, C)		 ->	{throw new NotImplementedException(expression.toString()); }
      Qunion (A, B, C)		 ->	{throw new NotImplementedException(expression.toString()); }
      
      /* atomic */
  	  FALSE()				  -> { prettyprint("false");}
  	  TRUE()				  -> { prettyprint("true");}
      Natural()				  -> { prettyprint("natural");}
      Natural1()			  -> { prettyprint("bnatural1");}
      INTEGER()				  -> { prettyprint("integer");}
      BOOL()				  -> { prettyprint("boolean");}
      X@EmptySet()			  -> { translateEmptySet(`X);}
      Prj1Gen()				  -> { prettyprint("prj1");}
      Prj2Gen()				  -> { prettyprint("prj2");}
      IdGen()				  -> { prettyprint("id");}
  	}
  } 
  
  private void translateEmptySet(Expression es) throws TranslationException {
  		sb.append("(");
  		sb.append("empty");
  		sb.append(":");
  		sb.append("(");
		tt.translate(es.getType());  		
  		sb.append(")");
  		sb.append(")");
  }
  
  private void translateInteger(BigInteger z) {
  	if (z.compareTo(BigInteger.ZERO) < 0) {
  		sb.append("(- ");
  		sb.append(z.negate().toString());
   		sb.append(")"); 		
  	} else {
  		sb.append(z.toString());
  	}
  }
  
  
  private void setextension(Expression[] parts, int offset) throws TranslationException {
	if (offset == parts.length - 1) {
		prefixUnaryExpression("singleton", parts[parts.length - 1]);
	} else {
		sb.append("(");
		sb.append("add");
		sb.append(" ");
		translate(parts[offset]);
		sb.append(" ");
		setextension(parts, offset + 1);
		sb.append(")");
	}
  }
  
  /**
  * Helper methods for constructing strings
  */

  private void translateBoundIdentifierDecl(BoundIdentDecl bid) throws TranslationException {
  	String targetName = nameMapper.mapBoundIdentifier(bid.getName());
  	System.out.println("BI1: " + bid.getName() + " -> " + targetName); 
  	sb.append(targetName);
  	sb.append(" : ");
  	tt.translate(bid.getType());
  }
  
  private void translateBoundIdentifier(BoundIdentifier bi) {
	String image = resolveIndex(bi.getBoundIndex(), boundNames);
	assert (image != null);
	String targetName = nameMapper.mapBoundIdentifier(image);
  	System.out.println("BI2: " + bi.getBoundIndex() + " -> " +image + " -> " + targetName); 
	sb.append(targetName);
  }  
  
   private void translateIdentifier(FreeIdentifier fi) throws TranslationException {
	if (nameMapper.isKnown(fi.getName())) {
		if (nameMapper.getKind(fi.getName()) == INameMapper.KIND.FREE) {
			identifiers.add(fi);
		} else if (nameMapper.getKind(fi.getName()) == INameMapper.KIND.TYPE) {
		    typeIdentifiers.add(fi);
			// make sure the type is included in the translation output
		    treferenceKeeper.referenceType(nameMapper.mapType(fi.getName()));	
		} else if (nameMapper.getKind(fi.getName()) == INameMapper.KIND.TYPE_LITERAL) {
			// make sure the type declaring the given type literal is included in the translation output
			tt.reference(fi.getType());
		}
		sb.append(nameMapper.mapName(fi.getName()));	
	} else {
		identifiers.add(fi);
	    sb.append(nameMapper.mapFreeIdentifier(fi.getName()));
	}
  }


  private void emitIdentifier(FreeIdentifier fi) throws TranslationException {
  	 aux.append("\tconstant ");
  	 aux.append(nameMapper.mapName(fi.getName()));
  	 aux.append(" : ");
  	 tt_aux.translate(fi.getType());
  	 aux.append("\n");
  }

  private void foldLeftInfix(String operator, Expression[] args) throws TranslationException {
		 foldLeftInfix(operator, args.length - 1, args);
  }
  
  private void foldLeftInfix(String operator, int base, Expression[] args) throws TranslationException {
  	assert(args.length >= 2);
  	
  	if (base == 1) {
  		sb.append("("); 
  		translate(args[0]); 
  		sb.append(" "); 
  		sb.append(operator); 
  		sb.append(" "); 
  		translate(args[1]); 
  		sb.append(")"); 
  	} else {
  		sb.append("("); 
  		foldLeftInfix(operator, base - 1, args);	
  		sb.append(" "); 
  		sb.append(operator); 
  		sb.append(" "); 
  		translate(args[base]); 
  		sb.append(")"); 
  	}
  }
  
  private void foldLeftInfixPredicate(String operator, Predicate[] args) throws TranslationException {
		 foldLeftInfixPredicate(operator, args.length - 1, args);
  }
  
  private void foldLeftInfixPredicate(String operator, int base, Predicate[] args) throws TranslationException {
  	assert(args.length >= 2);
  	
  	if (base == 1) {
  		sb.append("("); 
  		translate(args[0]); 
  		sb.append(" "); 
  		sb.append(operator); 
  		sb.append(" "); 
  		translate(args[1]); 
  		sb.append(")"); 
  	} else {
  		sb.append("("); 
  		foldLeftInfixPredicate(operator, base - 1, args);	
  		sb.append(" "); 
  		sb.append(operator); 
  		sb.append(" "); 
  		translate(args[base]); 
  		sb.append(")"); 
  	}
  }
  
   private void foldLeftPrefixPredicate(String operator, Predicate[] args) throws TranslationException {
		 foldLeftPrefixPredicate(operator, args.length - 1, args);
  }
  
  private void foldLeftPrefixPredicate(String operator, int base, Predicate[] args) throws TranslationException {
  	assert(args.length >= 2);
  	
  	if (base == 1) {
  		sb.append("("); 
   		sb.append(operator); 
  		sb.append(" ");  		
  		translate(args[0]); 
  		sb.append(" "); 
  		translate(args[1]); 
  		sb.append(")"); 
  	} else {
  		sb.append("("); 
  		sb.append(operator); 
  		sb.append(" ");   		
  		foldLeftPrefixPredicate(operator, base - 1, args);	
  		sb.append(" "); 
  		translate(args[base]); 
  		sb.append(")"); 
  	}
  } 
  
   private void foldLeftPrefix(String operator, Expression[] args) throws TranslationException {
		 foldLeftPrefix(operator, args.length - 1, args);
  }
  
  private void foldLeftPrefix(String operator, int base, Expression[] args) throws TranslationException {
  	assert(args.length >= 2);
  	
  	if (base == 1) {
  		sb.append("("); 
   		sb.append(operator); 
  		sb.append(" ");  		
  		translate(args[0]); 
  		sb.append(" "); 
  		translate(args[1]); 
  		sb.append(")"); 
  	} else {
  		sb.append("("); 
  		sb.append(operator); 
  		sb.append(" ");   		
  		foldLeftPrefix(operator, base - 1, args);	
  		sb.append(" "); 
  		translate(args[base]); 
  		sb.append(")"); 
  	}
  }  
  
  
    // makes a string of the form (op a)
  private void prefixUnaryPredicate(String op, Predicate a) throws TranslationException {
  	sb.append("(");
  	sb.append(op);
  	sb.append(" ");
  	translate(a);
  	sb.append(")");
  } 
  
  
  // makes a string of the form (op a b)
  private void prefixBinaryPredicate(String op, Predicate a, Predicate b) throws TranslationException {
  	sb.append("(");
  	sb.append(op);
  	sb.append(" ");
  	translate(a);
  	sb.append(" ");
  	translate(b);
  	sb.append(")");
  } 

  // makes a string of the form (op a b)
  private void prefixBinaryExpression(String op, Expression a, Expression b) throws TranslationException {
  	sb.append("(");
  	sb.append(op);
  	sb.append(" ");
  	translate(a);
  	sb.append(" ");
  	translate(b);
  	sb.append(")");
  } 

  private void prefixBinaryExpression2(String op1, String op2, Expression a, Expression b) throws TranslationException {
  	sb.append("(");
  	sb.append(op1);
  	sb.append("(");
  	sb.append(op2);
  	sb.append(" ");
  	translate(a);
  	sb.append(" ");
  	translate(b);
  	sb.append(")");
  	sb.append(")");
  } 


  // makes a string of the form (a op b)
  private void infixBinaryPredicate(String op, Predicate a, Predicate b) throws TranslationException {
  	sb.append("(");
  	translate(a);
  	sb.append(" ");
  	sb.append(op);
  	sb.append(" ");
  	translate(b);
  	sb.append(")");
  }
  
  // makes a string of the form (op a : b.) forall, exists
   private void QuantifiedPredicate(String op, BoundIdentDecl[] identifiers , Predicate pred) throws TranslationException {

	String[] c = boundNames.clone();
	boundNames = addBound(identifiers);

   	sb.append("(");
  	sb.append(op);
  	sb.append(" ");
	for(int i = 0; i < identifiers.length; i++) {
			if (i > 0) sb.append(", ");
			translateBoundIdentifierDecl(identifiers[i]);
	}  	
  	sb.append(" . (");
  	translate(pred);
  	sb.append("))");
  	
	boundNames = c;
	for(int i = 0; i < identifiers.length; i++) {
			nameMapper.removeLocallyBoundName(identifiers[i].getName());
	}  		
   
  }
    private void translateBoundIdentifierDeclType(BoundIdentDecl bid) throws TranslationException {
  	tt.translate(bid.getType());
  }
    private void translateBoundIdentifierDeclName(BoundIdentDecl bid) throws TranslationException {
  	String targetName = nameMapper.mapBoundIdentifier(bid.getName());
  	System.out.println("BI1: " + bid.getName() + " -> " + targetName); 
  	sb.append(targetName);
  }
  
    // makes a string of the form  set comprehension
   private void QuantifiedExpression(String op, BoundIdentDecl[] identifiers , Predicate pred, Expression expr) throws TranslationException {

	String[] c = boundNames.clone();
	boundNames = addBound(identifiers);

   	sb.append("(");
  	sb.append(op);
  	sb.append(" ");
  	sb.append("(\\z:(");
	for(int i = 0; i < identifiers.length; i++) {
			if (i > 0) sb.append(", ");
			translateBoundIdentifierDeclType(identifiers[i]);
		}  	
  	sb.append(") .");
  	sb.append("(let (");
  		for(int i = 0; i < identifiers.length; i++) {
			if (i > 0) sb.append(", ");
			translateBoundIdentifierDeclName(identifiers[i]);
		}
	sb.append(")");	
	sb.append(" = ");
	sb.append("z in ");		
  	translate(pred);
	sb.append(")) ");
	sb.append("(\\z:("); // Expression part
	for(int i = 0; i < identifiers.length; i++) {
			if (i > 0) sb.append(", ");
			translateBoundIdentifierDeclType(identifiers[i]);
		}  	
  	sb.append(") .");
  	sb.append("(let (");
  		for(int i = 0; i < identifiers.length; i++) {
			if (i > 0) sb.append(", ");
			translateBoundIdentifierDeclName(identifiers[i]);
		}
	sb.append(")");	
	sb.append(" = ");
	sb.append("z in ");		
  	translate(expr);
	sb.append(")) ");
	sb.append(")"); // Closing op
 	boundNames = c;
   
  }

  // makes a string of the form (a op b) (for expressions)
  private void infixBinaryExpression(String op, Expression a, Expression b) throws TranslationException {
  	sb.append("(");
  	translate(a);
  	sb.append(" ");
  	sb.append(op);
  	sb.append(" ");
  	translate(b);
  	sb.append(")");
  } 
  
  // makes a string of the form (op a) (for expressions)
  private void prefixUnaryExpression(String op, Expression a) throws TranslationException {
    sb.append("(");
  	sb.append(op);
  	sb.append(" ");
  	translate(a);
  	sb.append(")");
  } 
 

  // simply prints an object as it is
  private void prettyprint(Object s) {
  	sb.append(s.toString());
  }  
  
}
  