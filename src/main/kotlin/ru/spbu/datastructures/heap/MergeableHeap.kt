package ru.spbu.datastructures.heap

/**
 * @author SingularityA
 * A heap supporting merge and meld operations
 * @param K the type of heap keys.
 * @param V the type of heap values.
 */
interface MergeableHeap<K, V>: Heap<K, V> {
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
}