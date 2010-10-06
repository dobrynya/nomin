package org.nomin.entity;

import java.text.MessageFormat;
import java.util.*;

/**
 * Represents a business entity.
 * @author Dmitry Dobrynin
 *         Created 14.04.2010 16:31:03
 */
public class LegacyDetails {
    private Date birthday;
    private Boolean sex; // identical to Details.sex
    private Object[] children;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean isSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Object[] getChildren() {
        return children;
    }

    public void setChildren(Object[] children) {
        this.children = children;
    }

    public String toString() {
        return MessageFormat.format("LegacyDetails [ birthday = {0} sex = {1} children = {2}]", birthday, sex, children);
    }
}
