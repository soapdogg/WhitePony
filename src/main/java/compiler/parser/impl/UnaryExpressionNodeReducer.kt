package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IUnaryExpressionNodeReducer

internal class UnaryExpressionNodeReducer: IUnaryExpressionNodeReducer {
    override fun reduceToUnaryNode(
        insideNode: IParsedExpressionNode,
        operator: String,
        parseStack: Stack<IShiftReduceStackItem>
    ) {
        val resultNode = ParsedUnaryExpressionNode(insideNode, operator)
        parseStack.push(NodeShiftReduceStackItem(resultNode))
    }
}