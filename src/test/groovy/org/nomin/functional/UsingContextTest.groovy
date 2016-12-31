package org.nomin.functional

import org.junit.*
import org.nomin.context.MapContext
import org.nomin.core.Nomin

/**
 * Tests using context values.
 * @author Dmitry Dobrynin <dobrynya@inbox.ru>
 * Created at 31.12.16 14:49.
 */
class UsingContextTest {
    static class ContextValues {
        String val1
        String val2
        Integer val3
    }

    @Test
    void mapperShouldApplyContextValuesToAnEntityBeingMapped() {
        def nomin = new Nomin(new MapContext([val1: "value 1", val2: "value 2", val3: "3"]))
                .parse("mappings/contextValues.groovy")
        def result = nomin.map(new Object(), ContextValues)
        assert result.val1 == "value 1"
        assert result.val2 == "value 2"
        assert result.val3 == 3
    }
}