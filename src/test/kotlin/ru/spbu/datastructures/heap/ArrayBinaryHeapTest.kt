package ru.spbu.datastructures.heap

import org.junit.Assert.*
import org.junit.Test
import java.util.Comparator

/**
 * @author SingularityA
 */
class ArrayBinaryHeapTest {

    @Test
    fun testPeek() {
        val heap = ArrayBinaryHeap(
            listOf(3, 2, 1, 4, 5),
            listOf(33, 22, 11, 44, 55)
        )
        assertEquals(11, heap.peek())
    }

    @Test
    fun testPeekComparator() {
        val heap = ArrayBinaryHeap(
            listOf(3, 2, 1, 4, 5),
            listOf(33, 22, 11, 44, 55),
            Comparator.reverseOrder()
        )
        assertEquals(55, heap.peek())
    }

    @Test
    fun testPeekEmpty() {
        val emptyHeap = ArrayBinaryHeap<Int, Int>()
        assertEquals(null, emptyHeap.peek())
        assertEquals(0, emptyHeap.size)
    }

    @Test
    fun testPush() {
        val heap = ArrayBinaryHeap<Int, Int>()
        (10 downTo 1).onEach { heap.push(it, it * 11) }

        assertEquals(10, heap.size)
        assertEquals(11, heap.peek())
    }

    @Test
    fun testPushComparator() {
        val heap = ArrayBinaryHeap<Int, Int>(reverseOrder())
        (10 downTo 1).onEach { heap.push(it, it * 11) }

        assertEquals(10, heap.size)
        assertEquals(110, heap.peek())
    }

    @Test
    fun testPop() {
        val heap = ArrayBinaryHeap(
            listOf(3, 2, 1, 4, 5),
            listOf(33, 22, 11, 44, 55)
        )

        val popped = heap.pop()

        assertEquals(11, popped)
        assertEquals(4, heap.size)
        assertEquals(22, heap.peek())
    }

    @Test
    fun testPopComparator() {
        val heap = ArrayBinaryHeap(
            listOf(3, 2, 1, 4, 5),
            listOf(33, 22, 11, 44, 55),
            reverseOrder()
        )

        val popped = heap.pop()

        assertEquals(55, popped)
        assertEquals(4, heap.size)
        assertEquals(44, heap.peek())
    }

    @Test
    fun testPopEmpty() {
        val emptyHeap = ArrayBinaryHeap<Int, Int>()

        val popped = emptyHeap.pop()

        assertEquals(null, popped)
        assertEquals(0, emptyHeap.size)
    }

    @Test
    fun testReplace() {
        val heap = ArrayBinaryHeap(
            listOf(1, 2, 3, 4, 5),
            listOf(11, 22, 33, 44, 55)
        )

        val replaced = heap.replace(6, 66)

        println(heap)
        assertEquals(5, heap.size)
        assertEquals(22, heap.peek())
        assertEquals(11, replaced)
    }

    @Test
    fun testReplaceComparator() {
        val heap = ArrayBinaryHeap(
            listOf(6, 5, 4, 3, 2),
            listOf(66, 55, 44, 33, 22),
            reverseOrder()
        )

        val replaced = heap.replace(1, 11)

        println(heap)
        assertEquals(5, heap.size)
        assertEquals(55, heap.peek())
        assertEquals(66, replaced)
    }

    @Test
    fun testReplaceEmpty() {
        val emptyHeap = ArrayBinaryHeap<Int, Int>()

        val replaced = emptyHeap.replace(1, 11)
        println(emptyHeap)

        assertEquals(null, replaced)
        assertEquals(1, emptyHeap.size)
        assertEquals(11, emptyHeap.peek())
    }

    @Test
    fun testRemove() {
        val heap = ArrayBinaryHeap(
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(11, 22, 33, 44, 55, 66, 77)
        )
        println(heap)

        var removed = heap.remove(22)
        println(heap)

        assertEquals(6, heap.size)
        assertEquals(11, heap.peek())
        assertEquals(22, removed)

        removed = heap.remove(55)
        println(heap)

        assertEquals(5, heap.size)
        assertEquals(11, heap.peek())
        assertEquals(55, removed)
    }

