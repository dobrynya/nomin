package org.nomin.util

import org.nomin.core.preprocessing.Preprocessing
import org.mockito.Mockito
import static java.util.Arrays.asList

/**
 * Tests ArrayHelper.
 * @author Dmitry Dobrynin
 * Date: 28.11.2010 time: 22:13:48
 */
class ArrayHelperTest {
  ArrayHelper ah = new ArrayHelper(TypeInfoFactory.typeInfo(Entity[].class))
  Preprocessing preprocessing = Mockito.mock(Preprocessing)
  Preprocessing[] preprocessings = [ preprocessing ]

  @org.junit.Test
  void testCreateContainer() {
    def res = ah.createContainer(5)
    assert res && res.class.isArray() && res.class.getComponentType() == Entity && res.length == 5
  }

  @org.junit.Test
  void testConvert() {
    def val1 = new Object(), val2 = new Object()
    def preprocessed1 = new Entity(), preprocessed2 = new Entity()
    def source = [val1, val2]
    Mockito.when(preprocessing.preprocess(val1)).thenReturn(preprocessed1)
    Mockito.when(preprocessing.preprocess(val2)).thenReturn(preprocessed2)
    def res = ah.convert(source, preprocessings)
    assert res && [preprocessed1, preprocessed2] == asList((Object[]) res)
  }

  @org.junit.Test
  void testGetElement() {
    assert preprocessing == ah.getElement(preprocessings, 0)
    assert ah.getElement(preprocessings, 1) == null

    try { ah.getElement(preprocessings, -1); assert false } catch (IndexOutOfBoundsException ignored) {}
  }

  @org.junit.Test
  void testSetElement() {
    def val = new Object(), ent = new Entity()
    Mockito.when(preprocessing.preprocess(val)).thenReturn(ent)
    def res = ah.setElement(null, 0, val, preprocessings)
    assert res && res[0] == ent

    res = ah.setElement(new Entity[1], 2, val, preprocessings)
    assert res && res.length == 3 && !res[0] && !res[1] && res[2] == ent

    res = ah.setElement(new Entity[1], -1, val, preprocessings)
    assert res && res.length == 2 && res[1] == ent
  }
}


