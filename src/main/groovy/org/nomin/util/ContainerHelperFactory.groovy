package org.nomin.util

/**
 * Creates an appropriate ContainerHelper instance.
 * @author Dmitry Dobrynin
 * Date: 25.10.2010 Time: 21:47:18
 */
class ContainerHelperFactory {
  static ContainerHelper create(TypeInfo typeInfo) {
    typeInfo.array ? new ArrayHelper(typeInfo) :
      typeInfo.collection ? new CollectionHelper(typeInfo) :
        typeInfo.map ? new MapHelper(typeInfo) : null
  }
}