    @Test
    fun testRemoveComparator() {
        val heap = ArrayBinaryHeap(
            listOf(5, 4, 3, 2, 1),
            listOf(55, 44, 33, 22, 11),
            reverseOrder()
        )
        println(heap)

        var removed = heap.remove(22)
        println(heap)

        assertEquals(4, heap.size)
        assertEquals(55, heap.peek())
        assertEquals(22, removed)

        removed = heap.remove(55)
        println(heap)

        assertEquals(3, heap.size)
        assertEquals(44, heap.peek())
        assertEquals(55, removed)
    }

    @Test
    fun testRemoveEmpty() {
        val emptyHeap = ArrayBinaryHeap<Int, Int>()
        val removed = emptyHeap.remove(11)

        assertEquals(null, removed)
    }

    @Test
    fun testPrioritize() {
        val heap = ArrayBinaryHeap(
            listOf(2, 3, 4, 5, 6, 7),
            listOf(22, 33, 44, 55, 66, 77)
        )

        val prioritized = heap.prioritize(7, 1)

        println(heap)
        assertEquals(1, prioritized)
        assertEquals(77, heap.peek())
        assertEquals(6, heap.size)
    }

    @Test
    fun testPrioritizeComparator() {
        val heap = ArrayBinaryHeap(
            listOf(5, 4, 3, 2, 1),
            listOf(55, 44, 33, 22, 11),
            reverseOrder()
        )

        val prioritized = heap.prioritize(1, 6)

        println(heap)
        assertEquals(6, prioritized)
        assertEquals(11, heap.peek())
    }

    @Test
    fun testPrioritizeEmpty() {
        val emptyHeap = ArrayBinaryHeap<Int, Int>()

        val prioritized = emptyHeap.prioritize(2, 1)

        println(emptyHeap)
        assertEquals(null, prioritized)
        assertEquals(0, emptyHeap.size)
        assertEquals(null, emptyHeap.peek())
    }

    @Test
    fun testMerge() {
        val heap = ArrayBinaryHeap(listOf(1, 2, 3), listOf(11, 22, 33))
        val anotherHeap = ArrayBinaryHeap(listOf(4, 5, 6, 7, 8), listOf(44, 55, 66, 77, 88))

        val mergedHeap = heap.merge(anotherHeap)

        println(heap)
        println(anotherHeap)
        println(mergedHeap)

        assertNotSame(heap, anotherHeap)
        assertEquals(8, mergedHeap.size)
    }

    @Test
    fun testMergeEmpty() {
        val heap = ArrayBinaryHeap<Int, Int>()
        val anotherHeap = ArrayBinaryHeap<Int, Int>()

        val mergedHeap = heap.merge(anotherHeap)

        println(heap)
        println(anotherHeap)
        println(mergedHeap)

        assertNotSame(heap, anotherHeap)
        assertTrue(mergedHeap.isEmpty())
    }

    @Test
    fun testMeld() {
        val heap = ArrayBinaryHeap(listOf(1, 2, 3), listOf(11, 22, 33))
        val anotherHeap = ArrayBinaryHeap(listOf(4, 5, 6, 7, 8), listOf(44, 55, 66, 77, 88))

        val meldedHeap = heap.meld(anotherHeap)

        println(heap)
        println(anotherHeap)
        println(meldedHeap)

        assertSame(heap, meldedHeap)
        assertEquals(8, meldedHeap.size)
    }

    @Test
    fun testMeldEmpty() {
        val heap = ArrayBinaryHeap<Int, Int>()
        val anotherHeap = ArrayBinaryHeap<Int, Int>()

        val meldedHeap = heap.merge(anotherHeap)

        println(heap)
        println(anotherHeap)
        println(meldedHeap)

        assertNotSame(heap, anotherHeap)
        assertTrue(meldedHeap.isEmpty())
    }
}