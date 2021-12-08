package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryNotOperatorExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IUnaryExpressionNodeReducer

internal class UnaryNotExpressionNodeReducer: IUnaryExpressionNodeReducer {
    override fun reduceToUnaryNode(
        insideNode: IParsedExpressionNode,
        operator: String,
        parseStack: Stack<IShiftReduceStackItem>
    ) {
        val resultNode = ParsedUnaryNotOperatorExpressionNode(insideNode)
        parseStack.push(NodeShiftReduceStackItem(resultNode))
    }
}