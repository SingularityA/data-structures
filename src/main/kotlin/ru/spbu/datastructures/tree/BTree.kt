package ru.spbu.datastructures.tree

abstract class BTree<K : Comparable<K>, V> {

    abstract val root: BNode<K, V>?
    val degree: Int

    constructor(degree: Int = 3) {
        this.degree = degree
    }

    val size: Int
        get() = TODO("Not implemented")
    val height: Int
        get() = TODO("Not implemented")

    // minimum number of keys in the node
    val minKeys: Int
        get() = degree - 1

    // number of keys triggering node split
    val maxKeys: Int
        get() = 2 * degree - 1

    fun isEmpty() = root == null

    open fun search(key: K): V? = TODO("Not implemented")

    open fun insert(key: K, value: V): V = TODO("Not implemented")

    open fun delete(key: K): V? = TODO("Not implemented")
}

class ArrayListBTree<K : Comparable<K>, V> : BTree<K, V>() {

    override var root: ArrayListBNode<K, V>? = null

    private val median: Int
        get() = minKeys

    override fun search(key: K): V? {
        if (isEmpty()) return null
        var node = root!!
        var index: Int

        while (true) {
            index = node.keys.binarySearch(key)

            if (index >= 0) {
                return node.values[index]
            } else {
                if (node.isLeaf()) return null
                else {
                    index = -index - 1 // insertion point
                    node = node.children[index] as ArrayListBNode<K, V>
                }
            }
        }
    }

    override fun insert(key: K, value: V): V {
        if (isEmpty()) {
            root = ArrayListBNode()
            root!!.add(key, value)
        } else {
            var node = root!!
            var index: Int

            // searching for leaf for insertion
            while (node.isNotLeaf()) {
                index = node.keys.binarySearch(key)
                if (index < 0) index = -index - 1
                node = node.children[index] as ArrayListBNode<K, V>
            }

            // inserting key-value into leaf node
            node.insert(key, value)

            if (node.size == maxKeys)
                split(node)
        }
        return value
    }

    private fun split(node: ArrayListBNode<K, V>) {
        val medianKey = node.keys[median]
        val medianValue = node.values[median]

        val first = ArrayListBNode(
            node.keys.subList(0, median),
            node.values.subList(0, median),
            node.children.subList(0, degree)
        )
        val second = ArrayListBNode(
            node.keys.subList(median + 1, maxKeys),
            node.values.subList(median + 1, maxKeys),
            node.children.subList(degree, 2 * degree)
        )

        if (node.parent == null) {
            root = ArrayListBNode(
                arrayListOf(medianKey),
                arrayListOf(medianValue),
                arrayListOf(first, second)
            )
        } else {
            val parent = node.parent as ArrayListBNode<K, V>
            val index = parent.insert(medianKey, medianValue)

            first.parent = parent
            second.parent = parent
            parent.children[index] = first
            parent.children.add(index + 1, second)
        }

        // по идее не обязательно
        node.clear()
    }
}