package org.nomin.entity;

import java.util.List;

/**
 * Represents a business entity.
 * @author Dmitry Dobrynin
 *         Created: 13.05.2010 0:29:55
 */
public class Zoo {
    private Animal favorite;

    private List<Animal> animals;

    private Object[] animalArray;

    private Animal theWorst;

    public Animal getFavorite() {
        return favorite;
    }

    public void setFavorite(Animal favorite) {
        this.favorite = favorite;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public Object[] getAnimalArray() {
        return animalArray;
    }

    public void setAnimalArray(Object[] animalArray) {
        this.animalArray = animalArray;
    }

    public Animal getTheWorst() {
        return theWorst;
    }

    public void setTheWorst(Animal theWorst) {
        this.theWorst = theWorst;
    }
}
