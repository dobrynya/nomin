package org.nomin.core;

import org.nomin.core.preprocessing.Preprocessing;
import static java.text.MessageFormat.format;

/**
 * Processes getting and setting properties, invoking methods and other related operations.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 20:33:25
 */
public abstract class RuleElem {
    protected RuleElem next;
    protected TypeInfo typeInfo;

    protected RuleElem(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    protected abstract Object retrieve(Object instance);

    protected abstract void store(Object instance, Object value, Preprocessing preprocessing);

    protected Object initialize(Object instance, Object value) {
        // value can be analyzed for some reason, f.i. to create an array with specified size
        return instance;
    }

    public Object get(Object instance) {
        Object result = retrieve(instance);
        return result != null && next != null ? next.get(result) : result;
    }

    public void set(Object instance, Object value, Preprocessing preprocessing) {
        if (next != null) {
            Object current = retrieve(instance);
            if (current == null) current = initialize(instance, value);
            next.set(current, value, preprocessing);
        } else
            store(instance, value, preprocessing);
    }

    public String path() { return next != null ? format("{0}->{1}", this, next.path()) : this.toString(); }
}
