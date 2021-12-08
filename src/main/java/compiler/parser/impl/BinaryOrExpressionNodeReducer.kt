package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryOrOperatorExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IBinaryExpressionNodeReducer

internal class BinaryOrExpressionNodeReducer: IBinaryExpressionNodeReducer {
    override fun reduceToBinaryNode(
        rightNode: IParsedExpressionNode,
        operator: String,
        parseStack: Stack<IShiftReduceStackItem>
    ) {
        val leftItem = parseStack.pop() as NodeShiftReduceStackItem
        val resultNode = ParsedBinaryOrOperatorExpressionNode(leftItem.node, rightNode)
        parseStack.push(NodeShiftReduceStackItem(resultNode))
    }
}