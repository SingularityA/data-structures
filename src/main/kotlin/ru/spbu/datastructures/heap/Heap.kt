package ru.spbu.datastructures.heap

import ru.spbu.datastructures.BasicCollection

/**
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
     * Removes the pair with the [key].

     * @return the value associated with the [key], or `null` if there is no such key or the heap is empty.
     */
    fun remove(key: K): V?

    /**
     * Joins two heaps to form a valid new heap containing all the elements of both, preserving the original heaps.
     *
     * @return new heap containing elements of this heap and [heap].
     */
    fun merge(heap: Heap<K, V>): Heap<K, V>

    /**
     * Joins two heaps to form a valid new heap containing all the elements of both, destroying the original heaps.
     * (Mutates this and [heap]).
     *
     * @return this
     */
    fun meld(heap: Heap<K, V>): Heap<K, V>

    /**
     * Changes the key of the element from [oldKey] to [newKey].
     * The [newKey] is assumed to be higher than the [oldKey].
     *
     * @return the assigned [newKey], or `null` if the [oldKey] was not present in the heap.
     */
    fun prioritize(oldKey: K, newKey: K): K?
}