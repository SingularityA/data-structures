package ru.spbu.datastructures.heap

import org.junit.Assert
import org.junit.Test
import ru.spbu.datastructures.list.PersistentList

class PersistentListTest {
    @Test
    fun testEmptyList() {
        var pList = PersistentList<Int>()
        val version = 0

        Assert.assertEquals(null, pList.peek())
        Assert.assertEquals(0, pList.version)
        Assert.assertEquals(true, pList.isEmpty(version))
        Assert.assertEquals(0, pList.size())
    }

    @Test
    fun testCreateList() {
        val pList = PersistentList(listOf(1,2,3,4), 2)

        Assert.assertEquals(1, pList.peek())
        Assert.assertEquals(0, pList.version)
        Assert.assertEquals(4, pList.size())
    }

    @Test
    fun testPush() {
        val pList1 = PersistentList<Int>(1)
        pList1.pushHead(11)
        (2..5).forEach { i -> pList1.push((i-1)*11, i*11) }
        pList1.printAllVersions

        println()

        Assert.assertEquals(5, pList1.version)
        Assert.assertEquals(5, pList1.size())
        Assert.assertEquals(11, pList1.peek())

        val pList2 = PersistentList<Int>(2)
        (1..10).forEach { i -> if (i%2 == 0) pList2.push((i-1)*11, i*11) else pList2.pushHead(i*11) }
        pList2.printAllVersions

        Assert.assertEquals(10, pList2.version)
        Assert.assertEquals(10, pList2.size())
        Assert.assertEquals(99, pList2.peek())
        Assert.assertEquals(11, pList2.peek(1))
    }

    @Test
    fun testPop() {
        val pList = PersistentList<Int>(1)
        (1..10).forEach { i -> if (i%2 == 0) pList.push((i-1)*11, i*11) else pList.pushHead(i*11) }
        (1..5).forEach { i -> pList.pop(2*i*11) }
        pList.printAllVersions
        println()

        Assert.assertEquals(5, pList.size())
        Assert.assertEquals(99, pList.peek())
        Assert.assertEquals(15, pList.version)

        val pList2 = PersistentList(listOf(11,22,33,44,55,66),2)
        (1..3).forEach { i -> pList2.pop(2*i*11) }
        pList2.printAllVersions

        Assert.assertEquals(6, pList2.size(0))
        Assert.assertEquals(3, pList2.size())
        Assert.assertEquals(11, pList2.peek())
        Assert.assertEquals(3, pList2.version)
    }
}