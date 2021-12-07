package compiler.core.stack

class Stack<T> {

    private val elements = mutableListOf<T>()

    fun push(item: T) = elements.add(item)

    fun pop(): T {
        val item = elements.last()
        elements.removeLast()
        return item
    }

    fun isNotEmpty(): Boolean = elements.isNotEmpty()

    fun isEmpty(): Boolean = elements.isEmpty()
}