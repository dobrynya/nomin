package org.nomin.entity;

import java.text.MessageFormat;
import static java.text.MessageFormat.*;

/**
 * Document please.
 * @author Dmitry Dobrynin
 *         Created 22.04.2010 16:58:37
 */
public class Education {
    private String name;
    private String description;

    public Education() {}

    public Education(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return format("Education [name = {0} description = {1}]", name, description);
    }
}
