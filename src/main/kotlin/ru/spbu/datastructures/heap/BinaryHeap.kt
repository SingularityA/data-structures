package ru.spbu.datastructures.heap

import kotlin.collections.ArrayList

class BinaryHeap<K : Comparable<K>, V>(
    private val comparator: Comparator<K> = naturalOrder()
) : Heap<K, V> {

    override val keys: MutableList<K> = ArrayList()

    override val values: MutableList<V> = ArrayList()

    override val size: Int
        get() = keys.size

    private val lastIndex: Int
        get() = keys.lastIndex

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
        var index = i
        while (comparator.compare(keys[index], keys[parent(index)]) < 0) {
            swap(index, parent(index))
            index = parent(index)
        }
    }

    private fun siftDown(i: Int) {
        var index = i
        var chosen: Int
        while (left(index) < size) {
            chosen = left(index)

            if (right(index) < size && comparator.compare(keys[right(index)], keys[left(index)]) < 0)
                chosen = right(index)

            if (comparator.compare(keys[index], keys[chosen]) <= 0)
                break

            swap(index, chosen)
            index = chosen
        }
    }

    private fun swap(i: Int, j: Int) {
        keys[i] = keys[j].also { keys[j] = keys[i] }
        values[i] = values[j].also { values[j] = values[i] }
    }

    private fun parent(i: Int) = (i - 1) / 2

    private fun left(i: Int) = 2 * i + 1

    private fun right(i: Int) = 2 * i + 2

    override fun toString(): String {
        if (isEmpty()) return "[]"
        return "[ ${(0..lastIndex).joinToString { index -> "${keys[index]} = ${values[index]}" }} ]"
    }
}