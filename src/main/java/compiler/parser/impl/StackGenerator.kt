package compiler.parser.impl

import compiler.core.stack.Stack
import compiler.parser.impl.internal.IStackGenerator

internal class StackGenerator: IStackGenerator {

    override fun <T> generateNewStack(clazz: Class<T>): Stack<T> {
        return Stack()
    }
}