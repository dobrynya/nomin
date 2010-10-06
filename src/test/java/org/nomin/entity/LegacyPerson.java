package org.nomin.entity;

import java.text.MessageFormat;

/**
 * Just represents a legacy business entity.
 * @author Dmitry Dobrynin
 *         Created 14.04.2010 16:29:56
 */
public class LegacyPerson {
    private Object name;
    private Object lastName;
    private Object details;

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    public String toString() {
        return MessageFormat.format("LegacyPerson [name = {0} lastName = {1} details = {2}]", name, lastName, details);
    }
}
