package uk.ac.ncl.eventb.why3.gen.translation;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import static org.eventb.core.ast.QuantifiedUtil.catenateBoundIdentLists;
import org.eventb.core.ast.*;

import uk.ac.ncl.eventb.why3.translator.INameMapper;
import uk.ac.ncl.eventb.why3.translator.ITypeReferenceKeeper;
import uk.ac.ncl.eventb.why3.translator.TypeTranslator;
import uk.ac.ncl.eventb.why3.translator.TranslationException;
import uk.ac.ncl.eventb.why3.translator.NotImplementedException;
import uk.ac.ncl.eventb.why3.gen.TriggerContext;

public class GenericTranslator implements ITranslator {
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
private TriggerContext triggerContext;
private IGenericTranslatorPort genericPort; 	


private static boolean tom_equal_term_int(int t1, int t2) {
return  t1==t2 ;
}
private static boolean tom_is_sort_int(int t) {
return  true ;
}
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
private static boolean tom_equal_term_Predicate(Object t1, Object t2) {
return  t1.equals(t2) ;
}
private static boolean tom_is_sort_Predicate(Object t) {
return  t instanceof Predicate ;
}
private static boolean tom_equal_term_Expression(Object t1, Object t2) {
return  t1.equals(t2) ;
}
private static boolean tom_is_sort_Expression(Object t) {
return  t instanceof Expression ;
}
private static boolean tom_equal_term_BoundIdentDecl(Object t1, Object t2) {
return  t1.equals(t2) ;
}
private static boolean tom_is_sort_BoundIdentDecl(Object t) {
return  t instanceof BoundIdentDecl ;
}
private static boolean tom_equal_term_BigInteger(Object t1, Object t2) {
return  t1.equals(t2) ;
}
private static boolean tom_is_sort_BigInteger(Object t) {
return  t instanceof BigInteger ;
}
private static boolean tom_equal_term_PredicateList(Object t1, Object t2) {
return 
		java.util.Arrays.equals((Predicate[]) t1, (Predicate[]) t2)
	;
}
private static boolean tom_is_sort_PredicateList(Object t) {
return  t instanceof Predicate[] ;
}
private static boolean tom_equal_term_ExpressionList(Object t1, Object t2) {
return 
		java.util.Arrays.equals((Expression[]) t1, (Expression[]) t2)
	;
}
private static boolean tom_is_sort_ExpressionList(Object t) {
return  t instanceof Expression[] ;
}
private static boolean tom_equal_term_BoundIdentDeclList(Object t1, Object t2) {
return 
		java.util.Arrays.equals((BoundIdentDecl[]) t1, (BoundIdentDecl[]) t2)
	;
}
private static boolean tom_is_sort_BoundIdentDeclList(Object t) {
return  t instanceof BoundIdentDecl[] ;
}
private static boolean tom_is_fun_sym_Land( Predicate  t) {
return  t != null && t.getTag() == Formula.LAND ;
}
private static  Predicate[]  tom_get_slot_Land_children( Predicate  t) {
return  ((AssociativePredicate) t).getChildren() ;
}
private static boolean tom_is_fun_sym_Lor( Predicate  t) {
return  t != null && t.getTag() == Formula.LOR ;
}
private static  Predicate[]  tom_get_slot_Lor_children( Predicate  t) {
return  ((AssociativePredicate) t).getChildren() ;
}
private static boolean tom_is_fun_sym_Limp( Predicate  t) {
return  t != null && t.getTag() == Formula.LIMP ;
}
private static  Predicate  tom_get_slot_Limp_left( Predicate  t) {
return  ((BinaryPredicate) t).getLeft() ;
}
private static  Predicate  tom_get_slot_Limp_right( Predicate  t) {
return  ((BinaryPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_Leqv( Predicate  t) {
return  t != null && t.getTag() == Formula.LEQV ;
}
private static  Predicate  tom_get_slot_Leqv_left( Predicate  t) {
return  ((BinaryPredicate) t). getLeft() ;
}
private static  Predicate  tom_get_slot_Leqv_right( Predicate  t) {
return  ((BinaryPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_Not( Predicate  t) {
return  t != null && t.getTag() == Formula.NOT ;
}
private static  Predicate  tom_get_slot_Not_child( Predicate  t) {
return  ((UnaryPredicate) t). getChild() ;
}
private static boolean tom_is_fun_sym_Equal( Predicate  t) {
return  t != null && t.getTag() == Formula.EQUAL ;
}
private static  Expression  tom_get_slot_Equal_left( Predicate  t) {
return  ((RelationalPredicate) t). getLeft() ;
}
private static  Expression  tom_get_slot_Equal_right( Predicate  t) {
return  ((RelationalPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_NotEqual( Predicate  t) {
return  t != null && t.getTag() == Formula.NOTEQUAL ;
}
private static  Expression  tom_get_slot_NotEqual_left( Predicate  t) {
return  ((RelationalPredicate) t). getLeft() ;
}
private static  Expression  tom_get_slot_NotEqual_right( Predicate  t) {
return  ((RelationalPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_Lt( Predicate  t) {
return  t != null && t.getTag() == Formula.LT ;
}
private static  Expression  tom_get_slot_Lt_left( Predicate  t) {
return  ((RelationalPredicate) t). getLeft() ;
}
private static  Expression  tom_get_slot_Lt_right( Predicate  t) {
return  ((RelationalPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_Le( Predicate  t) {
return  t != null && t.getTag() == Formula.LE ;
}
private static  Expression  tom_get_slot_Le_left( Predicate  t) {
return  ((RelationalPredicate) t). getLeft() ;
}
private static  Expression  tom_get_slot_Le_right( Predicate  t) {
return  ((RelationalPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_Gt( Predicate  t) {
return  t != null && t.getTag() == Formula.GT ;
}
private static  Expression  tom_get_slot_Gt_left( Predicate  t) {
return  ((RelationalPredicate) t). getLeft() ;
}
private static  Expression  tom_get_slot_Gt_right( Predicate  t) {
return  ((RelationalPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_Ge( Predicate  t) {
return  t != null && t.getTag() == Formula.GE ;
}
private static  Expression  tom_get_slot_Ge_left( Predicate  t) {
return  ((RelationalPredicate) t). getLeft() ;
}
private static  Expression  tom_get_slot_Ge_right( Predicate  t) {
return  ((RelationalPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_In( Predicate  t) {
return  t != null && t.getTag() == Formula.IN ;
}
private static  Expression  tom_get_slot_In_left( Predicate  t) {
return  ((RelationalPredicate) t). getLeft() ;
}
private static  Expression  tom_get_slot_In_right( Predicate  t) {
return  ((RelationalPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_NotIn( Predicate  t) {
return  t != null && t.getTag() == Formula.NOTIN ;
}
private static  Expression  tom_get_slot_NotIn_left( Predicate  t) {
return  ((RelationalPredicate) t). getLeft() ;
}
private static  Expression  tom_get_slot_NotIn_right( Predicate  t) {
return  ((RelationalPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_Subset( Predicate  t) {
return  t != null && t.getTag() == Formula.SUBSET ;
}
private static  Expression  tom_get_slot_Subset_left( Predicate  t) {
return  ((RelationalPredicate) t). getLeft() ;
}
private static  Expression  tom_get_slot_Subset_right( Predicate  t) {
return  ((RelationalPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_NotSubset( Predicate  t) {
return  t != null && t.getTag() == Formula.NOTSUBSET ;
}
private static  Expression  tom_get_slot_NotSubset_left( Predicate  t) {
return  ((RelationalPredicate) t). getLeft() ;
}
private static  Expression  tom_get_slot_NotSubset_right( Predicate  t) {
return  ((RelationalPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_SubsetEq( Predicate  t) {
return  t != null && t.getTag() == Formula.SUBSETEQ ;
}
private static  Expression  tom_get_slot_SubsetEq_left( Predicate  t) {
return  ((RelationalPredicate) t). getLeft() ;
}
private static  Expression  tom_get_slot_SubsetEq_right( Predicate  t) {
return  ((RelationalPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_NotSubsetEq( Predicate  t) {
return  t != null && t.getTag() == Formula.NOTSUBSETEQ ;
}
private static  Expression  tom_get_slot_NotSubsetEq_left( Predicate  t) {
return  ((RelationalPredicate) t). getLeft() ;
}
private static  Expression  tom_get_slot_NotSubsetEq_right( Predicate  t) {
return  ((RelationalPredicate) t).getRight() ;
}
private static boolean tom_is_fun_sym_ForAll( Predicate  t) {
return  t != null && t.getTag() == Formula.FORALL ;
}
private static  BoundIdentDecl[]  tom_get_slot_ForAll_identifiers( Predicate  t) {
return  ((QuantifiedPredicate)t).getBoundIdentDecls() ;
}
private static  Predicate  tom_get_slot_ForAll_predicate( Predicate  t) {
return  ((QuantifiedPredicate)t).getPredicate() ;
}
private static boolean tom_is_fun_sym_Exists( Predicate  t) {
return  t != null && t.getTag() == Formula.EXISTS ;
}
private static  BoundIdentDecl[]  tom_get_slot_Exists_identifiers( Predicate  t) {
return  ((QuantifiedPredicate)t).getBoundIdentDecls() ;
}
private static  Predicate  tom_get_slot_Exists_predicate( Predicate  t) {
return  ((QuantifiedPredicate)t).getPredicate() ;
}
private static boolean tom_is_fun_sym_BTRUE( Predicate  t) {
return  t != null && t.getTag() == Formula.BTRUE ;
}
private static boolean tom_is_fun_sym_BFALSE( Predicate  t) {
return  t != null && t.getTag() == Formula.BFALSE ;
}
private static boolean tom_is_fun_sym_Finite( Predicate  t) {
return  t != null && t.getTag() == Formula.KFINITE ;
}
private static  Expression  tom_get_slot_Finite_child( Predicate  t) {
return  ((SimplePredicate) t).getExpression() ;
}
private static boolean tom_is_fun_sym_BUnion( Expression  t) {
return  t != null && t.getTag() == Formula.BUNION ;
}
private static  Expression[]  tom_get_slot_BUnion_children( Expression  t) {
return  ((AssociativeExpression) t).getChildren() ;
}
private static boolean tom_is_fun_sym_BInter( Expression  t) {
return  t != null && t.getTag() == Formula.BINTER ;
}
private static  Expression[]  tom_get_slot_BInter_children( Expression  t) {
return  ((AssociativeExpression) t).getChildren() ;
}
private static boolean tom_is_fun_sym_Bcomp( Expression  t) {
return  t != null && t.getTag() == Formula.BCOMP ;
}
private static  Expression[]  tom_get_slot_Bcomp_children( Expression  t) {
return  ((AssociativeExpression) t).getChildren() ;
}
private static boolean tom_is_fun_sym_Fcomp( Expression  t) {
return  t != null && t.getTag() == Formula.FCOMP ;
}
private static  Expression[]  tom_get_slot_Fcomp_children( Expression  t) {
return  ((AssociativeExpression) t).getChildren() ;
}
private static boolean tom_is_fun_sym_Ovr( Expression  t) {
return  t != null && t.getTag() == Formula.OVR ;
}
private static  Expression[]  tom_get_slot_Ovr_children( Expression  t) {
return  ((AssociativeExpression) t).getChildren() ;
}
private static boolean tom_is_fun_sym_Plus( Expression  t) {
return  t != null && t.getTag() == Formula.PLUS ;
}
private static  Expression[]  tom_get_slot_Plus_children( Expression  t) {
return  ((AssociativeExpression) t).getChildren() ;
}
private static boolean tom_is_fun_sym_Mul( Expression  t) {
return  t != null && t.getTag() == Formula.MUL ;
}
private static  Expression[]  tom_get_slot_Mul_children( Expression  t) {
return  ((AssociativeExpression) t).getChildren() ;
}
private static boolean tom_is_fun_sym_Natural( Expression  t) {
return  t != null && t.getTag() == Formula.NATURAL ;
}
private static boolean tom_is_fun_sym_Natural1( Expression  t) {
return  t != null && t.getTag() == Formula.NATURAL1 ;
}
private static boolean tom_is_fun_sym_INTEGER( Expression  t) {
return  t != null && t.getTag() == Formula.INTEGER ;
}
private static boolean tom_is_fun_sym_BOOL( Expression  t) {
return  t != null && t.getTag() == Formula.BOOL ;
}
private static boolean tom_is_fun_sym_TRUE( Expression  t) {
return  t != null && t.getTag() == Formula.TRUE ;
}
private static boolean tom_is_fun_sym_FALSE( Expression  t) {
return  t != null && t.getTag() == Formula.FALSE ;
}
private static boolean tom_is_fun_sym_EmptySet( Expression  t) {
return  t != null && t.getTag() == Formula.EMPTYSET ;
}
private static boolean tom_is_fun_sym_Prj1Gen( Expression  t) {
return  t != null && t.getTag() == Formula.KPRJ1_GEN ;
}
private static boolean tom_is_fun_sym_Prj2Gen( Expression  t) {
return  t != null && t.getTag() == Formula.KPRJ2_GEN ;
}
private static boolean tom_is_fun_sym_IdGen( Expression  t) {
return  t != null && t.getTag() == Formula.KID_GEN ;
}
private static boolean tom_is_fun_sym_Mapsto( Expression  t) {
return  t != null && t.getTag() == Formula.MAPSTO ;
}
private static  Expression  tom_get_slot_Mapsto_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Mapsto_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Rel( Expression  t) {
return  t != null && t.getTag() == Formula.REL ;
}
private static  Expression  tom_get_slot_Rel_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Rel_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Trel( Expression  t) {
return  t != null && t.getTag() == Formula.TREL ;
}
private static  Expression  tom_get_slot_Trel_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Trel_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Srel( Expression  t) {
return  t != null && t.getTag() == Formula.SREL ;
}
private static  Expression  tom_get_slot_Srel_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Srel_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Strel( Expression  t) {
return  t != null && t.getTag() == Formula.STREL ;
}
private static  Expression  tom_get_slot_Strel_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Strel_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Pfun( Expression  t) {
return  t != null && t.getTag() == Formula.PFUN ;
}
private static  Expression  tom_get_slot_Pfun_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Pfun_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Tfun( Expression  t) {
return  t != null && t.getTag() == Formula.TFUN ;
}
private static  Expression  tom_get_slot_Tfun_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Tfun_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Pinj( Expression  t) {
return  t != null && t.getTag() == Formula.PINJ ;
}
private static  Expression  tom_get_slot_Pinj_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Pinj_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Tinj( Expression  t) {
return  t != null && t.getTag() == Formula.TINJ ;
}
private static  Expression  tom_get_slot_Tinj_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Tinj_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Psur( Expression  t) {
return  t != null && t.getTag() == Formula.PSUR ;
}
private static  Expression  tom_get_slot_Psur_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Psur_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Tsur( Expression  t) {
return  t != null && t.getTag() == Formula.TSUR ;
}
private static  Expression  tom_get_slot_Tsur_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Tsur_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Tbij( Expression  t) {
return  t != null && t.getTag() == Formula.TBIJ ;
}
private static  Expression  tom_get_slot_Tbij_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Tbij_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_SetMinus( Expression  t) {
return  t != null && t.getTag() == Formula.SETMINUS ;
}
private static  Expression  tom_get_slot_SetMinus_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_SetMinus_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Cprod( Expression  t) {
return  t != null && t.getTag() == Formula.CPROD ;
}
private static  Expression  tom_get_slot_Cprod_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Cprod_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Dprod( Expression  t) {
return  t != null && t.getTag() == Formula.DPROD ;
}
private static  Expression  tom_get_slot_Dprod_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Dprod_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Pprod( Expression  t) {
return  t != null && t.getTag() == Formula.PPROD ;
}
private static  Expression  tom_get_slot_Pprod_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Pprod_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_DomRes( Expression  t) {
return  t != null && t.getTag() == Formula.DOMRES ;
}
private static  Expression  tom_get_slot_DomRes_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_DomRes_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_DomSub( Expression  t) {
return  t != null && t.getTag() == Formula.DOMSUB ;
}
private static  Expression  tom_get_slot_DomSub_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_DomSub_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_RanRes( Expression  t) {
return  t != null && t.getTag() == Formula.RANRES ;
}
private static  Expression  tom_get_slot_RanRes_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_RanRes_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_RanSub( Expression  t) {
return  t != null && t.getTag() == Formula.RANSUB ;
}
private static  Expression  tom_get_slot_RanSub_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_RanSub_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_UpTo( Expression  t) {
return  t != null && t.getTag() == Formula.UPTO ;
}
private static  Expression  tom_get_slot_UpTo_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_UpTo_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Minus( Expression  t) {
return  t != null && t.getTag() == Formula.MINUS ;
}
private static  Expression  tom_get_slot_Minus_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Minus_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Div( Expression  t) {
return  t != null && t.getTag() == Formula.DIV ;
}
private static  Expression  tom_get_slot_Div_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Div_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_Mod( Expression  t) {
return  t != null && t.getTag() == Formula.MOD ;
}
private static  Expression  tom_get_slot_Mod_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_Mod_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_FunImage( Expression  t) {
return  t != null && t.getTag() == Formula.FUNIMAGE ;
}
private static  Expression  tom_get_slot_FunImage_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_FunImage_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_RelImage( Expression  t) {
return  t != null && t.getTag() == Formula.RELIMAGE ;
}
private static  Expression  tom_get_slot_RelImage_left( Expression  t) {
return  ((BinaryExpression) t).getLeft() ;
}
private static  Expression  tom_get_slot_RelImage_right( Expression  t) {
return  ((BinaryExpression) t).getRight() ;
}
private static boolean tom_is_fun_sym_FreeIdentifier( Expression  t) {
return  t != null && t.getTag() == Formula.FREE_IDENT ;
}
private static  String  tom_get_slot_FreeIdentifier_name( Expression  t) {
return  ((FreeIdentifier)t).getName() ;
}
private static boolean tom_is_fun_sym_BoundIdentifier( Expression  t) {
return  t != null && t.getTag() == Formula.BOUND_IDENT ;
}
private static  int  tom_get_slot_BoundIdentifier_boundIndex( Expression  t) {
return  ((BoundIdentifier)t).getBoundIndex() ;
}
private static boolean tom_is_fun_sym_IntegerLiteral( Expression  t) {
return  t instanceof IntegerLiteral ;
}
private static  BigInteger  tom_get_slot_IntegerLiteral_value( Expression  t) {
return  ((IntegerLiteral) t).getValue() ;
}
private static boolean tom_is_fun_sym_Cset( Expression  t) {
return  t != null && t.getTag() == Formula.CSET ;
}
private static  BoundIdentDecl[]  tom_get_slot_Cset_identifiers( Expression  t) {
return  ((QuantifiedExpression)t).getBoundIdentDecls() ;
}
private static  Predicate  tom_get_slot_Cset_predicate( Expression  t) {
return  ((QuantifiedExpression)t).getPredicate() ;
}
private static  Expression  tom_get_slot_Cset_expression( Expression  t) {
return  ((QuantifiedExpression)t).getExpression() ;
}
private static boolean tom_is_fun_sym_Qinter( Expression  t) {
return  t != null && t.getTag() == Formula.QINTER ;
}
private static  BoundIdentDecl[]  tom_get_slot_Qinter_identifiers( Expression  t) {
return  ((QuantifiedExpression)t).getBoundIdentDecls() ;
}
private static  Predicate  tom_get_slot_Qinter_predicate( Expression  t) {
return  ((QuantifiedExpression)t).getPredicate() ;
}
private static  Expression  tom_get_slot_Qinter_expression( Expression  t) {
return  ((QuantifiedExpression)t).getExpression() ;
}
private static boolean tom_is_fun_sym_Qunion( Expression  t) {
return  t != null && t.getTag() == Formula.QUNION ;
}
private static  BoundIdentDecl[]  tom_get_slot_Qunion_identifiers( Expression  t) {
return  ((QuantifiedExpression)t).getBoundIdentDecls() ;
}
private static  Predicate  tom_get_slot_Qunion_predicate( Expression  t) {
return  ((QuantifiedExpression)t).getPredicate() ;
}
private static  Expression  tom_get_slot_Qunion_expression( Expression  t) {
return  ((QuantifiedExpression)t).getExpression() ;
}
private static boolean tom_is_fun_sym_SetExtension( Expression  t) {
return  t != null && t.getTag() == Formula.SETEXT ;
}
private static  Expression[]  tom_get_slot_SetExtension_members( Expression  t) {
return  ((SetExtension)t).getMembers() ;
}
private static boolean tom_is_fun_sym_Card( Expression  t) {
return  t != null && t.getTag() == Formula.KCARD ;
}
private static  Expression  tom_get_slot_Card_child( Expression  t) {
return  ((UnaryExpression) t).getChild() ;
}
private static boolean tom_is_fun_sym_Pow( Expression  t) {
return  t != null && t.getTag() == Formula.POW ;
}
private static  Expression  tom_get_slot_Pow_child( Expression  t) {
return  ((UnaryExpression) t).getChild() ;
}
private static boolean tom_is_fun_sym_Pow1( Expression  t) {
return  t != null && t.getTag() == Formula.POW1 ;
}
private static  Expression  tom_get_slot_Pow1_child( Expression  t) {
return  ((UnaryExpression) t).getChild() ;
}
private static boolean tom_is_fun_sym_Union( Expression  t) {
return  t != null && t.getTag() == Formula.KUNION ;
}
private static  Expression  tom_get_slot_Union_child( Expression  t) {
return  ((UnaryExpression) t).getChild() ;
}
private static boolean tom_is_fun_sym_Inter( Expression  t) {
return  t != null && t.getTag() == Formula.KINTER ;
}
private static  Expression  tom_get_slot_Inter_child( Expression  t) {
return  ((UnaryExpression) t).getChild() ;
}
private static boolean tom_is_fun_sym_Dom( Expression  t) {
return  t != null && t.getTag() == Formula.KDOM ;
}
private static  Expression  tom_get_slot_Dom_child( Expression  t) {
return  ((UnaryExpression) t).getChild() ;
}
private static boolean tom_is_fun_sym_Ran( Expression  t) {
return  t != null && t.getTag() == Formula.KRAN ;
}
private static  Expression  tom_get_slot_Ran_child( Expression  t) {
return  ((UnaryExpression) t).getChild() ;
}
private static boolean tom_is_fun_sym_Min( Expression  t) {
return  t != null && t.getTag() == Formula.KMIN ;
}
private static  Expression  tom_get_slot_Min_child( Expression  t) {
return  ((UnaryExpression) t).getChild() ;
}
private static boolean tom_is_fun_sym_Max( Expression  t) {
return  t != null && t.getTag() == Formula.KMAX ;
}
private static  Expression  tom_get_slot_Max_child( Expression  t) {
return  ((UnaryExpression) t).getChild() ;
}
private static boolean tom_is_fun_sym_Converse( Expression  t) {
return  t != null && t.getTag() == Formula.CONVERSE ;
}
private static  Expression  tom_get_slot_Converse_child( Expression  t) {
return  ((UnaryExpression) t).getChild() ;
}


public GenericTranslator(StringBuilder aux, INameMapper nameMapper, ITypeReferenceKeeper treferenceKeeper, TriggerContext triggerContext, IGenericTranslatorPort genericPort) {
this.typeDeclarations = new StringBuilder();
this.treferenceKeeper = treferenceKeeper;
this.aux = aux;
this.nameMapper = nameMapper;
this.triggerContext = triggerContext;
this.genericPort = genericPort;
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

@Override
public void translate(Type type) throws TranslationException {
tt.translate(type);
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


@Override
public void translate(Predicate predicate) throws TranslationException {
triggerContext.enter(predicate.getTag());

// see if there is a user-defined translation pattern and apply if applicable
if (genericPort.translate(predicate)) 
return;


{
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_BTRUE((predicate))) {
prettyprint("true");
}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_BFALSE((predicate))) {
prettyprint("false");
}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_Finite((predicate))) {
prefixUnaryExpression(genericPort.mapOperator(predicate.getTag(), "finite"), 
tom_get_slot_Finite_child((predicate)));

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_Limp((predicate))) {
infixBinaryPredicate("->", 
tom_get_slot_Limp_left((predicate)), 
tom_get_slot_Limp_right((predicate))); 

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_Leqv((predicate))) {
infixBinaryPredicate("<->", 
tom_get_slot_Leqv_left((predicate)), 
tom_get_slot_Leqv_right((predicate)));

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_Lt((predicate))) {
infixBinaryExpression("<", 
tom_get_slot_Lt_left((predicate)), 
tom_get_slot_Lt_right((predicate))); 

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_Le((predicate))) {
infixBinaryExpression("<=", 
tom_get_slot_Le_left((predicate)), 
tom_get_slot_Le_right((predicate))); 

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_Gt((predicate))) {
infixBinaryExpression(">", 
tom_get_slot_Gt_left((predicate)), 
tom_get_slot_Gt_right((predicate))); 

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_Ge((predicate))) {
infixBinaryExpression(">=", 
tom_get_slot_Ge_left((predicate)), 
tom_get_slot_Ge_right((predicate))); 

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_NotEqual((predicate))) {
infixBinaryExpression("<>", 
tom_get_slot_NotEqual_left((predicate)), 
tom_get_slot_NotEqual_right((predicate))); 

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_Equal((predicate))) {
infixBinaryExpression("=", 
tom_get_slot_Equal_left((predicate)), 
tom_get_slot_Equal_right((predicate))); 

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_In((predicate))) {
prefixBinaryExpression(genericPort.mapOperator(predicate.getTag(), "mem"), 
tom_get_slot_In_left((predicate)), 
tom_get_slot_In_right((predicate)));

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_NotIn((predicate))) {
prefixBinaryExpression(genericPort.mapOperator(predicate.getTag(), "not mem"), 
tom_get_slot_NotIn_left((predicate)), 
tom_get_slot_NotIn_right((predicate)));

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_Subset((predicate))) {
prefixBinaryExpression(genericPort.mapOperator(predicate.getTag(), "subsetprop"), 
tom_get_slot_Subset_left((predicate)), 
tom_get_slot_Subset_right((predicate)));

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_NotSubset((predicate))) {
prefixBinaryExpression(genericPort.mapOperator(predicate.getTag(), "not subsetprop"), 
tom_get_slot_NotSubset_left((predicate)), 
tom_get_slot_NotSubset_right((predicate)));

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_SubsetEq((predicate))) {
prefixBinaryExpression(genericPort.mapOperator(predicate.getTag(), "subset"), 
tom_get_slot_SubsetEq_left((predicate)), 
tom_get_slot_SubsetEq_right((predicate)));

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_NotSubsetEq((predicate))) {
prefixBinaryExpression(genericPort.mapOperator(predicate.getTag(), "not subset"), 
tom_get_slot_NotSubsetEq_left((predicate)), 
tom_get_slot_NotSubsetEq_right((predicate)));

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_Land((predicate))) {
foldLeftInfixPredicate("/\\", 
tom_get_slot_Land_children((predicate))); 

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_Lor((predicate))) {
foldLeftInfixPredicate("\\/", 
tom_get_slot_Lor_children((predicate))); 

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_Not((predicate))) {
prefixUnaryPredicate("not", 
tom_get_slot_Not_child((predicate)));

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_ForAll((predicate))) {
QuantifiedPredicate("forall", 
tom_get_slot_ForAll_identifiers((predicate)), 
tom_get_slot_ForAll_predicate((predicate)));

}
}

}
{
if (tom_is_sort_Predicate(predicate)) {
if (tom_is_fun_sym_Exists((predicate))) {
QuantifiedPredicate("exists", 
tom_get_slot_Exists_identifiers((predicate)), 
tom_get_slot_Exists_predicate((predicate)));

}
}

}


}


triggerContext.leave(predicate.getTag());

}

@Override
public void translate(Expression expression) throws TranslationException {
triggerContext.enter(expression.getTag());

// see if there is a user-defined translation pattern and apply if applicable
if (genericPort.translate(expression)) 
return;


{
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_IntegerLiteral((expression))) {
translateInteger(
tom_get_slot_IntegerLiteral_value((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_FreeIdentifier((expression))) {
translateIdentifier((FreeIdentifier)
(expression)); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_BoundIdentifier((expression))) {
translateBoundIdentifier((BoundIdentifier)
(expression)); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_SetExtension((expression))) {
setextension(
tom_get_slot_SetExtension_members((expression)), 0); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Plus((expression))) {
foldLeftInfix("+", 
tom_get_slot_Plus_children((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Mul((expression))) {
foldLeftInfix("*", 
tom_get_slot_Mul_children((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_BUnion((expression))) {
foldLeftPrefix(genericPort.mapOperator(expression.getTag(), "union"), 
tom_get_slot_BUnion_children((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_BInter((expression))) {
foldLeftPrefix(genericPort.mapOperator(expression.getTag(), "inter"), 
tom_get_slot_BInter_children((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Fcomp((expression))) {
foldLeftPrefix(genericPort.mapOperator(expression.getTag(), "fcomp"), 
tom_get_slot_Fcomp_children((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Bcomp((expression))) {
foldLeftPrefix(genericPort.mapOperator(expression.getTag(), "bcomp"), 
tom_get_slot_Bcomp_children((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Ovr((expression))) {
foldLeftInfix (genericPort.mapOperator(expression.getTag(), "<+"), 
tom_get_slot_Ovr_children((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Union((expression))) {
prefixUnaryExpression(genericPort.mapOperator(expression.getTag(), "gunion"), 
tom_get_slot_Union_child((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Inter((expression))) {
prefixUnaryExpression(genericPort.mapOperator(expression.getTag(), "ginter"), 
tom_get_slot_Inter_child((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Dom((expression))) {
prefixUnaryExpression(genericPort.mapOperator(expression.getTag(), "dom"), 
tom_get_slot_Dom_child((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Ran((expression))) {
prefixUnaryExpression(genericPort.mapOperator(expression.getTag(), "ran"), 
tom_get_slot_Ran_child((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Converse((expression))) {
prefixUnaryExpression(genericPort.mapOperator(expression.getTag(), "inverse"), 
tom_get_slot_Converse_child((expression)));

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Max((expression))) {
prefixUnaryExpression(genericPort.mapOperator(expression.getTag(), "max"), 
tom_get_slot_Max_child((expression)));

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Min((expression))) {
prefixUnaryExpression(genericPort.mapOperator(expression.getTag(), "min"), 
tom_get_slot_Min_child((expression)));

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Card((expression))) {
prefixUnaryExpression(genericPort.mapOperator(expression.getTag(), "card"), 
tom_get_slot_Card_child((expression)));

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Pow((expression))) {
prefixUnaryExpression(genericPort.mapOperator(expression.getTag(), "pow"), 
tom_get_slot_Pow_child((expression)));

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Pow1((expression))) {
prefixUnaryExpression(genericPort.mapOperator(expression.getTag(), "pow1"), 
tom_get_slot_Pow1_child((expression)));

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Div((expression))) {
prefixBinaryExpression(genericPort.mapOperator(expression.getTag(), "div"), 
tom_get_slot_Div_left((expression)), 
tom_get_slot_Div_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Mod((expression))) {
prefixBinaryExpression(genericPort.mapOperator(expression.getTag(), "mod"), 
tom_get_slot_Mod_left((expression)), 
tom_get_slot_Mod_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Minus((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), "-"), 
tom_get_slot_Minus_left((expression)), 
tom_get_slot_Minus_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Mapsto((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), ", "), 
tom_get_slot_Mapsto_left((expression)), 
tom_get_slot_Mapsto_right((expression)));

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Rel((expression))) {
prefixBinaryExpression(genericPort.mapOperator(expression.getTag(), "pow cprod"), 
tom_get_slot_Rel_left((expression)), 
tom_get_slot_Rel_right((expression)));

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_UpTo((expression))) {
prefixBinaryExpression(genericPort.mapOperator(expression.getTag(), "mk"), 
tom_get_slot_UpTo_left((expression)), 
tom_get_slot_UpTo_right((expression)));

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_SetMinus((expression))) {
prefixBinaryExpression(genericPort.mapOperator(expression.getTag(), "diff"), 
tom_get_slot_SetMinus_left((expression)), 
tom_get_slot_SetMinus_right((expression)));

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_FunImage((expression))) {
prefixBinaryExpression(genericPort.mapOperator(expression.getTag(), "apply"), 
tom_get_slot_FunImage_left((expression)), 
tom_get_slot_FunImage_right((expression)));

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_RelImage((expression))) {
prefixBinaryExpression(genericPort.mapOperator(expression.getTag(), "image"), 
tom_get_slot_RelImage_left((expression)), 
tom_get_slot_RelImage_right((expression)));

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Pfun((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), "+->"), 
tom_get_slot_Pfun_left((expression)), 
tom_get_slot_Pfun_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Tfun((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), "-->"), 
tom_get_slot_Tfun_left((expression)), 
tom_get_slot_Tfun_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Trel((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), "<<->"), 
tom_get_slot_Trel_left((expression)), 
tom_get_slot_Trel_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Srel((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), "<->>"), 
tom_get_slot_Srel_left((expression)), 
tom_get_slot_Srel_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Strel((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), "<<->>"), 
tom_get_slot_Strel_left((expression)), 
tom_get_slot_Strel_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Pinj((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), ">+>"), 
tom_get_slot_Pinj_left((expression)), 
tom_get_slot_Pinj_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Tinj((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), ">->"), 
tom_get_slot_Tinj_left((expression)), 
tom_get_slot_Tinj_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Psur((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), "+->>"), 
tom_get_slot_Psur_left((expression)), 
tom_get_slot_Psur_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Tsur((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), "-->>"), 
tom_get_slot_Tsur_left((expression)), 
tom_get_slot_Tsur_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Tbij((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), ">->>"), 
tom_get_slot_Tbij_left((expression)), 
tom_get_slot_Tbij_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Cprod((expression))) {
prefixBinaryExpression(genericPort.mapOperator(expression.getTag(), "cprod"), 
tom_get_slot_Cprod_left((expression)), 
tom_get_slot_Cprod_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Pprod((expression))) {
prefixBinaryExpression(genericPort.mapOperator(expression.getTag(), "pprod"), 
tom_get_slot_Pprod_left((expression)), 
tom_get_slot_Pprod_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Dprod((expression))) {
prefixBinaryExpression(genericPort.mapOperator(expression.getTag(), "dprod"), 
tom_get_slot_Dprod_left((expression)), 
tom_get_slot_Dprod_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_DomRes((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), "<|"), 
tom_get_slot_DomRes_left((expression)), 
tom_get_slot_DomRes_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_DomSub((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), "<<|"), 
tom_get_slot_DomSub_left((expression)), 
tom_get_slot_DomSub_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_RanRes((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), "|>"), 
tom_get_slot_RanRes_left((expression)), 
tom_get_slot_RanRes_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_RanSub((expression))) {
infixBinaryExpression(genericPort.mapOperator(expression.getTag(), "|>>"), 
tom_get_slot_RanSub_left((expression)), 
tom_get_slot_RanSub_right((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Cset((expression))) {
QuantifiedExpression("bsetc", 
tom_get_slot_Cset_identifiers((expression)), 
tom_get_slot_Cset_predicate((expression)), 
tom_get_slot_Cset_expression((expression))); 

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Qinter((expression))) {
throw new NotImplementedException(expression.toString()); 
}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Qunion((expression))) {
throw new NotImplementedException(expression.toString()); 
}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_FALSE((expression))) {
prettyprint("false");
}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_TRUE((expression))) {
prettyprint("true");
}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Natural((expression))) {
prettyprint("natural");
}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Natural1((expression))) {
prettyprint("bnatural1");
}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_INTEGER((expression))) {
prettyprint("integer");
}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_BOOL((expression))) {
prettyprint("boolean");
}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_EmptySet((expression))) {
translateEmptySet(
(expression));

}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Prj1Gen((expression))) {
prettyprint(genericPort.mapOperator(expression.getTag(), "prj1"));
}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_Prj2Gen((expression))) {
prettyprint(genericPort.mapOperator(expression.getTag(), "prj2"));
}
}

}
{
if (tom_is_sort_Expression(expression)) {
if (tom_is_fun_sym_IdGen((expression))) {
prettyprint(genericPort.mapOperator(expression.getTag(), "id"));
}
}

}


}


triggerContext.leave(expression.getTag());

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

//System.out.println("BI " + bid.getName() + " maps to " + nameMapper.mapFreeIdentifier(bid.getName())); 
sb.append(nameMapper.mapFreeIdentifier(bid.getName()));
sb.append(" : ");
tt.translate(bid.getType());
}

private void translateBoundIdentifier(BoundIdentifier bi) {
String image = resolveIndex(bi.getBoundIndex(), boundNames);
assert (image != null);
sb.append(nameMapper.mapFreeIdentifier(image));
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

}
// makes a string of the form  set comprehension
private void QuantifiedExpression(String op, BoundIdentDecl[] identifiers , Predicate pred, Expression expr) throws TranslationException {

String[] c = boundNames.clone();
boundNames = addBound(identifiers);

sb.append("(");
sb.append(op);
sb.append(" ");
sb.append("(\\");
for(int i = 0; i < identifiers.length; i++) {
if (i > 0) sb.append(", ");
translateBoundIdentifierDecl(identifiers[i]);
}  	
sb.append(" .");
translate(pred);
sb.append(")");
sb.append(" ");
sb.append("(");
sb.append("\\");
for(int i = 0; i < identifiers.length; i++) {
if (i > 0) sb.append(", ");
translateBoundIdentifierDecl(identifiers[i]);
}  
sb.append(" .");	
translate(expr);
sb.append("))");

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

