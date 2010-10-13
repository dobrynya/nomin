package org.nomin.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.nomin.core.preprocessing.Preprocessing;
import org.nomin.util.TypeInfo;

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
            if (instance instanceof List) setListItem((List) instance, result);
            else if (instance instanceof Collection) setCollectionItem((Collection) instance, result);
            return result;
        } catch (Exception e) {
            throw new NominException(format("Could not instantiate a {0}!", typeInfo.determineType().getSimpleName()), e);
        }
    }

    protected Object retrieve(Object instance) {
        if (instance instanceof List) return getListItem((List) instance);
        else if (instance instanceof Collection) return getCollectionItem((Collection) instance);
        else if (instance instanceof Object[]) return getArrayItem((Object[]) instance);
        return null;
    }

    protected Object getCollectionItem(Collection collection) {
        if (index >= 0 && index < collection.size()) {
            int i = 0;
            for (Object o : collection) { if (i++ == index) return o; }
        }
        return null;
    }

    protected void setCollectionItem(Collection collection, Object value) {
        ArrayList<Object> result = new ArrayList(collection);
        setListItem(result, value);
        collection.clear();
        collection.addAll(result);
    }

    protected Object getListItem(List list) {
        if (index >= 0 && list.size() > index) return list.get(index);
        return null;
    }

    protected void setListItem(List list, Object item) {
        if (index >= 0 && list.size() - 1 < index) for (int i = 0; i < index - list.size(); i++) list.add(null);
        list.add(item);
    }

    protected Object getArrayItem(Object[] array) { return array.length > index ? array[index] : null; }

    protected void store(Object instance, Object value, Preprocessing preprocessing) {
        if (instance instanceof List) setListItem((List) instance,
                preprocessing != null ? preprocessing.preprocess(value, null) : value);
        else if (instance instanceof Collection) setCollectionItem((Collection) instance,
                preprocessing != null ? preprocessing.preprocess(value, null) : value);
    }

    public String toString() { return format("[{0}]:{1}", index, typeInfo); }
}
