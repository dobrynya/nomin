package org.nomin.util;

import java.text.*;
import java.util.Date;
import groovy.lang.Closure;
import org.nomin.core.NominException;
import static java.text.MessageFormat.format;

/**
 * Performs conversions between string and date.
 * @author Dmitry Dobrynin
 *         Created 26.05.2010 12:04:15
 */
public class String2DateConversion extends Closure {
    private SimpleDateFormat sdf;

    public String2DateConversion(SimpleDateFormat sdf) {
        super(null, null);
        this.sdf = sdf;
    }

    public Object call(Object[] args) {
        if (Date.class.isInstance(args[0])) return sdf.format(args[0]);
        else if (String.class.isInstance(args[0])) try {
            return sdf.parse((String) args[0]);
        } catch (ParseException e) {
            throw new NominException(format("Could not parse a date from {0}!", args[0]), e);
        }
        return null;
    }
}
