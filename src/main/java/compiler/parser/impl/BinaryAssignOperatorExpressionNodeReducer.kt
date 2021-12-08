package compiler.parser.impl

import compiler.core.constants.PrinterConstants
import compiler.core.constants.TokenizerConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignOperatorExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IExpressionNodeReducer

internal class BinaryAssignOperatorExpressionNodeReducer: IExpressionNodeReducer {
    override fun reduceToExpressionNode(
        rightNode: IParsedExpressionNode,
        operator: String,
        parseStack: Stack<IShiftReduceStackItem>
    ) {
        val leftItem = parseStack.pop() as NodeShiftReduceStackItem
        val resultNode = ParsedBinaryAssignOperatorExpressionNode(leftItem.node, rightNode, operator.replace(TokenizerConstants.ASSIGN_OPERATOR, PrinterConstants.EMPTY))
        parseStack.push(NodeShiftReduceStackItem(resultNode))
    }
}