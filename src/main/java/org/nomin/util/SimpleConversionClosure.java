package org.nomin.util;

import java.util.Map;
import groovy.lang.Closure;

/**
 * Document please.
 * @author Dmitry Dobrynin
 *         Created 26.05.2010 12:44:25
 */
public class SimpleConversionClosure extends Closure {
    private Map<Object, Object> variants;

    public SimpleConversionClosure(Map<Object, Object> variants) {
        super(null, null);
        this.variants = variants;
    }

    public Object call(Object[] args) { return variants.get(args[0]); }
}
