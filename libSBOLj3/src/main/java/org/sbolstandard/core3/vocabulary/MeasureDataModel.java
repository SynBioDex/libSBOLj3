package org.sbolstandard.core3.vocabulary;

import java.net.URI;

import org.sbolstandard.core3.util.URINameSpace;

/**
 * 
 * Representing units of measurement in the SBOL data model.
 *
 */
public class MeasureDataModel {
	
	/**
	 * 
	 * Defines a prefix in the data model.
	 *
	 */
	public static final class Prefix
	{
		public static URI uri=URINameSpace.OM.local("Prefix");
		public static URI factor=URINameSpace.OM.local("hasFactor");
	}
	
	/**
	 * 
	 * Defines a prefix as an SI unit in the data model.
	 *
	 */
	public static final class SIPrefix
	{
		public static URI uri=URINameSpace.OM.local("SIPrefix");
	}
	
	/**
	 * 
	 * Defines a binary prefix in the data model.
	 *
	 */
	public static final class BinaryPrefix
	{
		public static URI uri=URINameSpace.OM.local("BinaryPrefix");
	}
	
	/**
	 * 
	 * Defines the structure of a unit in the data model.
	 *
	 */
	public static final class Unit
	{
		public static URI uri=URINameSpace.OM.local("Unit");
		public static URI symbol=URINameSpace.OM.local("symbol");
		public static URI alternativeSymbol=URINameSpace.OM.local("alternativeSymbol");
		public static URI label=URINameSpace.RDFS.local("label");
		public static URI alternativeLabel=URINameSpace.OM.local("alternativeLabel");
		public static URI comment=URINameSpace.RDFS.local("comment");
		public static URI longComment=URINameSpace.OM.local("longcomment");
	}
	
	/**
	 * 
	 * Defines a singular unit in the data model.
	 *
	 */
	public static final class SingularUnit
	{
		public static URI uri=URINameSpace.OM.local("SingularUnit");
		public static URI factor=Prefix.factor;
		public static URI unit=PrefixedUnit.unit;
	}
	
	/**
	 * 
	 * Defines a compound unit in the data model.
	 *
	 */
	public static final class CompoundUnit
	{
		public static URI uri=URINameSpace.OM.local("CompoundUnit");
	}
	
	/**
	 * 
	 * Defines a prefixed unit in the data model.
	 *
	 */
	public static final class PrefixedUnit
	{
		public static URI uri=URINameSpace.OM.local("PrefixedUnit");
		public static URI prefix=URINameSpace.OM.local("hasPrefix");
		public static URI unit=URINameSpace.OM.local("hasUnit");	
	}
	
	/**
	 * 
	 * Defines the multiplication of two units in the data model.
	 *
	 */
	public static final class UnitMultiplication
	{
		public static URI uri=URINameSpace.OM.local("UnitMultiplication");
		public static URI term1=URINameSpace.OM.local("hasTerm1");
		public static URI term2=URINameSpace.OM.local("hasTerm2");	
	}
	
	/**
	 * 
	 * Defines the division of two units in the data model.
	 *
	 */
	public static final class UnitDivision
	{
		public static URI uri=URINameSpace.OM.local("UnitDivision");
		public static URI numerator=URINameSpace.OM.local("hasNumerator");
		public static URI denominator=URINameSpace.OM.local("hasDenominator");	
	}
	
	/**
	 * 
	 * Defines the application of an exponent to a unit in the data model.
	 *
	 */
	public static final class UnitExponentiation
	{
		public static URI uri=URINameSpace.OM.local("UnitExponentiation");
		public static URI base=URINameSpace.OM.local("hasBase");
		public static URI exponent=URINameSpace.OM.local("hasExponent");	
	}
	
	/**
	 * 
	 * Defines a measure in the data model.
	 *
	 */
	public static final class Measure
	{
		public static URI uri=URINameSpace.OM.local("Measure");
		public static URI value=URINameSpace.OM.local("hasNumericalValue");
		public static URI type=URINameSpace.SBOL.local("type");	
		public static URI unit=PrefixedUnit.unit;
		
	}
			
}
