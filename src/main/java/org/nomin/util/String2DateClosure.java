package org.nomin.util;

import java.text.ParseException;
import groovy.lang.Closure;
import org.apache.commons.lang.time.FastDateFormat;
import org.nomin.core.NominException;

/**
 * Performs string to date conversion.
 * @author Dmitry Dobrynin
 *         Created 26.05.2010 12:04:15
 */
public class String2DateClosure extends Closure {
    private FastDateFormat fdf;

    public String2DateClosure(FastDateFormat fdf) {
        super(null, null);
        this.fdf = fdf;
    }

    public Object call(Object[] args) {
        try { return fdf.parseObject((String) args[0]); } catch (ParseException e) {
            throw new NominException("Could not parse a date from " + args[0] + "!");
        }
    }
}
