package org.nomin.core;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import org.nomin.core.preprocessing.Preprocessing;

import static java.text.MessageFormat.*;

/**
 * Provides access to an indexed element of collections or arrays.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 22:53:37
 */
@SuppressWarnings({"unchecked"})
public class SeqRuleElem extends RuleElem {
    final Integer index;

    public SeqRuleElem(TypeInfo typeInfo, Integer index) {
        super(typeInfo);
        this.index = index;
    }

    protected Object initialize(Object instance, Object value) {
        try {
            Object result = typeInfo.determineTypeDynamically(value).newInstance();
            setListItem((List) instance, result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Could not instantiate a " + typeInfo.determineType().getSimpleName() + "!", e);
        }
    }

    protected Object retrieve(Object instance) {
        if (instance instanceof List) return getListItem((List) instance);
        else if (instance instanceof Collection) return getCollectionItem((Collection) instance);
        else if (instance instanceof Object[]) return getArrayItem((Object[]) instance);
        return null;
    }

    private Object getArrayItem(Object[] array) {
        return array.length > index ? array[index] : null;
    }

    private Object getCollectionItem(Collection collection) {
        int i = 0;
        for (Object o : collection) { if (i == index) return o; }
        return null;
    }

    protected Object getListItem(List list) {
        if (list.size() > index) return list.get(index);
        return null;
    }

    protected void setListItem(List list, Object item) {
        if (list.size() - 1 < index) for (int i = 0; i < index - list.size(); i++) list.add(null);
        list.add(item);
    }

    protected void store(Object instance, Object value, Preprocessing preprocessing) {
        if (instance instanceof List) setListItem((List) instance,
                preprocessing != null ? preprocessing.preprocess(value, null) : value);
        else throw new NominException(instance.getClass() + " is not supported yet!");
    }

    public String toString() { return format("[{0}]:{1}", index, typeInfo); }
}
