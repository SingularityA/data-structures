package ru.spbu.datastructures.heap

import org.junit.Assert
import org.junit.Test

/**
 * @author NEA33
 */

class FibonacciHeapTest {

    @Test
    fun testEmptyHeap() {
        val heap = FibonacciHeap<Int>()

        Assert.assertEquals(0, heap.size)
        Assert.assertEquals(true, heap.isEmpty)
        Assert.assertEquals(null, heap.peek)
        Assert.assertEquals(setOf<Int>(), heap.keys)

        heap.print()

    }

    @Test
    fun testPush() {
        val heap1 = FibonacciHeap<Int>()
        val heap2 = FibonacciHeap<Int>()
        (1..10).onEach { heap1.push(it, it*11) }
        (10 downTo 1).onEach { heap2.push(it, it*11) }

        heap2.print()

        Assert.assertEquals(10, heap1.size)
        Assert.assertEquals(11, heap1.peek)
        Assert.assertEquals((1..10).toSet(), heap1.keys)
        Assert.assertEquals(10, heap2.size)
        Assert.assertEquals(11, heap2.peek)
        Assert.assertEquals((1..10).toSet(), heap2.keys)

        heap2.push(1,12)
    }

    @Test
    fun testMerge() {
        val heapEmpty = FibonacciHeap<Int>()
        val heap = FibonacciHeap<Int>()
        heap.push(1, 11)
        //слить пустую кучу и обычную
        heapEmpty.merge(heap)
        Assert.assertEquals(1, heapEmpty.size)
        Assert.assertEquals(11, heapEmpty.peek)
        Assert.assertEquals(setOf(1), heapEmpty.keys)
        Assert.assertEquals(setOf<Int>(), heap.keys)

        //слить обычную кучу и пустую
        heapEmpty.merge(heap)
        Assert.assertEquals(1, heapEmpty.size)
        Assert.assertEquals(11, heapEmpty.peek)
        Assert.assertEquals(setOf(1), heapEmpty.keys)
        Assert.assertEquals(setOf<Int>(), heap.keys)

        //слияние 2-х куч
        val heap1 = FibonacciHeap<Int>()
        val heap2 = FibonacciHeap<Int>()
        (1..5).onEach { heap1.push(it, it * 11) }
        (10 downTo 6).onEach { heap1.push(it, it * 11) }
        (11..15).onEach { heap2.push(it, it * 11) }
        (20 downTo 16).onEach { heap2.push(it, it * 11) }

        heap2.merge(heap1)

        heap2.print()

        Assert.assertEquals(11, heap2.peek)
        Assert.assertEquals(20, heap2.size)
        Assert.assertEquals(true, heap1.isEmpty)
        Assert.assertEquals((1..20).toSet(), heap2.keys)
        Assert.assertEquals(setOf<Int>(), heap1.keys)

        heap2.merge(heapEmpty)
        Assert.assertEquals(20, heap2.size)
        Assert.assertEquals((1..20).toSet(), heap2.keys)
        Assert.assertEquals(1, heapEmpty.size)
        Assert.assertEquals(setOf(1), heapEmpty.keys)
    }

    @Test
    fun consolidate() {
        val heap = FibonacciHeap<Int>()

        heap.push(3, 33)
        heap.push(2, 22)
        heap.push(1, 11)
        heap.push(5, 55)
        heap.push(4, 44)

        heap.consolidate(heap.size)

        heap.print()


        Assert.assertEquals(5, heap.size)
        Assert.assertEquals(11, heap.peek)
    }

    @Test
    fun testPop() {
        val heap = FibonacciHeap<Int>()
        Assert.assertEquals(null, heap.pop)

        heap.push(1, 11)
        Assert.assertEquals(11, heap.pop)
        Assert.assertEquals(null, heap.peek)
        Assert.assertEquals(0, heap.size)

        heap.push(1, 11)
        heap.push(2, 22)
        Assert.assertEquals(11, heap.pop)
        Assert.assertEquals(22, heap.peek)
        Assert.assertEquals(1, heap.size)

        heap.push(1, 11)
        heap.push(3, 33)

        Assert.assertEquals(11, heap.pop)
        Assert.assertEquals(22, heap.peek)
        Assert.assertEquals(2, heap.size)
    }

    @Test
    fun testPopChild() {
        val heap = FibonacciHeap<Int>()
        (1..5).onEach { heap.push(it, it*11) }
        (10 downTo 6).onEach { heap.push(it, it*11) }

        Assert.assertEquals(11, heap.pop)
        Assert.assertEquals(22, heap.peek)
        Assert.assertEquals(9, heap.size)

        Assert.assertEquals(22, heap.pop)
        Assert.assertEquals(33, heap.peek)
        Assert.assertEquals(8, heap.size)


        Assert.assertEquals(33, heap.pop)
        Assert.assertEquals(44, heap.peek)
        Assert.assertEquals(7, heap.size)

        heap.print()
    }

    @Test
    fun testDecreaseKey() {
        val heap = FibonacciHeap<Int>()
        (1..5).onEach { heap.push(it, it*11) }
        (10 downTo 6).onEach { heap.push(it, it*11) }

        Assert.assertEquals(11, heap.pop)
        Assert.assertEquals(22, heap.peek)
        Assert.assertEquals(9, heap.size)

        Assert.assertEquals(22, heap.pop)
        Assert.assertEquals(33, heap.peek)
        Assert.assertEquals(8, heap.size)

        heap.decreaseKey(15,20)
        heap.decreaseKey(5,12)
        heap.decreaseKey(9, -1)
        Assert.assertEquals(99, heap.peek)
    }

    @Test
    fun testDelete() {
        val heap = FibonacciHeap<Int>()
        (1..5).onEach { heap.push(it, it*11) }
        (10 downTo 6).onEach { heap.push(it, it*11) }
        heap.print()
        println()

        Assert.assertEquals(11, heap.pop)
        Assert.assertEquals(22, heap.peek)
        Assert.assertEquals(9, heap.size)
        heap.print()
        println()

        Assert.assertEquals(22, heap.pop)
        Assert.assertEquals(33, heap.peek)
        Assert.assertEquals(8, heap.size)
        heap.print()
        println()

        Assert.assertEquals(77, heap.delete(7))
        Assert.assertEquals(7, heap.size)
        Assert.assertEquals(33, heap.peek)
        heap.print()
        println()

    }
}