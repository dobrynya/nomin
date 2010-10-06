package org.nomin.entity;

import java.util.List;

/**
 * Documentation.
 *
 * @author Dmitry Dobrynin
 *         Created: 13.05.2010 0:35:05
 */
public class AnimalContainer {
    private List<WildAnimal> wildAnimals;

    public List<WildAnimal> getWildAnimals() {
        return wildAnimals;
    }

    public void setWildAnimals(List<WildAnimal> wildAnimals) {
        this.wildAnimals = wildAnimals;
    }
}
