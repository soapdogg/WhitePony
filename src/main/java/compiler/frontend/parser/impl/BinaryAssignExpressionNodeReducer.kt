package compiler.frontend.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.frontend.parser.impl.internal.IExpressionNodeReducer

internal class BinaryAssignExpressionNodeReducer: IExpressionNodeReducer {
    override fun reduceToExpressionNode(
        rightNode: IParsedExpressionNode,
        operator: String,
        parseStack: Stack<IShiftReduceStackItem>
    ) {
        val leftItem = parseStack.pop() as NodeShiftReduceStackItem
        val resultNode = ParsedBinaryAssignExpressionNode(leftItem.node, rightNode)
        parseStack.push(NodeShiftReduceStackItem(resultNode))
    }
}