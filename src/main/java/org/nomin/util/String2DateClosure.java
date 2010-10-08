package org.nomin.util;

import java.text.*;
import groovy.lang.Closure;
import org.nomin.core.NominException;

/**
 * Performs string to date conversion.
 * @author Dmitry Dobrynin
 *         Created 26.05.2010 12:04:15
 */
public class String2DateClosure extends Closure {
    private SimpleDateFormat sdf;

    public String2DateClosure(SimpleDateFormat sdf) {
        super(null, null);
        this.sdf = sdf;
    }

    public Object call(Object[] args) {
        try { return sdf.parseObject((String) args[0]); } catch (ParseException e) {
            throw new NominException("Could not parse a date from " + args[0] + "!");
        }
    }
}
