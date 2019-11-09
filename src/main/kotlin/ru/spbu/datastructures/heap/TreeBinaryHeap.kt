package ru.spbu.datastructures.heap

import kotlin.math.log2

class TreeBinaryHeap<K : Comparable<K>, V>(
    private val comparator: Comparator<K> = naturalOrder()
) : Heap<K, V> {

    private var head: Node<K, V>? = null

    override val keys: List<K>
        get() = TODO("not implemented")

    override val values: List<V>
        get() = TODO("not implemented")

    override var size: Int = 0
        private set

    private val last: Node<K, V>?
        get() = if (isEmpty()) null else getElement(size - 1)

    override fun peek(): V? = head?.value

    override fun pop(): V? {
        if (isEmpty()) return null
        val value = head!!.value

        if (size == 1) {
            head = null
        } else {
            val tail = last!!
            swapContent(head!!, tail)
            if (size % 2 == 1)
                tail.parent!!.right = null
            else
                tail.parent!!.left = null
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
            val oldValue = head?.value
            head?.key = key
            head?.value = value

            siftDown(head!!)
            oldValue
        }
    }

    override fun remove(key: K): V? {
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

    override fun isEmpty() = head == null

    override fun clear() {
        head = null
        size = 0
    }

    fun print() = if (isEmpty()) println("[]") else printNode(head)

    private fun printNode(node: Node<K, V>?) {
        if (node != null) {
            println("(key = ${node.key}, value = ${node.value})\n")
            printNode(node.left)
            printNode(node.right)
        }
    }

    private fun getElement(index: Int): Node<K, V> {
        require(index < size) { "Index must be lesser than size" }
        val level = log2(index.toFloat()).toInt()
        var number = 2.times(level)
        var i = index
        var node = head!!

        while (number > 0) {
            number /= 2
            i -= number
            node = if (i < number)
                node.left!!
            else
                node.right!!
        }

        return node
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
}

private class Node<K : Comparable<K>, V>(
    var key: K,
    var value: V,
    var parent: Node<K, V>? = null,
    var left: Node<K, V>? = null,
    var right: Node<K, V>? = null
)