package ru.spbu.datastructures.list

class PersistentList<V>(private val p: Int = 2) {

    constructor(listv0: List<V>, p: Int = 2): this(p) {
        var node1: Node<V> = Node(listv0[0])
        node1.listMod.add(Triple(0, Node.Field.LEFT, null))
        listModHead.add(Pair(0, node1))
        var node2: Node<V>
        for (i in 1..listv0.size - 1) {
            node2 = Node(listv0[i])
            node1.listMod.add(Triple(0, Node.Field.RIGHT, node2))
            node2.listMod.add(Triple(0, Node.Field.LEFT, node1))
            node1 = node2
        }
        node1.listMod.add(Triple(0, Node.Field.RIGHT, null))
        listModTail.add(Pair(0, node1))
    }
    var version: Int = 0

    fun size(version: Int = this.version): Int {
            if (this.isEmpty(version))
                return 0
            var newNode: Node<V>? = this.head(version)
            var size = 1
            val thisTail = this.tail(version)
            while (newNode != thisTail) {
                size += 1
                newNode = newNode!!.next(version)
            }
            return size
        }

    fun print(version: Int = this.version) {
        if (this.isEmpty(version))
            println("Список пуст")
        else {
            var newNode: Node<V>? = this.head(version)
            val thisTail = this.tail(version)
            print("[")
            while (newNode != thisTail) {
                print("${newNode!!.value}, ")
                newNode = newNode.next(version)
            }
            println("${newNode!!.value}]")
        }
    }

    val printAllVersions: Unit
        get() {
            //var currentVersion = this.version
            for (v in 0..this.version) {
                print("version ${v} ")
                this.print(v)
            }
        }

    fun isEmpty(version: Int = this.version): Boolean = this.head(version) == null

    private var listModHead: MutableList<Pair<Int, Node<V>?>> = mutableListOf()
    private var listModTail: MutableList<Pair<Int, Node<V>?>> = mutableListOf()

    private fun head(version: Int = this.version): Node<V>? = findNode(version, this.listModHead)
    private fun tail(version: Int = this.version): Node<V>? = findNode(version, this.listModTail)

    private fun findNode(version: Int, listMod: MutableList<Pair<Int, Node<V>?>>): Node<V>? {
        var findNode: Node<V>? = null
        for (mod in listMod)
            if (mod.first <= version)
                findNode = mod.second
        return findNode
    }

    val printModHead
        get() = println(this.listModHead)

    val printModTail
        get() = println(this.listModTail)

    private class Node<V> (val value: V?) {
        var listMod: MutableList<Triple<Int,Field,Node<V>?>> = mutableListOf()

        enum class Field {
            RIGHT, LEFT
        }

        fun next(version: Int): Node<V>? {
            if (this.listMod.isEmpty())
                return null
            var nextNode: Node<V>? = null
            for (mod in this.listMod) {
                if (mod.first <= version && mod.second == Field.RIGHT)
                    nextNode = mod.third
            }
            return nextNode
        }

        fun prev(version: Int): Node<V>? {
            if (this.listMod.isEmpty())
                return null
            var prevNode: Node<V>? = null
            for (mod in this.listMod) {
                if (mod.first <= version && mod.second == Field.LEFT)
                    prevNode = mod.third
            }
            return prevNode
        }

        fun isFull (maxMod: Int): Boolean = this.listMod.size >= maxMod

        fun findMostLeftNotFull(currentVersion: Int, maxMod: Int): Pair<Boolean, Node<V>> {
            var isHeadFull = false
            var currentNode = this.prev(currentVersion)
            var copyNode = Node(this.value)
            while (true) {
                if (currentNode == null) {
                    isHeadFull = true
                    break
                }
                else {
                    if (currentNode.listMod.size < maxMod) {
                        currentNode.listMod.add(Triple(currentVersion + 1, Field.RIGHT, copyNode))
                        copyNode.listMod.add(Triple(currentVersion + 1, Field.LEFT, currentNode))
                        break
                    }
                    else {
                        val tmp = copyNode
                        copyNode = Node(currentNode.value)
                        tmp.listMod.add(Triple(currentVersion + 1, Field.LEFT, copyNode))
                        copyNode.listMod.add(Triple(currentVersion + 1, Field.RIGHT, tmp))
                        currentNode = currentNode.prev(currentVersion)
                    }
                }
            }
            return Pair(isHeadFull,copyNode)
        }

        fun findMostRightNotFull(currentVersion: Int, maxMod: Int): Pair<Boolean, Node<V>> {
            var isTailFull = false
            var currentNode = this.next(currentVersion)
            var copyNode = Node(this.value)
            while (true) {
                if (currentNode == null) {
                    isTailFull = true
                    break
                } else {
                    if (currentNode.listMod.size < maxMod) {
                        currentNode.listMod.add(Triple(currentVersion + 1, Field.LEFT, copyNode))
                        copyNode.listMod.add(Triple(currentVersion + 1, Field.RIGHT, currentNode))
                        break
                    } else {
                        val tmp = copyNode
                        copyNode = Node(currentNode.value)
                        tmp.listMod.add(Triple(currentVersion + 1, Field.RIGHT, copyNode))
                        copyNode.listMod.add(Triple(currentVersion + 1, Field.LEFT, tmp))
                        currentNode = currentNode.next(currentVersion)
                    }
                }
            }
            return Pair(isTailFull,copyNode)
        }
    }

