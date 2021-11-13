package compiler.core

class Stack<T> {

    private val elements = mutableListOf<T>()

    fun push(item: T) = elements.add(item)

    fun pop(): T {
        val item = elements.last()
        elements.removeLast()
        return item
    }

    fun peek(): T = elements.last()

    fun isEmpty(): Boolean = elements.isEmpty()

    fun isNotEmpty(): Boolean = elements.isNotEmpty()

    fun size(): Int = elements.size
}