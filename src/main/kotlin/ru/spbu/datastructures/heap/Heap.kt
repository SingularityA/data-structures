package ru.spbu.datastructures.heap

/**
 * A modifiable collection that holds pairs of objects (keys and values) and supports efficiently retrieving
 * the top value of collection. Heap keys are unique; the heap holds only one value for each key.
 * @param K the type of heap keys.
 * @param V the type of heap values.
 */
interface Heap<K, V>: MutableMap<K, V> {

    /**
     * Returns the value of the key on top of the heap,
     * does not remove the key and its corresponding value from the heap
     *
     * @return the value associated with the top key, or `null` if the heap is empty.
     */
    fun peek(): V?

    /**
     * Extracts the value of the key on top of the heap,
     * removing the key and its corresponding value from the heap
     *
     * @return the value associated with the top key, or `null` if the heap is empty.
     */
    fun pop(): V?

    /**
     * Pops the top element and puts the new pair of [key] and [value]
     * More efficient than pop followed by put, since only need to balance once
     *
     * @return the value associated with the top key, or `null` if the heap is empty.
     */
    fun replace(key: K, value: V): V?

    /**
     * Joins two heaps to form a valid new heap containing all the elements of both, preserving the original heaps.
     *
     * @return the new heap containing elements of this heap and [heap].
     */
    fun merge(heap: Heap<K, V>): Heap<K, V>

    /**
     * Joins two heaps to form a valid new heap containing all the elements of both, destroying the original heaps.
     * (Mutates this and [heap])
     *
     * @return this
     */
    fun meld(heap: Heap<K, V>): Heap<K, V>

    /**
     * Changes the key of the element from [oldKey] to [newKey].
     * The [newKey] is assumed to be higher than the [oldKey]
     *
     * @return the assigned [newKey], or `null` if the [oldKey] was not present in the heap.
     */
    fun prioritize(oldKey: K, newKey: K): K?
}