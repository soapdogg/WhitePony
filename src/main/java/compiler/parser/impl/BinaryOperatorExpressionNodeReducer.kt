package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryOperatorExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IBinaryExpressionNodeReducer

internal class BinaryOperatorExpressionNodeReducer: IBinaryExpressionNodeReducer {
    override fun reduceToBinaryNode(
        rightNode: IParsedExpressionNode,
        operator: String,
        parseStack: Stack<IShiftReduceStackItem>
    ) {
        val leftItem = parseStack.pop() as NodeShiftReduceStackItem
        val resultItem = ParsedBinaryOperatorExpressionNode(leftItem.node, rightNode, operator)
        parseStack.push(NodeShiftReduceStackItem(resultItem))
    }
}