package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedInnerExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IExpressionNodeReducer

internal class InnerExpressionNodeReducer: IExpressionNodeReducer {
    override fun reduceToExpressionNode(
        rightNode: IParsedExpressionNode,
        operator: String,
        parseStack: Stack<IShiftReduceStackItem>
    ) {
        parseStack.pop() //LEFT_PARENTHESES
        parseStack.push(NodeShiftReduceStackItem(ParsedInnerExpressionNode(rightNode)))
    }
}