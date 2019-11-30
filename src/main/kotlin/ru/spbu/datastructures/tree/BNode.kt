package ru.spbu.datastructures.tree

abstract class BNode<K : Comparable<K>, V> {
    abstract val keys: MutableCollection<K>
    abstract val values: MutableCollection<V>

    abstract var parent: BNode<K, V>?
    abstract val children: MutableCollection<BNode<K, V>>

    val size: Int
        get() = keys.size

    fun isLeaf() = children.isEmpty()
    fun isNotLeaf() = children.isNotEmpty()

    fun add(key: K, value: V) {
        keys.add(key)
        values.add(value)
    }

    fun clear() {
        keys.clear()
        values.clear()
        parent = null
        children.clear()
    }
}

class ArrayListBNode<K : Comparable<K>, V> : BNode<K, V> {
    override val keys: MutableList<K>
    override val values: MutableList<V>

    override var parent: BNode<K, V>?
    override val children: MutableList<BNode<K, V>>

    constructor() {
        keys = mutableListOf()
        values = mutableListOf()
        parent = null
        children = mutableListOf()
    }

    constructor(keys: Collection<K>,
                values: Collection<V>,
                children: Collection<BNode<K, V>>,
                parent: BNode<K, V>? = null): super() {
        this.keys = ArrayList(keys)
        this.values = ArrayList(values)
        this.children = ArrayList(children)
        this.parent = parent
    }

    fun insert(key: K, value: V): Int {
        var index = keys.binarySearch(key)
        if (index < 0) index = -index - 1
        keys.add(index, key)
        values.add(index, value)
        return index
    }
}