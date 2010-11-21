package org.nomin.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests SunInstanceCreator.
 * @author Dmitry Dobrynin
 *         Date: 26.10.2010 Time: 23:04:00
 */
public class SunInstanceCreatorTest {

    @Test (expected = InstantiationException.class)
    public void testUsingClassNewInstance() throws IllegalAccessException, InstantiationException {
        WithoutDefaultConstructor.class.newInstance();
    }

    @Test
    public void testUsingInstanceCreator() throws Exception {
        WithoutDefaultConstructor wdc = new SunInstanceCreator().create(WithoutDefaultConstructor.class);
        assertNotNull(wdc);
    }

    static class WithoutDefaultConstructor {
        public WithoutDefaultConstructor(String stuff) { throw new IllegalArgumentException("!"); }
    }
}
