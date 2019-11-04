package ru.spbu.datastructures.list

class PersistentList<V>(val p: Int = 2) {
    private var listModHead: MutableList<Pair<Int, Node<V>>> = mutableListOf()
    private var listModTail: MutableList<Pair<Int, Node<V>>> = mutableListOf()
    private fun findNode(version: Int, listMod: MutableList<Pair<Int, Node<V>>>): Node<V>? {
        var findNode: Node<V>? = null
        for (mod in listMod)
            if (mod.first <= version)
                findNode = mod.second
        return findNode
    }
    private fun head(version: Int = this.version): Node<V>? = findNode(version, this.listModHead)
    private fun tail(version: Int = this.version): Node<V>? = findNode(version, this.listModTail)

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

        fun push(currentVersion: Int, newValue: V): Pair<Boolean,Node<V>>  {
            var newNode = Node(newValue)
            var nodeIsEnd: Boolean
            var thisNext = this.next(currentVersion)
            if (thisNext != null) {
                thisNext.listMod.add(Triple(currentVersion+1, Field.LEFT, newNode))
                newNode.listMod.add(Triple(currentVersion+1, Field.RIGHT, thisNext))
                nodeIsEnd = false
            }
            else
                nodeIsEnd = true
            this.listMod.add(Triple(currentVersion+1, Field.RIGHT, newNode))
            newNode.listMod.add(Triple(currentVersion, Field.LEFT, this))
            return Pair(nodeIsEnd, newNode)
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
            this.listModTail.add(Pair(this.version+1, newNode))
        else {
            this.head(this.version)!!.listMod.add(Triple(this.version+1, Node.Field.LEFT, newNode))
            newNode.listMod.add(Triple(this.version+1, Node.Field.RIGHT, this.head(this.version)!!))
        }

        this.listModHead.add(Pair(this.version+1, newNode))
        return ++this.version
    }

    fun push(currentValue: V, newValue: V): Int {
        var currentNode = findValue(this.version, currentValue)
        if (currentNode == null) {
            println("В версии ${this.version} элемент ${currentValue} не найден")
            return this.version
        }

        var (nodeIsEnd, node) = currentNode.push(this.version, newValue)
        if (nodeIsEnd)
            this.listModTail.add(Pair(this.version+1, node))

        return ++this.version
    }
}