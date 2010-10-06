package org.nomin.entity;

/**
 * Represents a business entity.
 * @author Dmitry Dobrynin
 *         Created 16.04.2010 18:25:06
 */
public class Kid {

    private String kidName;

    public Kid() {}

    public Kid(String kidName) {
        this.kidName = kidName;
    }

    public String getKidName() {
        return kidName;
    }

    public void setKidName(String kidName) {
        this.kidName = kidName;
    }

    public String toString() {
        return "Kid [kidName = " + kidName + "]";
    }
}
