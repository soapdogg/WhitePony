package compiler.parser.impl.internal

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.Stack

internal interface IExpressionNodeReducer {
    fun reduceToExpressionNode(
        rightNode: IParsedExpressionNode,
        operator: String,
        parseStack: Stack<IShiftReduceStackItem>
    )
}