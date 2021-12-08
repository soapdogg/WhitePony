package compiler.parser.impl.internal

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.Stack

internal interface IUnaryExpressionNodeReducer {
    fun reduceToUnaryNode(
        insideNode: IParsedExpressionNode,
        operator: String,
        parseStack: Stack<IShiftReduceStackItem>
    )
}