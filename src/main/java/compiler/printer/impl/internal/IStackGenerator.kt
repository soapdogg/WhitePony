package compiler.printer.impl.internal

import compiler.core.stack.Stack

internal interface IStackGenerator {
    fun <T> generateNewStack(clazz: Class<T>): Stack<T>
}