    fun peek(version: Int = this.version): V? {
        return if (this.isEmpty(version))
            null
        else
            this.head(version)!!.value
    }

    private fun findValue(version: Int, value: V): Node<V>? {
        var findNode: Node<V>? = this.head(version)
        while (true) {
            if (findNode == null)
                break
            if (findNode.value == value)
                break
            findNode = findNode.next(version)
        }
        return findNode
    }

    private fun copyRightNodeFull(nextNode: Node<V>): Node<V> {
        var newNextNode: Node<V>? = nextNode
        if (newNextNode!!.isFull(2*this.p)) {
            val (isTailFull, mostRightNotFull) = newNextNode!!.findMostRightNotFull(this.version, 2 * this.p)
            if (isTailFull) {
                this.listModTail.add(Pair(this.version + 1, mostRightNotFull))
                mostRightNotFull.listMod.add(Triple(this.version + 1, Node.Field.RIGHT, null))
            }
            newNextNode = mostRightNotFull
            while (true) {
                if (newNextNode!!.prev(this.version + 1) == null)
                    break
                newNextNode = newNextNode.prev(this.version + 1)
            }
        }
        return newNextNode!!
    }

    private fun copyLeftNodeFull(prevNode: Node<V>): Node<V> {
        var newPrevNode: Node<V>? = prevNode
        if (newPrevNode!!.isFull(2*this.p)) {
            val (isHeadFull, mostLeftNotFull) = newPrevNode.findMostLeftNotFull(this.version, 2*this.p)
            if (isHeadFull) {
                this.listModHead.add(Pair(this.version + 1, mostLeftNotFull))
                mostLeftNotFull.listMod.add(Triple(this.version + 1, Node.Field.LEFT, null))
            }
            newPrevNode = mostLeftNotFull
            while (true) {
                if (newPrevNode!!.next(this.version + 1) == null)
                    break
                newPrevNode = newPrevNode.next(this.version + 1)
            }
        }
        return newPrevNode!!
    }

    fun pushHead(newValue: V): Int {
        val newNode = Node(newValue)
        this.listModHead.add(Pair(this.version + 1, newNode))
        newNode.listMod.add(Triple(this.version + 1, Node.Field.LEFT, null))

        if (this.isEmpty(this.version)) {
            this.listModTail.add(Pair(this.version + 1, newNode))
            newNode.listMod.add(Triple(this.version + 1, Node.Field.RIGHT, null))
        }
        else {
            val nextNode = this.copyRightNodeFull(this.head(this.version)!!)
            nextNode.listMod.add(Triple(this.version + 1, Node.Field.LEFT, newNode))
            newNode.listMod.add(Triple(this.version + 1, Node.Field.RIGHT, nextNode))
        }
        return ++this.version
    }

    fun push(currentValue: V, newValue: V): Int {
        val currentNode = findValue(this.version, currentValue)
        if (currentNode == null) {
            println("В версии ${this.version} элемент ${currentValue} не найден")
            return this.version
        }

        val newCurrentNode = this.copyLeftNodeFull(currentNode)
        val newNode = Node(newValue)
        newCurrentNode.listMod.add(Triple(this.version + 1, Node.Field.RIGHT, newNode))
        newNode.listMod.add(Triple(this.version + 1, Node.Field.LEFT, newCurrentNode))

        val nextNode = currentNode.next(this.version)
        if (nextNode == null) {
            this.listModTail.add(Pair(this.version + 1, newNode))
            newNode.listMod.add(Triple(this.version + 1, Node.Field.RIGHT, null))
        }
        else {
            val newNextNode: Node<V> = this.copyRightNodeFull(nextNode)
            newNextNode.listMod.add(Triple(this.version + 1, Node.Field.LEFT, newNode))
            newNode.listMod.add(Triple(this.version + 1, Node.Field.RIGHT, newNextNode))
        }
        return ++this.version
    }

    private val popHead: Unit
        get() {
            val nextNode = this.copyRightNodeFull(this.head(this.version)!!.next(this.version)!!)
            this.listModHead.add(Pair(this.version + 1, nextNode))
            nextNode.listMod.add(Triple(this.version + 1, Node.Field.LEFT, null))
        }

    private val popTail: Unit
        get() {
            val prevNode = this.copyLeftNodeFull(this.tail(this.version)!!.prev(this.version)!!)
            this.listModTail.add(Pair(this.version + 1, prevNode))
            prevNode.listMod.add(Triple(this.version + 1, Node.Field.RIGHT, null))
        }

    fun pop(value: V): Int {
        val currentNode = this.findValue(this.version, value)
        if (currentNode == null) {
            println("Узел со значением ${value} в версии ${this.version} не найден")
            return this.version
        }

        if (this.size() == 1) {
            this.listModHead.add(Pair(this.version + 1, null))
            this.listModTail.add(Pair(this.version + 1, null))
            return ++this.version
        }

        val prevNode = currentNode.prev(this.version)
        val nextNode = currentNode.next(this.version)

        if (prevNode == null) {
            this.popHead
            return ++this.version
        }

        if (nextNode == null) {
            this.popTail
            return ++this.version
        }

        var newPrevNode = this.copyLeftNodeFull(prevNode)
        var newNextNode = this.copyRightNodeFull(nextNode)
        newNextNode.listMod.add(Triple(this.version + 1, Node.Field.LEFT, newPrevNode))
        newPrevNode.listMod.add(Triple(this.version + 1, Node.Field.RIGHT, newNextNode))
        return ++this.version
    }
}