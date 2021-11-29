package compiler.printer.impl

import compiler.core.stack.Stack
import compiler.printer.impl.internal.IStackGenerator

internal class StackGenerator: IStackGenerator {

    override fun <T> generateNewStack(clazz: Class<T>): Stack<T> {
        return Stack()
    }
}