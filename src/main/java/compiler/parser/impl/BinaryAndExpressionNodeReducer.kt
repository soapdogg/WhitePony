package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAndOperatorExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IBinaryExpressionNodeReducer

internal class BinaryAndExpressionNodeReducer: IBinaryExpressionNodeReducer {
    override fun reduceToBinaryNode(
        rightNode: IParsedExpressionNode,
        operator: String,
        parseStack: Stack<IShiftReduceStackItem>
    ) {
        val leftItem = parseStack.pop() as NodeShiftReduceStackItem
        val resultNode = ParsedBinaryAndOperatorExpressionNode(leftItem.node, rightNode)
        parseStack.push(NodeShiftReduceStackItem(resultNode))
    }
}