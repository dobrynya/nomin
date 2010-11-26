package org.nomin.entity;

import java.text.MessageFormat;

/**
 * Just represents a business entity.
 * @author Dmitry Dobrynin
 *         Created 13.04.2010 11:04:19
 */
public class Employee {
    private String name, last, properties;

    private Details details;

    public Employee() {}

    public Employee(String name, String last, String properties, Details details) {
        this.name = name;
        this.last = last;
        this.properties = properties;
        this.details = details;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getLast() { return last; }

    public void setLast(String last) { this.last = last; }

    public String getProperties() { return properties; }

    public void setProperties(String properties) { this.properties = properties; }

    public Details getDetails() { return details; }

    public void setDetails(Details details) { this.details = details; }

    public String toString() {
        return MessageFormat.format("Employee [ name = {0} last = {1} details = {2} ]", name, last, details);
    }
}
