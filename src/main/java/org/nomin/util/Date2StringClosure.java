package org.nomin.util;

import groovy.lang.Closure;
import org.apache.commons.lang.time.FastDateFormat;

/**
 * Performs date to string conversion.
 * @author Dmitry Dobrynin
 *         Created 26.05.2010 12:04:15
 */
public class Date2StringClosure extends Closure {
    private FastDateFormat fdf;

    public Date2StringClosure(FastDateFormat fdf) {
        super(null, null);
        this.fdf = fdf;
    }

    public Object call(Object[] args) {
        return fdf.format(args[0]);
    }
}