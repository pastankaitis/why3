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


private static boolean tom_equal_term_char(char t1, char t2) {
return  t1==t2 ;
}
private static boolean tom_is_sort_char(char t) {
return  true ;
}
private static boolean tom_equal_term_String(String t1, String t2) {
return  t1.equals(t2) ;
}
private static boolean tom_is_sort_String(String t) {
return  t instanceof String ;
}
private static boolean tom_equal_term_Type(Object t1, Object t2) {
return  t1.equals(t2) ;
}
private static boolean tom_is_sort_Type(Object t) {
return t instanceof Type ;
}
private static boolean tom_equal_term_TypeList(Object t1, Object t2) {
return 
		java.util.Arrays.equals((Type[]) t1, (Type[]) t2)
	;
}
private static boolean tom_is_fun_sym_PowSet( Type  t) {
return  t instanceof PowerSetType ;
}
private static  Type  tom_get_slot_PowSet_child( Type  t) {
return  ((PowerSetType) t).getBaseType() ;
}
private static boolean tom_is_fun_sym_CProd( Type  t) {
return  t instanceof ProductType ;
}
private static  Type  tom_get_slot_CProd_left( Type  t) {
return  ((ProductType) t).getLeft() ;
}
private static  Type  tom_get_slot_CProd_right( Type  t) {
return  ((ProductType) t).getRight() ;
}
private static boolean tom_is_fun_sym_Set( Type  t) {
return  t instanceof GivenType ;
}
private static  String  tom_get_slot_Set_name( Type  t) {
return  ((GivenType) t).getName() ;
}
private static boolean tom_is_fun_sym_Int( Type  t) {
return  t instanceof IntegerType ;
}
private static boolean tom_is_fun_sym_Bool( Type  t) {
return  t instanceof BooleanType ;
}


public TypeTranslator(StringBuilder typeDeclarations, StringBuilder stringbuilder, INameMapper nameMapper, ITypeReferenceKeeper treferenceKeeper) {
this.typeDeclarations = typeDeclarations;
sb = stringbuilder; 	
this.nameMapper = nameMapper;
this.treferenceKeeper = treferenceKeeper;
}	

public void translate(Type type) throws TranslationException {

{
{
if (tom_is_sort_Type(type)) {
if (tom_is_fun_sym_Int((( Type )type))) {
prettyprint("int");
}
}

}
{
if (tom_is_sort_Type(type)) {
if (tom_is_fun_sym_Bool((( Type )type))) {
prettyprint("bool");
}
}

}
{
if (tom_is_sort_Type(type)) {
if (tom_is_fun_sym_Set((( Type )type))) {
givenSet(
tom_get_slot_Set_name((( Type )type)));

}
}

}
{
if (tom_is_sort_Type(type)) {
if (tom_is_fun_sym_PowSet((( Type )type))) {
sb.append("(set "); translate(
tom_get_slot_PowSet_child((( Type )type))); sb.append(")"); 

}
}

}
{
if (tom_is_sort_Type(type)) {
if (tom_is_fun_sym_CProd((( Type )type))) {
sb.append("("); translate(
tom_get_slot_CProd_left((( Type )type))); sb.append(", "); translate(
tom_get_slot_CProd_right((( Type )type))); sb.append(")"); 

}
}

}


}

}

public void reference(Type type) throws TranslationException {

{
{
if (tom_is_sort_Type(type)) {
if (tom_is_fun_sym_Set((( Type )type))) {
referenceType(
tom_get_slot_Set_name((( Type )type)));

}
}

}
{
if (tom_is_sort_Type(type)) {
if (tom_is_fun_sym_PowSet((( Type )type))) {
reference(
tom_get_slot_PowSet_child((( Type )type))); 

}
}

}
{
if (tom_is_sort_Type(type)) {
if (tom_is_fun_sym_CProd((( Type )type))) {
reference(
tom_get_slot_CProd_left((( Type )type))); reference(
tom_get_slot_CProd_right((( Type )type)));  

}
}

}


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

