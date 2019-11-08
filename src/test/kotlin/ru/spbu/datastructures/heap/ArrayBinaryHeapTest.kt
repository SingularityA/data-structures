package ru.spbu.datastructures.heap

import org.junit.Assert
import org.junit.Test
import java.util.Comparator

class ArrayBinaryHeapTest {

    @Test
    fun testPush() {
        val heap: Heap<Int, Int> = ArrayBinaryHeap()

        (10 downTo 1).onEach { heap.push(it, it * 11) }

        Assert.assertEquals(10, heap.size)
        Assert.assertEquals(11, heap.peek())
    }

    @Test
    fun testPop() {
        val heap = ArrayBinaryHeap(listOf(3, 2, 1, 4, 5), listOf(33, 22, 11, 44, 55))

        val popped = heap.pop()

        Assert.assertEquals(11, popped)
        Assert.assertEquals(4, heap.size)
        Assert.assertEquals(22, heap.peek())
    }

    @Test
    fun testComparator() {
        val heap = ArrayBinaryHeap(listOf(3, 2, 1, 4, 5), listOf(33, 22, 11, 44, 55), Comparator.reverseOrder())
        Assert.assertEquals(55, heap.peek())
    }

    @Test
    fun testMerge() {
        val heap1 = ArrayBinaryHeap(listOf(1, 2, 3), listOf(11, 22, 33))
        val heap2 = ArrayBinaryHeap(listOf(4, 5, 6, 7, 8), listOf(44, 55, 66, 77, 88))

        val mergedHeap = heap1.merge(heap2)

        println(heap1)
        println(heap2)
        println(mergedHeap)

        Assert.assertNotSame(heap1, mergedHeap)
    }

    @Test
    fun testMeld() {
        val heap1 = ArrayBinaryHeap(listOf(1, 2, 3), listOf(11, 22, 33))
        val heap2 = ArrayBinaryHeap(listOf(4, 5, 6, 7, 8), listOf(44, 55, 66, 77, 88))

        val meldedHeap = heap1.meld(heap2)

        println(heap1)
        println(heap2)
        println(meldedHeap)

        Assert.assertSame(heap1, meldedHeap)
    }
}