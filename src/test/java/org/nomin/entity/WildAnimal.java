package org.nomin.entity;

/**
 * Represents a business entity.
 * @author Dmitry Dobrynin
 *         Created: 13.05.2010 0:35:52
 */
public class WildAnimal {
    private String name;
    private String kind;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
