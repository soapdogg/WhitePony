package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryNotOperatorExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IExpressionNodeReducer

internal class UnaryNotExpressionNodeReducer: IExpressionNodeReducer {
    override fun reduceToExpressionNode(
        rightNode: IParsedExpressionNode,
        operator: String,
        parseStack: Stack<IShiftReduceStackItem>
    ) {
        val resultNode = ParsedUnaryNotOperatorExpressionNode(rightNode)
        parseStack.push(NodeShiftReduceStackItem(resultNode))
    }
}