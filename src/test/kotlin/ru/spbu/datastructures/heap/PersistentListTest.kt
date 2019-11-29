package ru.spbu.datastructures.heap

import org.junit.Assert
import org.junit.Test
import ru.spbu.datastructures.list.PersistentList

/**
 * @author NEA33
 * param p >= 3
 */

class PersistentListTest {
    @Test
    fun testEmptyList() {
        val pList = PersistentList<Int>()
        val version = 0

        Assert.assertEquals(null, pList.peek())
        Assert.assertEquals(0, pList.version)
        Assert.assertEquals(true, pList.isEmpty(version))
        Assert.assertEquals(0, pList.size())
    }

    @Test
    fun testCreateList() {
        val pList = PersistentList(listOf(1,2,3,4), 3)
        pList.printAllVersions()

        Assert.assertEquals(1, pList.peek())
        Assert.assertEquals(0, pList.version)
        Assert.assertEquals(4, pList.size())
    }

    @Test
    fun testPush() {
        val pList1 = PersistentList<Int>(3)
        pList1.pushHead(11)
        (2..5).forEach { i -> pList1.push((i-1)*11, i*11) }
        pList1.printAllVersions()


        Assert.assertEquals(5, pList1.version)
        Assert.assertEquals(5, pList1.size())
        Assert.assertEquals(11, pList1.peek())

        val pList2 = PersistentList<Int>(3)
        (1..10).forEach { i -> if (i%2 == 0) pList2.push((i-1)*11, i*11) else pList2.pushHead(i*11) }
        pList2.printAllVersions()

        Assert.assertEquals(10, pList2.version)
        Assert.assertEquals(10, pList2.size())
        Assert.assertEquals(99, pList2.peek())
        Assert.assertEquals(11, pList2.peek(1))
    }

    @Test
    fun testPop() {
        val pList = PersistentList<Int>(3)
        (1..10).forEach { i -> if (i%2 == 0) pList.push((i-1)*11, i*11) else pList.pushHead(i*11) }
        (1..5).forEach { i -> pList.pop(2*i*11) }
        pList.printAllVersions()
        println()

        Assert.assertEquals(5, pList.size())
        Assert.assertEquals(99, pList.peek())
        Assert.assertEquals(15, pList.version)

        val pList2 = PersistentList(listOf(11,22,33,44,55,66),3)
        (1..3).forEach { i -> pList2.pop(2*i*11) }
        pList2.printAllVersions()

        Assert.assertEquals(6, pList2.size(0))
        Assert.assertEquals(3, pList2.size())
        Assert.assertEquals(11, pList2.peek())
        Assert.assertEquals(3, pList2.version)
    }

    @Test
    fun testReplace() {
        val pList = PersistentList<Int>(3)
        pList.pushHead(11)
        (2..5).forEach { i -> pList.push((i-1)*11, i*11) }

        pList.replace(11,66)
        pList.replace(22,77)
        pList.replace(33, 88)
        pList.replace(44, 99)
        pList.replace(55,110)
        pList.replace(88,121)
        pList.replace(77, 132)
        pList.push(132, 154)
        pList.pop(99)
        pList.push(154,165)
        pList.replace(165,176)
        pList.replace(176,187)
        pList.replace(187,198)
        pList.replace(198,209)
        pList.replace(209,220)
        pList.printAllVersions()

    }
}