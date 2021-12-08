package compiler.parser.impl

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IReductionEnder

internal class ReductionEnder : IReductionEnder {
    override fun endReduction(
        parseStack: Stack<IShiftReduceStackItem>,
        itemsToPush: List<IShiftReduceStackItem>
    ): Boolean {
        itemsToPush.forEach {
            parseStack.push(it)
        }
        return false
    }
}