package org.nomin.mappings

import org.nomin.Mapping

import org.nomin.core.Array
import org.nomin.entity.Zoo
import org.nomin.entity.AnimalContainer
import org.nomin.entity.Rhino
import org.nomin.entity.Crocodile

/**
 * Just a mapping.
 * @author Dmitry Dobrynin
 * Created: 13.05.2010 0:37:45
 */
class Zoo2AnimalContainer extends Mapping {
  protected void build() {
    mappingFor a: Zoo, b: AnimalContainer
    a.favorite = b.wildAnimals[0]
    hint a: { Class.forName("org.nomin.entity.${it.kind}") }
    a.animals = b.wildAnimals
    hint a: List[{ Class.forName("org.nomin.entity.${it.kind}") }]
    a.animalArray = b.wildAnimals
    hint a: Array[{ Class.forName("org.nomin.entity.${it.kind}") }]
    a.theWorst.name = "The worst"
    hint a: { b.wildAnimals && b.wildAnimals.size() == 3 ? Crocodile : Rhino }
  }
}
