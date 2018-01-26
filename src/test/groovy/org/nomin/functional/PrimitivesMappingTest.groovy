package org.nomin.functional

import org.junit.Test
import org.nomin.Mapping
import org.nomin.core.Nomin

/**
 * Tests mapping between primitive values.
 * @author Dmitry Dobrynin <dobrynya@inbox.ru>
 * Created at 26.01.18 18:23
 * */
class PrimitivesMappingTest {
    Nomin nomin = new Nomin(A2B)

    static class A {
        byte b;
        boolean bool;
        long l;
        int i;
    }

    static class B {
        Byte b;
        Boolean bool;
        Long l;
        Integer i;
    }

    @Test
    void testDirectMapping() {
        def b = nomin.map(new A(b: 11, bool: true, l: 17L, i: 125), B)
        assert b.b == 11 && b.bool && b.l == 17L && b.i == 125
    }

    @Test
    void revertMapping() {
        def a = nomin.map(new B(b: 11, bool: true, l: 17L, i: 125), A)
        assert a.b == 11 && a.bool && a.l == 17L && a.i == 125
    }
}

class A2B extends Mapping {
    protected void build() {
        mappingFor a: PrimitivesMappingTest.A, b: PrimitivesMappingTest.B
        a.b = b.b
        a.bool = b.bool;
        a.l = b.l
        a.i = b.i
    }
}