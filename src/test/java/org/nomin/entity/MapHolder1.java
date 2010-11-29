package org.nomin.entity;

import java.util.Map;

/**
 * An entity.
 * @author Dmitry Dobrynin
 *         Date: 08.11.2010 Time: 17:57:19
 */
public class MapHolder1 {
    private Map<String, String> strings;
    private Map<String, Person> persons;
    private Object objects;
    private Map<String, String> persons2;
    private Map<String, String> strings2;

    public Map<String, String> getStrings() { return strings; }

    public void setStrings(Map<String, String> strings) { this.strings = strings; }

    public Map<String, Person> getPersons() { return persons; }

    public void setPersons(Map<String, Person> persons) { this.persons = persons; }

    public Object getObjects() { return objects; }

    public void setObjects(Object objects) { this.objects = objects; }

    public Map<String, String> getPersons2() { return persons2; }

    public void setPersons2(Map<String, String> persons2) { this.persons2 = persons2; }

    public Map<String, String> getStrings2() { return strings2; }

    public void setStrings2(Map<String, String> strings2) { this.strings2 = strings2; }
}
