package compiler.frontend.parser.impl

import compiler.core.constants.TokenizerConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryPostOperatorExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.frontend.parser.impl.internal.IExpressionNodeReducer

internal class UnaryPostExpressionNodeReducer: IExpressionNodeReducer {
    override fun reduceToExpressionNode(
        rightNode: IParsedExpressionNode,
        operator: String,
        parseStack: Stack<IShiftReduceStackItem>
    ) {
        val isIncrement = operator == TokenizerConstants.INCREMENT
        val nodeOperator = if (isIncrement) TokenizerConstants.PLUS_OPERATOR else TokenizerConstants.MINUS_OPERATOR
        val oppositeOperator = if (isIncrement) TokenizerConstants.MINUS_OPERATOR else TokenizerConstants.PLUS_OPERATOR
        val resultNode = ParsedUnaryPostOperatorExpressionNode(rightNode, nodeOperator, oppositeOperator)
        parseStack.push(NodeShiftReduceStackItem(resultNode))
    }
}