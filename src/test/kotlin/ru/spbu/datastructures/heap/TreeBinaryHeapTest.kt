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

        val heap = TreeBinaryHeap(
            (1..15).toList(),
            (11..165 step 11).toList()
        )
        println(heap)

        Assert.assertEquals(11, heap.peek())
        Assert.assertEquals(15, heap.size)
    }

    @Test
    fun testComparator() {
        val heap = TreeBinaryHeap(
            (15 downTo 1).toList(),
            (165 downTo 11 step 11).toList(),
            reverseOrder()
        )
        println(heap)

        Assert.assertEquals(165, heap.peek())
        Assert.assertEquals(15, heap.size)
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
        val heap = TreeBinaryHeap(
            (1..15).toList(),
            (11..165 step 11).toList()
        )

        val popped = heap.pop()

        Assert.assertEquals(11, popped)
        Assert.assertEquals(14, heap.size)
        Assert.assertEquals(22, heap.peek())
    }

    @Test
    fun testReplace() {
        val heap = TreeBinaryHeap(
            (1..7).toList(),
            (11..77 step 11).toList()
        )
        println(heap)

        val replaced = heap.replace(8, 88)

        println(heap)
        Assert.assertEquals(11, replaced)
        Assert.assertEquals(7, heap.size)
        Assert.assertEquals(22, heap.peek())
    }

    @Test
    fun testRemove() {
        val heap = TreeBinaryHeap(
            (1..7).toList(),
            (11..77 step 11).toList()
        )
        println(heap)

        var removed = heap.remove(55)

        println(heap)
        Assert.assertEquals(55, removed)
        Assert.assertEquals(6, heap.size)
        Assert.assertEquals(11, heap.peek())

        removed = heap.remove(11)

        println(heap)
        Assert.assertEquals(11, removed)
        Assert.assertEquals(5, heap.size)
        Assert.assertEquals(22, heap.peek())
    }

    @Test
    fun testPrioritize() {
        val heap = TreeBinaryHeap(
            (1..15).toList(),
            (11..165 step 11).toList()
        )
        println(heap)

        heap.prioritize(5, 0)
        println(heap)

        heap.prioritize(16, 0)
        println(heap)
    }
}