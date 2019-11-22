package ru.spbu.datastructures.heap

import kotlin.math.log2
import kotlin.math.pow

/**
 * @author SingularityA
 */
class TreeBinaryHeap<K : Comparable<K>, V>(
    private val comparator: Comparator<K> = naturalOrder()
) : Heap<K, V> {

    private var head: Node<K, V>? = null

    override val keys: List<K>
        get() = traverse().map { node -> node.key }

    override val values: List<V>
        get() = traverse().map { node -> node.value }

    override var size: Int = 0
        private set

    private val last: Node<K, V>?
        get() = if (isEmpty()) null else getElement(size - 1)

    constructor(
        keys: Collection<K>,
        values: Collection<V>,
        comparator: Comparator<K> = naturalOrder()
    ) : this(comparator) {

        require(keys.size == values.size) { "Keys and values collections must have equal size" }
        if (keys.isNotEmpty()) {
            (keys zip values).map { pair -> pushRaw(pair.first, pair.second) }
            heapify()
        }
    }

    private fun pushRaw(key: K, value: V) {
        val node = Node(key, value)
        if (isEmpty()) head = node
        else {
            val parent = getElement((size - 1) / 2)
            node.parent = parent
            if (parent.left == null) parent.left = node
            else parent.right = node
        }
        size += 1
    }

    override fun peek(): V? = head?.value

    override fun pop(): V? {
        if (isEmpty()) return null
        val value = head!!.value

        if (size == 1) {
            head = null
        } else {
            val tail = last!!
            swapContent(head!!, tail)
            abandon(tail)
            siftDown(head!!)
        }
        size--
        return value
    }

    override fun push(key: K, value: V) {
        val node = Node(key, value)
        if (isEmpty()) head = node
        else {
            val parent = getElement((size - 1) / 2)
            node.parent = parent
            if (parent.left == null) parent.left = node
            else parent.right = node
        }
        size += 1
        siftUp(node)
    }

    override fun replace(key: K, value: V): V? {
        return if (isEmpty()) {
            head = Node(key, value)
            null
        } else {
            val oldValue = head!!.value
            head!!.key = key
            head!!.value = value

            siftDown(head!!)
            oldValue
        }
    }

    override fun remove(value: V): V? {
        val node = find { node -> node.value == value } ?: return null

        if (size == 1) {
            head = null
        } else {
            val tail = last!!
            swapContent(node, tail)
            abandon(tail)
            siftDown(node)
        }
        size--
        return value
    }

    override fun prioritize(oldKey: K, newKey: K): K? {
        require(comparator.compare(oldKey, newKey) >= 0) { "New key must be higher than old key" }
        val node = find { node -> node.key == oldKey } ?: return null

        node.key = newKey
        siftUp(node)

        return newKey
    }

    override fun isEmpty() = head == null

    override fun clear() {
        head = null
        size = 0
    }

    override fun toString(): String {
        if (isEmpty()) return "[]"
        return "[ ${
        (0 until size)
            .map { index -> getElement(index) }
            .joinToString { node -> "${node.key} = ${node.value}" }
        } ]"
    }

    private fun getElement(index: Int): Node<K, V> {
        require(index < size) { "Index must be lesser than size" }
        val level = log2(index.toFloat() + 1).toInt()
        var elemsOnLevel = 2.0.pow(level).toInt()
        var indexOnLevel = index - (elemsOnLevel - 1)
        var node = head!!

        for (currentLevel in 0 until level) {
            elemsOnLevel /= 2
            if (indexOnLevel < elemsOnLevel)
                node = node.left!!
            else {
                node = node.right!!
                indexOnLevel -= elemsOnLevel
            }
        }
        return node
    }

    private fun abandon(tail: Node<K, V>) {
        if (size % 2 == 1)
            tail.parent!!.right = null
        else
            tail.parent!!.left = null
        tail.parent = null
    }

    private fun siftUp(node: Node<K, V>) {
        if (size < 2) return

        var child = node
        while (child.parent != null && comparator.compare(child.key, child.parent?.key) < 0) {
            swapContent(child, child.parent!!)
            child = child.parent!!
        }
    }

    private fun siftDown(node: Node<K, V>) {
        if (size < 2) return

        var parent = node
        var chosen: Node<K, V>

        while (parent.left != null) {
            chosen = if (parent.right != null && comparator.compare(parent.right!!.key, parent.left!!.key) < 0)
                parent.right!!
            else
                parent.left!!

            if (comparator.compare(parent.key, chosen.key) < 0)
                break

            swapContent(parent, chosen)
            parent = chosen
        }
    }

    private fun swapContent(node1: Node<K, V>, node2: Node<K, V>) {
        node1.key = node2.key.also { node2.key = node1.key }
        node1.value = node2.value.also { node2.value = node1.value }
    }

    private fun heapify() = heapify(head!!)

    private fun heapify(node: Node<K, V>?) {
        if (node != null) {
            siftDown(node)
            heapify(node.left)
            heapify(node.right)
        }
    }

    private fun traverse(): List<Node<K, V>> {
        val elements = mutableListOf<Node<K, V>>()
        traverse(head, elements)
        return elements
    }

    private fun traverse(node: Node<K, V>?, elements: MutableList<Node<K, V>>) {
        if (node != null) {
            elements.add(node)
            traverse(node.left, elements)
            traverse(node.right, elements)
        }
    }

    private fun find(predicate: (Node<K, V>) -> (Boolean)): Node<K, V>? {
        val result = mutableListOf<Node<K, V>>()
        find(predicate, head, result)
        return if (result.isEmpty()) null else result[0]
    }

    private fun find(predicate: (Node<K, V>) -> (Boolean), node: Node<K, V>?, result: MutableList<Node<K, V>>) {
        if (node == null) return
        if (predicate.invoke(node)) {
            result.add(node)
            return
        }
        find(predicate, node.left, result)
        find(predicate, node.right, result)
    }
}

private class Node<K : Comparable<K>, V>(
    var key: K,
    var value: V,
    var parent: Node<K, V>? = null,
    var left: Node<K, V>? = null,
    var right: Node<K, V>? = null
)