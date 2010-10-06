package org.nomin.core.preprocessing;

import org.apache.commons.beanutils.ConvertUtils;

/**
 * Document please.
 * @author Dmitry Dobrynin
 *         Created 28.04.2010 15:41:32
 */
public class ConvertUtilsPreprocessing implements Preprocessing {
    private Class<?> targetClass;

    public ConvertUtilsPreprocessing(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Object preprocess(Object source, Object target) {
        return source != null ? ConvertUtils.convert(source, targetClass) : null;
    }
}
