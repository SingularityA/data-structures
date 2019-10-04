package ru.spbu.datastructures.heap

import kotlin.collections.ArrayList

class BinaryHeap<K: Comparable<K>, V>(
    val comparator: Comparator<K> = naturalOrder()
) : Heap<K, V> {

    private val keys: MutableList<K> = ArrayList()

    private val values: MutableList<V> = ArrayList()

    private val lastIndex: Int
        get() = keys.lastIndex

    override val size: Int
        get() = keys.size

    override fun peek(): V? {
        return if (isEmpty())
            null
        else
            values.first()
    }

    override fun pop(): V? {
        if (isEmpty()) return null

        val value = values.first()
        swap(0, lastIndex)

        keys.removeAt(keys.lastIndex)
        values.removeAt(values.lastIndex)
        siftDown(0)

        return value
    }

    override fun push(key: K, value: V) {
        keys.add(key)
        values.add(value)
        siftUp(lastIndex)
    }

    override fun replace(key: K, value: V): V? {
        val oldValue = if (isEmpty()) null else values.first()

        keys[0] = key
        values[0] = value
        siftDown(0)

        return oldValue
    }

    override fun remove(key: K): V? {
        if (isEmpty()) return null

        val index = keys.indexOf(key)

        // see where to sift - up or down
        TODO("not implemented")
    }

    override fun merge(heap: Heap<K, V>): Heap<K, V> {
        TODO("not implemented")
    }

    override fun meld(heap: Heap<K, V>): Heap<K, V> {
        TODO("not implemented")
    }

    override fun prioritize(oldKey: K, newKey: K): K? {
        TODO("not implemented")
    }

    override fun isEmpty() = keys.isEmpty()

    override fun clear() {
        keys.clear()
        values.clear()
    }

    private fun siftUp(i: Int) {
        TODO("not implemented")
    }

    private fun siftDown(i: Int) {
        TODO("not implemented")
    }

    private fun swap(i: Int, j: Int) {
        keys[i] = keys[j].also { keys[j] = keys[i] }
        values[i] = values[j].also { values[j] = values[i] }
    }
}