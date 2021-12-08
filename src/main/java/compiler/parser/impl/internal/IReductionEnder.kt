package compiler.parser.impl.internal

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.Stack

internal interface IReductionEnder {
    fun endReduction(
        parseStack: Stack<IShiftReduceStackItem>,
        itemsToPush: List<IShiftReduceStackItem>
    ): Boolean
}