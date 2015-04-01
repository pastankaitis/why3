package uk.ac.ncl.eventb.why3.translator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.eventb.core.ast.*;

/**
All tom files must be called Tran*.t !
*/

public class TypeTranslator {
  private INameMapper nameMapper;
  private StringBuilder sb;
  private StringBuilder typeDeclarations;
  private ITypeReferenceKeeper treferenceKeeper; 

  %include {Type.tom}
	
  public TypeTranslator(StringBuilder typeDeclarations, StringBuilder stringbuilder, INameMapper nameMapper, ITypeReferenceKeeper treferenceKeeper) {
    this.typeDeclarations = typeDeclarations;
  	sb = stringbuilder; 	
  	this.nameMapper = nameMapper;
  	this.treferenceKeeper = treferenceKeeper;
  }	

  public void translate(Type type) throws TranslationException {
    %match (Type type) {
     Int()			-> {prettyprint("int");}
     Bool()			-> {prettyprint("bool");}
     Set(X)			-> {givenSet(`X);}
   	 PowSet(X)		-> {sb.append("(set "); translate(`X); sb.append(")"); }
   	 CProd(A, B)	-> {sb.append("("); translate(`A); sb.append(", "); translate(`B); sb.append(")"); }
    }
  }

  public void reference(Type type) throws TranslationException {
    %match (Type type) {
     Set(X)			-> {referenceType(`X);}
   	 PowSet(X)		-> {reference(`X); }
   	 CProd(A, B)	-> {reference(`A); reference(`B);  }
    }
  }

  private void givenSet(String name) throws TranslationException {
  	String typeName = nameMapper.mapType(name);
    sb.append(typeName);
    treferenceKeeper.referenceType(typeName);
  }

  private void referenceType(String name) throws TranslationException {
  	String typeName = nameMapper.mapType(name);
    treferenceKeeper.referenceType(typeName);
  }
  

  // simply prints an object as it is
  private void prettyprint(Object s) {
  	sb.append(s.toString());
  }  
  
}
  