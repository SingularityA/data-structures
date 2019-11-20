package ru.spbu.datastructures.heap

class IntArrayBinaryHeap<V>(
    comparator: Comparator<Int> = naturalOrder()
) : ArrayBinaryHeap<Int, V>(comparator) {

    constructor(
        keys: Collection<Int>,
        values: Collection<V>,
        comparator: Comparator<Int> = naturalOrder()
    ) : this(comparator) {

        require(keys.size == values.size) { "Keys and values collections must have equal size" }
        this.keys.addAll(keys)
        this.values.addAll(values)
        heapify()
    }

    override fun prioritize(oldKey: Int, newKey: Int): Int? {
        require(comparator.compare(oldKey, newKey) >= 0) { "New key must be higher than old key" }
        val index = keys.indexOf(oldKey)
        if (index == -1) return null

        keys[index] = newKey
        siftUp(index)

        return newKey
    }

    override fun merge(heap: Heap<Int, V>): Heap<Int, V> {
        val keys = ArrayList(this.keys)
        val values = ArrayList(this.values)

        keys.addAll(heap.keys)
        values.addAll(heap.values)

        return IntArrayBinaryHeap(keys, values, this.comparator)
    }
}