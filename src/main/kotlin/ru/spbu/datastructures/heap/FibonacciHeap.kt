package ru.spbu.datastructures.heap


import kotlin.math.log2

class FibonacciHeap <V> (val comparator: Comparator<Int> = naturalOrder()) {

    private var root: Node<V>? = null
    var keys: MutableSet<Int> = mutableSetOf<Int>()
    var size: Int = 0
        get() = keys.size

    private class Node<V>(var key: Int, var value: V) {
        var parent: Node<V>? = null
        var mark: Boolean = false
        var degree: Int = 0
        var left: Node<V> = this
        var right: Node<V> = this
        var child: Node<V>? = null

        fun addChild(newChild: Node<V>) {
            if (this.child == null) {
                this.child = newChild
            } else
                this.child!!.unionList(newChild)

            newChild.parent = this
            this.degree += 1
        }

        fun unionList(node: Node<V>) {
            val l = this.left
            val r = node.right
            node.right = this
            this.left = node
            l.right = r
            r.left = l
        }

        val listNode: List<Node<V>>
            get() {
                var node = this
                var listNode: MutableList<Node<V>> = mutableListOf()
                while (true) {
                    listNode.add(node)
                    node = node.right
                    if (node == this)
                        break
                }
                return listNode
            }

        fun findNode(key: Int): Node<V>? {
            var findNode: Node<V>? = null
            val listNode = this.listNode
            for (node in listNode) {
                if (node.key == key)
                    return node
                else {
                    if (node.child != null)
                        findNode = node.child!!.findNode(key)
                    if (findNode != null)
                        break
                }
            }
            return findNode
        }
    }

    private val listRoots: List<Node<V>>?
        get() {
            if (this.isEmpty)
                return null
            var listRoots: MutableList<Node<V>> = mutableListOf()
            var node = this.root
            while (true) {
                listRoots.add(node!!)
                node = node.right
                if (node == this.root)
                    break
            }
            return listRoots
        }

    val peek: V?
        get() =
            when (this.isEmpty) {
                true -> null
                false -> this.root!!.value
            }


    val pop: V?
        get() {
            if (this.isEmpty)
                return null
            var result = this.peek
            this.keys.remove(this.root!!.key)
            if (this.root!!.child == null) {
                if (this.root!!.right == this.root) {
                    this.root = null
                    return result
                } else {
                    this.root!!.left.right = this.root!!.right
                    this.root!!.right.left = this.root!!.left
                    this.root = this.root!!.right
                }
            } else {
                if (this.root!!.right == this.root) {
                    this.root = this.root!!.child

                } else {
                    this.root!!.right.left = this.root!!.left
                    this.root!!.left.right = this.root!!.right
                    var tmp = this.root
                    this.root = this.root!!.right

                    this.root!!.unionList(tmp!!.child!!)

                }
                this.listRoots!!.forEach { i -> i.parent = null }
            }
            if (this.root!!.right != this.root) {
                this.consolidate(this.size)
                val listRoots = this.listRoots
                var newRoot = listRoots!![0]
                listRoots.forEach { i -> if (comparator.compare(i.key, newRoot.key) < 0) newRoot = i }
                this.root = newRoot
            }
            return result
    }

    fun push(key: Int, value: V) {
        var list = Node(key, value)
        if (key !in keys)
            this.keys.add(key)
        else {
            println("Элемент с таким ключом существует")
            return
        }
        if (this.isEmpty)
            this.root = list
        else {
            // добавление, если root - единственный узел
            if (this.root!!.left == this) {
                list.left = this.root!!
                list.right = this.root!!
                this.root!!.left = list
                this.root!!.right = list
            }
            // иначе
            else {
                list.left = this.root!!.left
                this.root!!.left.right = list
                list.right = this.root!!
                this.root!!.left = list
            }

            if (comparator.compare(this.root!!.key, list.key) > 0)
                this.root = list
        }
    }

