package ru.spbu.datastructures

interface BasicCollection<E> {

    /**
     * Returns the size of the collection.
     */
    val size: Int

    /**
     * Returns `true` if the collection is empty (contains no elements), `false` otherwise.
     */
    fun isEmpty(): Boolean

    /**
     * Removes all elements from this collection.
     */
    fun clear(): Unit
}