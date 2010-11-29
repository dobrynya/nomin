package org.nomin.util

import org.nomin.core.ClassImprover
import org.nomin.core.preprocessing.Preprocessing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Matchers.anyObject
import org.nomin.core.preprocessing.ConversionPreprocessing

/**
 * Tests MapHelper.
 * @author Dmitry Dobrynin
 * Date: 29.11.2010 Time: 14:27:19
 */
class MapHelperTest {
  static { ClassImprover }

  MapHelper mh = new MapHelper(Map[String, Entity])

  @org.junit.Test
  void testCreateContainer() {
    assert new MapHelper(Map[String, Entity]).createContainer(1) instanceof HashMap
    assert new MapHelper(TreeMap[String, Entity]).createContainer(1) instanceof TreeMap
  }

  @org.junit.Test
  void testGetElement() {
    assert mh.getElement([:], "non-existent") == null
    assert mh.getElement([key: 1], "key") == 1
  }

  @org.junit.Test
  void testSetElement() {
    def value = new Object(), value1 = new Object()
    assert [key: value] == mh.setElement(null, "key", value, null)
    assert [key1: value1, key: value] == mh.setElement([key1: value1], "key", value, null)
  }

  @org.junit.Test
  void testConvert() {
    def value = new Object()
    assert [key: value] == mh.convert([key: value].entrySet(), null)

    Preprocessing preprocessing = mock(ConversionPreprocessing)
    def newValue = new Object(), element = new Object()
    when(preprocessing.preprocess(element)).thenReturn(["newKey", newValue])
    assert [newKey: newValue] == mh.convert([element], [preprocessing] as Preprocessing[])
  }
}
