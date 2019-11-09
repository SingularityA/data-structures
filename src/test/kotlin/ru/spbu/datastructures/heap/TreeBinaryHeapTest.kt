package ru.spbu.datastructures.heap

import org.junit.Assert
import org.junit.Test

/**
 * @author SingularityA
 */
class TreeBinaryHeapTest {

    @Test
    fun testPeek() {
        val emptyHeap = TreeBinaryHeap<Int, Int>()
        Assert.assertEquals(null, emptyHeap.peek())

        val heap = TreeBinaryHeap<Int, Int>()
        heap.push(1, 11)
        heap.push(2, 22)
        heap.push(3, 33)
        heap.push(4, 44)
        heap.push(5, 55)
        heap.print()

        Assert.assertEquals(11, heap.peek())
        Assert.assertEquals(5, heap.size)
    }

    @Test
    fun testComparator() {
        val heap = TreeBinaryHeap<Int, Int>(reverseOrder())
        heap.push(5, 55)
        heap.push(4, 44)
        heap.push(3, 33)
        heap.push(2, 22)
        heap.push(1, 11)
        heap.print()

        Assert.assertEquals(55, heap.peek())
        Assert.assertEquals(5, heap.size)
    }

    @Test
    fun testPush() {
        val heap: Heap<Int, Int> = TreeBinaryHeap()

        (10 downTo 1).onEach { heap.push(it, it * 11) }

        Assert.assertEquals(10, heap.size)
        Assert.assertEquals(11, heap.peek())
    }

    @Test
    fun testPop() {
        val heap = TreeBinaryHeap<Int, Int>()
        heap.push(1, 11)
        heap.push(2, 22)
        heap.push(3, 33)
        heap.push(4, 44)
        heap.push(5, 55)

        val popped = heap.pop()

        Assert.assertEquals(11, popped)
        Assert.assertEquals(4, heap.size)
        Assert.assertEquals(22, heap.peek())
    }
}