    fun merge(heap: FibonacciHeap<V>): FibonacciHeap<V> {
        var error = 0
        heap.keys.forEach { i -> if (i in this.keys) error += 1 }
        if (error > 0) {
            println("Ошибка. Есть совпадающие ключи")
            return this
        }
        this.keys.addAll(heap.keys)
        if (this.isEmpty) {
            this.root = heap.root
            //this.size = heap.size
            heap.clear
            return this
        }
        if (heap.isEmpty)
            return this
        // объединение списков корней
        this.root!!.unionList(heap.root!!)
        // назначение нового минимума
        if (comparator.compare(this.root!!.key, heap.root!!.key) > 0)
            this.root = heap.root
        heap.clear
        return this
    }

    val clear: Unit
        get() {
            this.root = null
            this.keys.clear()
        }

    val isEmpty: Boolean
        get() = this.root == null

    fun consolidate(N: Int) {
        val a: MutableList<Node<V>?> = mutableListOf()
        var n = log2(N.toDouble()).toInt()
        for (i in 0..n)
            a.add(null)
        var current: Node<V> = this.root!!
        var index: Int

        while (true) {
            index = current.degree
            if (a[index] == current)
                break
            if (a[index] == null) {
                a[index] = current
                current = current.right
                continue
            } else {
                if (comparator.compare(a[index]!!.key, current.key) < 0) {
                    var tmp = current
                    current = a[index]!!
                    a[index] = tmp

                    if (tmp.left == current) {
                        tmp.right.left = current
                        current.right = tmp.right
                    } else {
                        current.right.left = current.left
                        current.left.right = current.right
                        tmp.left.right = current
                        tmp.right.left = current
                        current.left = tmp.left
                        current.right = tmp.right
                    }
                }

                a[index]!!.left.right = a[index]!!.right
                a[index]!!.right.left = a[index]!!.left
                a[index]!!.left = a[index]!!
                a[index]!!.right = a[index]!!

                current.addChild(a[index]!!)

                a[index] = null
            }
        }

        var newRoot: Node<V>? = null
        for (node in a) {
            if (node != null) {
                if (newRoot == null)
                    newRoot = node
                else
                    if (comparator.compare(node.key, newRoot.key) < 0)
                        newRoot = node
            }
        }
        this.root = newRoot
    }

    fun decreaseKey(oldKey: Int, newKey: Int): Boolean {
        if (oldKey !in this.keys) {
            println("Not find old key")
            return false
        }
        if (newKey in this.keys) {
            println("New key in this.keys")
            return false
        }
        this.keys.remove(oldKey)
        this.keys.add(newKey)
        val findNode = this.root!!.findNode(oldKey)
        findNode!!.key = newKey
        if (findNode.parent != null) {
            if (comparator.compare(findNode.key, findNode.parent!!.key) > 0) {
                println("New key < parent.key")
                return false
            }
            else {
                findNode.parent!!.mark = true
                findNode.mark = true
                // если ссылка от родителя на искомый элемент
                if (findNode.parent!!.child == findNode) {
                    if (findNode.right == findNode)
                        findNode.parent!!.child = null
                    else {
                        findNode.parent!!.child = findNode.right
                        findNode.left.right = findNode.right
                        findNode.right.left = findNode.left
                    }
                }
                // иначе
                else {
                    findNode.left.right = findNode.right
                    findNode.right.left = findNode.left
                }

                findNode.parent = null
                findNode.right = findNode
                findNode.left = findNode
                this.root!!.unionList(findNode)
                var listChild = this.root!!.child!!.listNode

                var newDegree = 0
                for (child in listChild) {
                    if (child.degree > newDegree)
                        newDegree = child.degree
                }
                this.root!!.degree = newDegree
            }
        }
        if (comparator.compare(findNode.key, this.root!!.key) < 0)
            this.root = findNode
        return true
    }

    fun delete(key: Int): V? {
        var newKey = this.root!!.key
        if (comparator.compare(newKey, key) < 0)
            newKey -= 1
        else
            newKey += 1
        if (this.decreaseKey(key, newKey))
            return this.pop
        else
            return null
    }
}