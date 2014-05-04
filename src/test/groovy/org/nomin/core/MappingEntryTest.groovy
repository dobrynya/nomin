package org.nomin.core

import org.junit.Test
import org.nomin.Mapping
import org.nomin.core.preprocessing.*
import static org.nomin.util.TypeInfoFactory.typeInfo

/**
 * Document please.
 * @author Dmitry Dobrynin
 * Created 12.04.2010 18:46:00
 */
class MappingEntryTest {

  def e = new MappingEntry(mapping: new Mapping())

  @Test
  void testCompleted() {
    assert !e.completed()
    e.pathElem new RootPathElem()
    assert !e.completed()
    e.pathElem new RootPathElem()
    assert e.completed()
  }

  @Test
  void testNoNeedPreprocessing() {
    assert e.preprocessings(
            new MappingSide(lastRuleElem: new PropRuleElem(null, typeInfo(String), null)),
            new MappingSide(lastRuleElem: new PropRuleElem(null, typeInfo(String), null)))[0] == null

    assert e.preprocessings(
            new MappingSide(lastRuleElem: new PropRuleElem(null, List[String], null)),
            new MappingSide(lastRuleElem: new PropRuleElem(null, Set[String], null)))[0] == null
  }

  @Test
  void testConversionPreprocessing() {
    assert e.preprocessings(
            new MappingSide(lastRuleElem: new PropRuleElem(null, typeInfo(String), null), conversion: { it }),
            new MappingSide(lastRuleElem: new PropRuleElem(null, typeInfo(String), null), conversion: { it }))[0] instanceof ConversionPreprocessing

    assert e.preprocessings(
            new MappingSide(lastRuleElem: new PropRuleElem(null, List[String], null), conversion: { it }),
            new MappingSide(lastRuleElem: new PropRuleElem(null, Set[String], null), conversion: { it }))[0] instanceof ConversionPreprocessing
  }

  @Test
  void testConvertUtilsPreprocessing() {
    assert e.preprocessings(
            new MappingSide(lastRuleElem: new PropRuleElem(null, typeInfo(Integer), null)),
            new MappingSide(lastRuleElem: new PropRuleElem(null, typeInfo(String), null)))[0] instanceof ConverterPreprocessing
    assert e.preprocessings(
            new MappingSide(lastRuleElem: new PropRuleElem(null, typeInfo(String), null)),
            new MappingSide(lastRuleElem: new PropRuleElem(null, typeInfo(Integer), null)))[0] instanceof ConverterPreprocessing

    assert e.preprocessings(
            new MappingSide(lastRuleElem: new PropRuleElem(null, List[Integer], null)),
            new MappingSide(lastRuleElem: new PropRuleElem(null, Set[String], null)))[0] instanceof ConverterPreprocessing
    assert e.preprocessings(
            new MappingSide(lastRuleElem: new PropRuleElem(null, List[String], null)),
            new MappingSide(lastRuleElem: new PropRuleElem(null, Set[Integer], null)))[0] instanceof ConverterPreprocessing
  }

  @Test
  void testDynamicPreprocessing() {
    assert e.preprocessings(
            new MappingSide(lastRuleElem: new PropRuleElem(null, typeInfo(String), null)),
            new MappingSide(lastRuleElem: new PropRuleElem(null, typeInfo(Undefined), null)))[0] instanceof DynamicPreprocessing
    assert e.preprocessings(
            new MappingSide(lastRuleElem: new PropRuleElem(null, typeInfo({ String }), null)),
            new MappingSide(lastRuleElem: new PropRuleElem(null, typeInfo(String), null)))[0] instanceof DynamicPreprocessing

    assert e.preprocessings(
            new MappingSide(lastRuleElem: new PropRuleElem(null, List[String], null)),
            new MappingSide(lastRuleElem: new PropRuleElem(null, typeInfo(Undefined), null)))[0] instanceof DynamicPreprocessing
    assert e.preprocessings(
            new MappingSide(lastRuleElem: new PropRuleElem(null, List[{ String }], null)),
            new MappingSide(lastRuleElem: new PropRuleElem(null, List[String], null)))[0] instanceof DynamicPreprocessing
  }
}

/*
class A {
  String propA1, propA2, propA3
  List<String> propA4, propA5, propA6
  List<Object> propA7
}

class B {
  String propB1, propB2
  List<String> propB4, propB5
  List<Object> propB7
}

class A2B extends Mapping {
  protected void build() {
    mappingFor a: A, b: B
    a.propA1 = b.propB1 // the types are defined

    a.propA2 = b.propB2
    convert to_a: { it }, to_b: { it } // the conversions are given

    a.propA3 = { "just a string" } // undefined result type

    a.propA4 = b.propB4 // the types are defined

    a.propA5 = b.propB5
    convert to_a: { it }, to_b: { it } // the conversions are given

    a.propA6 = { ["just", "a", "string"] } // undefined result type

    a.propA7 = b.propB7
    hint a: List[{ it.class }], b: List[{ it.class }]
  }
}
*/
