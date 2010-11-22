package org.nomin.core;

import org.nomin.core.preprocessing.*;
import org.nomin.util.*;
import static java.text.MessageFormat.format;

/**
 * Processes getting and setting properties, invoking methods and other related operations.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 20:33:25
 */
public abstract class RuleElem {
    protected RuleElem next;
    protected TypeInfo typeInfo;
    protected Preprocessing[] preprocessings;
    protected ContainerHelper containerHelper;

    protected RuleElem(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
        containerHelper = ContainerHelper.create(typeInfo);
    }

    public ContainerHelper getContainerHelper() { return containerHelper; }

    public void setPreprocessings(Preprocessing[] preprocessings) { this.preprocessings = preprocessings; }

    public String path() { return next != null ? format("{0}->{1}", this, next.path()) : toString(); }

    public abstract Object get(Object instance) throws Exception;

    public abstract Object set(Object instance, Object value) throws Exception;
}
