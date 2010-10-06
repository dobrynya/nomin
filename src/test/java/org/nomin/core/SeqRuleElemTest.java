package org.nomin.core;

import java.util.*;
import org.junit.Test;
import org.nomin.core.SeqRuleElem;
import static org.junit.Assert.assertEquals;

/**
 * Document please.
 * @author Dmitry Dobrynin
 *         Created 28.04.2010 11:44:39
 */
public class SeqRuleElemTest {

    @Test
    public void testRead() {
        SeqRuleElem sme = new SeqRuleElem(null, 0);
        assertEquals("List", sme.retrieve(Arrays.asList("List")));
        assertEquals("Set", sme.retrieve(new HashSet(Arrays.asList("Set"))));
    }

    @Test
    public void testWrite() {
        SeqRuleElem sme = new SeqRuleElem(null, 0);
        List<String> list = new ArrayList<String>();
        sme.store(list, "List", null);
        assertEquals(1, list.size());
        assertEquals("List", list.get(0));
        sme = new SeqRuleElem(null, 1);
        sme.store(list, "List1", null);
        assertEquals(2, list.size());
        assertEquals("List", list.get(0));
        assertEquals("List1", list.get(1));
    }
}
