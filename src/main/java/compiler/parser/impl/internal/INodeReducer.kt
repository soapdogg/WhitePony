package compiler.parser.impl.internal

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack

internal interface INodeReducer {
    fun reduce(
        lookAheadValue: String,
        nodeItem: NodeShiftReduceStackItem,
        parseStack: Stack<IShiftReduceStackItem>
    ): Boolean
}