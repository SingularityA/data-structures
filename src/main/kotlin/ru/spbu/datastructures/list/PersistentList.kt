package ru.spbu.datastructures.list

class PersistentList<V>(val p: Int = 2) {
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

    fun isEmpty(version: Int = this.version): Boolean = this.head(version) == null

    private var listModHead: MutableList<Pair<Int, Node<V>>> = mutableListOf()
    private var listModTail: MutableList<Pair<Int, Node<V>>> = mutableListOf()

    private fun head(version: Int = this.version): Node<V>? = findNode(version, this.listModHead)
    private fun tail(version: Int = this.version): Node<V>? = findNode(version, this.listModTail)

    private fun findNode(version: Int, listMod: MutableList<Pair<Int, Node<V>>>): Node<V>? {
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
        var listMod: MutableList<Triple<Int,Field,Node<V>>> = mutableListOf()
        var left: Node<V>? = null
        var right: Node<V>? = null

        enum class Field {
            RIGHT, LEFT
        }

        fun next(version: Int): Node<V>? {
            if (this.listMod.isEmpty())
                return this.right
            var nextNode = this.right
            for (mod in this.listMod) {
                if (mod.first <= version && mod.second == Field.RIGHT)
                    nextNode = mod.third
            }
            return nextNode
        }

        fun prev(version: Int): Node<V>? {
            if (this.listMod.isEmpty())
                return this.left
            var prevNode = this.left
            for (mod in this.listMod) {
                if (mod.first <= version && mod.second == Field.LEFT)
                    prevNode = mod.third
            }
            return prevNode
        }

        fun isFull (p: Int): Boolean = this.listMod.size >= 2*p

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
                        var tmp = copyNode
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
                        var tmp = copyNode
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

    fun pushHead(newValue: V): Int {
        val newNode = Node(newValue)
        if (this.isEmpty(this.version))
            this.listModTail.add(Pair(this.version + 1, newNode))
        else {
            val nextNode = this.head(this.version)!!.next(this.version)
            if (nextNode == null)
                this.listModTail.add(Pair(this.version + 1, newNode))
            else {
                var newNextNode: Node<V>
                if (newNode.isFull(p)) {
                    val (isTailFull, mostRightNotFull) = nextNode.findMostRightNotFull(this.version, 2 * p)
                    if (isTailFull)
                        this.listModTail.add(Pair(this.version + 1, mostRightNotFull))
                    newNextNode = mostRightNotFull
                    while (true) {
                        if (newNextNode.prev(this.version + 1) == null)
                            break
                        newNextNode = newNextNode.prev(this.version + 1)!!
                    }
                } else
                    newNextNode = nextNode
                newNextNode.listMod.add(Triple(this.version + 1, Node.Field.LEFT, newNode))
                newNode.listMod.add(Triple(this.version + 1, Node.Field.RIGHT, newNextNode))
            }
        }
        this.listModHead.add(Pair(this.version + 1, newNode))
        return ++this.version
    }

    fun push(currentValue: V, newValue: V): Int {
        val currentNode = findValue(this.version, currentValue)
        if (currentNode == null) {
            println("В версии ${this.version} элемент ${currentValue} не найден")
            return this.version
        }
        var newCurrentNode: Node<V>
        if (currentNode.isFull(p)) {
            val (isHeadFull, mostLeftNotFull) = currentNode.findMostLeftNotFull(this.version, 2 * p)
            if (isHeadFull)
                this.listModHead.add(Pair(this.version + 1, mostLeftNotFull))
             newCurrentNode = mostLeftNotFull
            while (true) {
                if (newCurrentNode.next(this.version + 1) == null)
                    break
                newCurrentNode = newCurrentNode.next(this.version + 1)!!
            }
        }
        else
            newCurrentNode = currentNode

        val newNode = Node(newValue)
        newCurrentNode.listMod.add(Triple(this.version + 1, Node.Field.RIGHT, newNode))
        newNode.listMod.add(Triple(this.version + 1, Node.Field.LEFT, newCurrentNode))

        val nextNode = currentNode.next(this.version)
        if (nextNode == null)
            this.listModTail.add(Pair(this.version + 1, newNode))
        else {
            var newNextNode: Node<V>
            if (newNode.isFull(p)) {
                val (isTailFull, mostRightNotFull) = nextNode.findMostRightNotFull(this.version, 2*p)
                if (isTailFull)
                    this.listModTail.add(Pair(this.version + 1, mostRightNotFull))
                newNextNode = mostRightNotFull
                while (true) {
                    if (newNextNode.prev(this.version + 1) == null)
                        break
                    newNextNode = newNextNode.prev(this.version + 1)!!
                }
            }
            else
                newNextNode = nextNode
            newNextNode.listMod.add(Triple(this.version + 1, Node.Field.LEFT, newNode))
            newNode.listMod.add(Triple(this.version + 1, Node.Field.RIGHT, newNextNode))
        }
        return ++this.version
    }
}