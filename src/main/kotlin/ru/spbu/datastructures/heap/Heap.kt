package ru.spbu.datastructures.heap

import ru.spbu.datastructures.BasicCollection

/**
 * @author SingularityA
 * A modifiable collection that holds pairs of objects (keys and values)
 * and supports efficiently retrieving the top value of collection.
 * @param K the type of heap keys.
 * @param V the type of heap values.
 */
interface Heap<K, V>: BasicCollection<K> {

    /**
     * Returns a read-only [List] of all keys in this heap.
     */
    val keys: List<K>

    /**
     * Returns a read-only [List] of all values in this heap.
     */
    val values: List<V>

    /**
     * Returns the value of the key on top of the heap,
     * does not remove the key and its corresponding value from the heap.
     *
     * @return the value associated with the top key, or `null` if the heap is empty.
     */
    fun peek(): V?

    /**
     * Extracts the value of the key on top of the heap,
     * removing the key and its corresponding value from the heap.
     *
     * @return the value associated with the top key, or `null` if the heap is empty.
     */
    fun pop(): V?

    /**
     * Adds new [key] and [value] associated with it to the heap.
     */
    fun push(key: K, value: V)

    /**
     * Pops the top element and puts new pair of [key] and [value].
     * More efficient than pop followed by put, since only need to balance once.
     * If the heap is empty the pair would still be added to the heap.
     *
     * @return the value associated with the old top key, or `null` if the heap is empty.
     */
    fun replace(key: K, value: V): V?

    /**
     * Removes the pair with the [value] (first appearance).

     * @return the [value] or `null` if there is no pair with such [value].
     */
    fun remove(value: V): V?

    /**
     * Changes the key of the element from [oldKey] to [newKey].
     * The [newKey] is assumed to be higher than the [oldKey].
     *
     * @return the assigned [newKey], or `null` if the [oldKey] was not present in the heap.
     */
    fun prioritize(oldKey: K, newKey: K): K?
}