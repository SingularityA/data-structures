package ru.spbu.datastructures.tree

import org.junit.Test

class BTreeTest {

    @Test
    fun testInsert() {
        val tree = ArrayListBTree<Int, Int>(3)

        println(tree.root)

        (1..4).forEach { tree.insert(it, it * 10) }

        println(tree.root?.keys)

        tree.insert(5, 5 * 10)

        println(tree.root?.keys)
        println(tree.root?.children?.first()?.keys)
        println(tree.root?.children?.last()?.keys)
    }
}