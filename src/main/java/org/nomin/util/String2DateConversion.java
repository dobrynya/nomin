package org.nomin.util;

import java.text.*;
import java.util.Date;
import groovy.lang.Closure;
import org.nomin.core.NominException;
import org.nomin.util.TypeInfo;
import static java.text.MessageFormat.format;

/**
 * Performs conversions between string and date.
 * @author Dmitry Dobrynin
 *         Created 26.05.2010 12:04:15
 */
public class String2DateConversion extends Closure {
    private SimpleDateFormat sdf;
    private TypeInfo typeInfoFrom;
    private TypeInfo typeInfoTo;

    public String2DateConversion(SimpleDateFormat sdf, TypeInfo typeInfoFrom, TypeInfo typeInfoTo) {
        super(null, null);
        this.sdf = sdf;
        this.typeInfoFrom = typeInfoFrom;
        this.typeInfoTo = typeInfoTo;
    }

    public Object call(Object[] args) {
        Object value = args[0];
        if (value instanceof String) {
            Date date;
            try {
                date = sdf.parse((String) value);
            } catch (ParseException e) {
                throw new NominException(format("Could not parse a date from {0}!", args[0]), e);
            }

            Class<?> targetType = determineDateType(date);
            if (date == null || targetType.isInstance(date)) {
                return date;
            }
            return Converters.convert(date, targetType);
        } else {
            Date date;
            if (value == null) {
                return null;
            } else if (value instanceof Date) {
                date = (Date) value;
            } else {
                date = (Date) Converters.convert(value, Date.class);
            }

            return sdf.format(date);
        }
    }

    private Class<?> determineDateType(Object value) {
        Class<?> typeFrom = typeInfoFrom.determineTypeDynamically(value);
        Class<?> typeTo = typeInfoTo.determineTypeDynamically(value);
        return String.class.isAssignableFrom(typeFrom) || Object.class.equals(typeFrom) ? typeTo : typeFrom;
    }
}
