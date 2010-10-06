package org.nomin.core;

import java.util.*;
import groovy.lang.Closure;
import org.junit.*;
import org.mockito.Mockito;
import org.nomin.Mapping;
import org.nomin.entity.*;
import static java.util.Arrays.*;
import static org.junit.Assert.*;

/**
 * Tests replacing a mapping by parsed mapping with matching sides and mappingCase.
 * @author Dmitry Dobrynin
 *         Created 20.05.2010 17:29:51
 */
public class NominMappingReplacementTest {
    Nomin nomin = new Nomin();
    Class<?> p = Person.class, e = Employee.class;
    List<MappingRule> mrl = Collections.<MappingRule>emptyList();
    Map<String, Closure> hm = Collections.<String, Closure>emptyMap();

    @Test
    public void testMappingReplacement() {
        ParsedMapping p1 = new ParsedMapping("", p, e, null, mrl, hm, false, null, nomin);
        ParsedMapping p2 = new ParsedMapping("", e, p, null, mrl, hm, false, null, nomin);
        ParsedMapping p3 = new ParsedMapping("", e, p, "The first", mrl, hm, false, null, nomin);
        ParsedMapping p4 = new ParsedMapping("", p, e, "The first", mrl, hm, false, null, nomin);
        Mapping m1 = Mockito.mock(Mapping.class), m2 = Mockito.mock(Mapping.class), m3 = Mockito.mock(Mapping.class),
                m4 = Mockito.mock(Mapping.class);
        Mockito.when(m1.parse()).thenReturn(p1);
        Mockito.when(m2.parse()).thenReturn(p2);
        Mockito.when(m3.parse()).thenReturn(p3);
        Mockito.when(m4.parse()).thenReturn(p4);
        nomin.parse(m1);
        assertEquals(1, nomin.mappings.size());
        assertTrue(nomin.mappings.contains(p1));
        nomin.parse(m2);
        assertEquals(1, nomin.mappings.size());
        assertTrue(nomin.mappings.contains(p2));
        nomin.parse(m3);
        assertEquals(2, nomin.mappings.size());
        assertTrue(nomin.mappings.containsAll(asList(p2, p3)));
        nomin.parse(m4);
        assertEquals(2, nomin.mappings.size());
        assertTrue(nomin.mappings.containsAll(asList(p2, p4)));
    }
}
