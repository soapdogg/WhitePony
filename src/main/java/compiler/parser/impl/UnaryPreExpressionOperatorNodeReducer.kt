package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryPreOperatorExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IUnaryExpressionNodeReducer

internal class UnaryPreExpressionOperatorNodeReducer: IUnaryExpressionNodeReducer {
    override fun reduceToUnaryNode(
        insideNode: IParsedExpressionNode,
        operator: String,
        parseStack: Stack<IShiftReduceStackItem>
    ) {
        val resultNode = ParsedUnaryPreOperatorExpressionNode(insideNode, operator[0].toString())
        parseStack.push(NodeShiftReduceStackItem(resultNode))
    }
}