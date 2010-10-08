package org.nomin.util;

import groovy.lang.Closure;

import java.text.SimpleDateFormat;

/**
 * Performs date to string conversion.
 * @author Dmitry Dobrynin
 *         Created 26.05.2010 12:04:15
 */
public class Date2StringClosure extends Closure {
    private SimpleDateFormat sdf;

    public Date2StringClosure(SimpleDateFormat sdf) {
        super(null, null);
        this.sdf = sdf;
    }

    public Object call(Object[] args) { return sdf.format(args[0]); }
}